package br.com.jmdesenvolvimento.appcomercial.model.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.jmdesenvolvimento.appcomercial.controller.FuncoesSql;
import com.jmdesenvolvimento.appcomercial.controller.TabelasMapeadas;
import com.jmdesenvolvimento.appcomercial.controller.funcoesGerais.FuncoesGerais;
import com.jmdesenvolvimento.appcomercial.controller.funcoesGerais.VerificaTipos;
import com.jmdesenvolvimento.appcomercial.model.Tabela;
import com.jmdesenvolvimento.appcomercial.model.dao.IConnection;
import com.jmdesenvolvimento.appcomercial.model.entidades.Entidade;
import com.jmdesenvolvimento.appcomercial.model.entidades.cadastral.pessoas.Pessoa;
import com.jmdesenvolvimento.appcomercial.model.tabelasIntermediarias.TabelaProdutosVenda;

public class SQLiteDatabaseDao extends SQLiteOpenHelper implements IConnection {
    public static final String NOME_BANCO = "appcomercial";
    private static final int VERSAO = 6;
    // private Context context;
    private SQLiteDatabase db;

    public SQLiteDatabaseDao(Context context) {
        super(context, NOME_BANCO, null, VERSAO);
      //  this.context = context;
    }


    public void onCreate(SQLiteDatabase db) {
        this.db = db;
        FuncoesSql.createTables(this, FuncoesSql.SQLITE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(newVersion == 6){
            for(Tabela t : TabelasMapeadas.tabelas) {
                String sql = "DROP TABLE IF EXISTS " + t.getNomeTabela(false) +";";
                Log.i("DROP TABLE",t.getNomeTabela(false));
                db.execSQL(sql);
            }
            onCreate(db);
        }
    }

    public void insert(List<?> tabelas){
        for(int i = 0; i < tabelas.size(); i ++){
            Tabela t = (Tabela) tabelas.get(i);
            insert(t);
        }
    }

    public void insert(Tabela tabela) {
        if (tabela.getNomeTabela(true).equals("cliente")) {
            Log.i("cliente", "");
        }
        String nomeEntidade = tabela.getNomeTabela(false);

            if (tabela.getId() == 0) {
                tabela.setId(countIdEntidade(tabela) + 1);
            }

        HashMap<String, Object> map = tabela.getMapAtributos(true);

        SQLiteDatabase db = getWritableDatabase();
        String sql = montaSqlInsert(map,nomeEntidade);
        db.execSQL(sql);
        db.close();
        Log.i("SUCESSO",sql);
    }

    private String montaSqlInsert( HashMap<String, Object> map, String nomeEntidade ){

        String sql = "INSERT INTO " + nomeEntidade;
        String colunas = "(";
        String valores = "(";
        for (String s : map.keySet()) {
            Object atributo = map.get(s);
            if (atributo == null || VerificaTipos.isList(atributo, null)
                    || atributo.equals("")|| atributo.equals(" ")) {
                continue;
            }
            if (VerificaTipos.isString(atributo, null)) {
                colunas += ", " + s;
                String dps = FuncoesGerais.removeCaracteresEspeciais((String) atributo);
                valores += ", '" + dps + "'";

            }else if(VerificaTipos.isCalendar(atributo, null)){
                colunas += ", " + s;
                valores += ", " + FuncoesGerais.calendarToString((Calendar) atributo, FuncoesGerais.yyyyMMdd_HHMMSS,true);

            } else if (FuncoesGerais.isTabela(atributo)) {
                Tabela e = (Tabela) atributo;
                colunas += ", " + s;
                valores += ", " + e.getId();
            }else if(VerificaTipos.isBoolean(atributo, null)){
                colunas += ", " + s;
                valores += ", " + FuncoesGerais.booleanToint((Boolean) atributo);
            } else if(VerificaTipos.isDouble(atributo, null)){ // caso seja real ou integer
                colunas += ", " + s;
                valores += ", " + atributo;
            }else if(VerificaTipos.isInt(atributo, null)){ // caso seja real ou integer
                colunas += ", " + s;
                valores += ", " + atributo;
            } else{
                Log.e("insert","Tipo da variável " + s + " não encontrado! Verifique");
            }
        }
        valores = valores.replace("(,", "(");
        colunas = colunas.replace("(,", "(") + ")";
        sql += colunas + " VALUES " + valores + ");";
        return sql;
    }

    public List<?> selectAll(Tabela tabela, String where, boolean pegaExcluidos) {
        return selectAll(tabela,where,pegaExcluidos,null,null,null,null);
    }

    public List<?> selectAll(Tabela tabela,String where, boolean pegaExcluidos, String orderBy,String limit) {
        return selectAll(tabela,where,pegaExcluidos,null,null,orderBy,limit);
    }

        /**Informe classes que extendem de Tabela. where == null: buscará todos os campos.
         * @param @pegaExcluidos: informe true caso queira pegar valores excluidos de forma lógica*/
    public List<?> selectAll(Tabela tabela, String where, boolean pegaExcluidos,String[] selectionArgs,String groupBy,String orderBy,String limit) {

        // verifica se é para pegar registros excluídos
        if(pegaExcluidos == false){
            if(where != null){
                where += " AND " + tabela.getDataExclusaoNome() + " IS NULL";
            } else{
                where = tabela.getDataExclusaoNome() + " IS NULL";
            }
        }
        int i = countIdEntidade(tabela);
     //   Log.i("buscaTodos", "Entrou");
        SQLiteDatabase db = getReadableDatabase();
        String[] s = tabela.getNomesAtributos();
        Cursor cursor = db.query(tabela.getNomeTabela(false), s, where, selectionArgs, groupBy, null, orderBy,limit);

        HashMap<String, Object> map = tabela.getMapAtributos(false);

        List<Tabela> listEntidades = new ArrayList();

        while (cursor.moveToNext()) {
            Tabela e = adicionaValoresMap(cursor, map, tabela);
           // e.setMapAtributos(map);
            listEntidades.add(e);
        }
        cursor.close();
     //   Log.i("Busca no banco", db.q);
        return listEntidades;

    }
    /**
     * Busca apenas apenas um registro. Usar em casos de id conhecido ou em tabelas com apenas um registro.
     * Neste ultimo caso, informar id == null
     * */
    public Tabela select(Tabela tabela, String id, String where,String groupBy,String orderBy, String limit) {

        SQLiteDatabase db = getReadableDatabase();
        String[] s = tabela.getNomesAtributos();
        HashMap<String, Object> map = tabela.getMapAtributos(false);
        if (id != null) {
            where = tabela.getIdNome() + " = " + id;
        }
        String nomeTabela = tabela.getNomeTabela(false);
        Cursor cursor = db.query(nomeTabela, s, where +"", null, groupBy, null, orderBy,limit);

        while (cursor.moveToNext()) {
            tabela = adicionaValoresMap(cursor, map, tabela);
            continue;
        }
        cursor.close();
        return tabela;
    }
    // verificar a possibilidade de passsar esse metodo para o FuncoesSql
    @Override
    public int countIdEntidadeCriacao(Tabela tabela) {
        return countIdEntidade(db,tabela);
    }

    /**Adicionará os valores encontrados no registro no map de uma nova tabela e retornará essa tabela.
     * Parametros obrigatórios!*/
    private Tabela adicionaValoresMap(Cursor cursor, HashMap<String, Object> map, Tabela tabela) {

       int colunas = cursor.getColumnCount();
       for (int i = 0; i < colunas; i++) {

           String nomeColuna = cursor.getColumnName(i);
           try {
               if (!map.containsKey(nomeColuna)){// || map.get(nomeColuna) == null) {
                   continue;
               }

               Type type = map.get(nomeColuna).getClass();
               String nomeTipo = type.toString().replace("class ", "");
               verificaTiposColunas(nomeTipo, cursor, nomeColuna, map, i, tabela);

           }catch (NullPointerException e){
               Log.i("Coluna nula",nomeColuna);
               e.printStackTrace();
           }
       }
       try {
           Tabela novaTabela = (Tabela) Class.forName(tabela.getClass().getName()).newInstance();
           novaTabela.setMapAtributos(map);
         //  tabela.setMapAtributos(map);
           return novaTabela;

       } catch (InstantiationException e) {
           e.printStackTrace();
       } catch (IllegalAccessException e) {
           e.printStackTrace();
       } catch (ClassNotFoundException e) {
           e.printStackTrace();
       }
       cursor.close();
        return null;
    }

    public List<Tabela> buscaPessoaPorNomeCpf(Tabela entidade, String colunaWhere, String query) {

        Pessoa pessoa = new Pessoa();
        String condicao;
        if(colunaWhere.toLowerCase().contains("cpf")){
            condicao = colunaWhere + " like '" + query + "'";
        } else{
            condicao = colunaWhere+" like '%" + query.replace(" ","%") + "%'";
        }
        //String nomesAtibutosInLinha = entidade.nomesAtibutosInLinha();
        String sql = "SELECT * FROM "+entidade.getNomeTabela(false)
                +" JOIN PESSOA ON "+entidade.getNomeTabelaNomeId()+" = PESSOA." + pessoa.getIdNome()
                + " where " + condicao;
        Log.i("Pesquisa",sql);
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        List<Tabela> listTabela = new ArrayList<>();
        while (cursor.moveToNext()) {
            HashMap<String, Object> map = entidade.getMapAtributos(false);
            entidade = adicionaValoresMap(cursor,map,entidade);
            entidade.setMapAtributos(map);
            listTabela.add(entidade);
        }
        cursor.close();
        return listTabela;

    }

    private HashMap<String, Object> verificaTiposColunas(String nomeTipo, Cursor cursor, String nomeColuna, HashMap<String, Object> map, int i, Tabela tabela) {

        try {
            Object o = map.get(nomeColuna);
            if (FuncoesGerais.isTabela(map.get(nomeColuna))) {
                int idTabela = cursor.getInt(i);

                if(idTabela > 0) { // buscará entidade somente se id > 0
                Tabela tabelaNoBanco = select((Tabela) Class.forName(nomeTipo).newInstance(), idTabela+"", null,null,null,null);
                map.put(nomeColuna, tabelaNoBanco);

                } else{
                    Tabela novaTabela = (Tabela) Class.forName(nomeTipo).newInstance();
                    map.put(nomeColuna,novaTabela);
                }

            } else if (VerificaTipos.isString(o, null)) {
                    map.put(nomeColuna, FuncoesGerais.converteNuloToVazio(cursor.getString(i)));

            } else if (VerificaTipos.isInt(o, null)) {
                    map.put(nomeColuna, FuncoesGerais.corrigeValoresCamposInt(cursor.getInt(i)));

            } else if (VerificaTipos.isDouble(o, null)) {
                    map.put(nomeColuna, FuncoesGerais.corrigeValoresCamposDouble(cursor.getDouble(i)));

            } else if (VerificaTipos.isLong(o, null)) {
                map.put(nomeColuna, FuncoesGerais.corrigeValoresCamposLong(cursor.getLong(i)));

            } else if (VerificaTipos.isList(o, null)) {
                Class clss = tabela.getClass();
                Field listField = clss.getDeclaredField(nomeColuna);
                ParameterizedType listFieldGenericType = (ParameterizedType) listField.getGenericType();
                Class<?> stringListClass = (Class<?>) listFieldGenericType.getActualTypeArguments()[0];
                String nomeClassTabela = stringListClass.getName().replace("class ","");
                Tabela novaTabela = (Tabela) Class.forName(nomeClassTabela).newInstance();
                List list = selectAll(novaTabela, tabela.getNomeTabela(true) + " = " + tabela.getId(),false);
                map.put(nomeColuna, list);
            } else if(VerificaTipos.isCalendar(o, null)){
                map.put(nomeColuna,FuncoesGerais.corrigeValoresCalendar(cursor.getString(i),FuncoesGerais.yyyyMMdd_HHMMSS));

            } else if(VerificaTipos.isBoolean(o, null)){
                map.put(nomeColuna,FuncoesGerais.intToBoolean(cursor.getInt(i)));
            }
        } catch (NoSuchFieldException e) {
            Log.e("Field nao encontrado",nomeColuna +" na TABELA " + tabela.getNomeTabela(false));
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            Log.e("Erro", "Erro ao tentar criar nova instancia no método verificaTiposColunas()");
            e.printStackTrace();
        } catch (NullPointerException e) {
            Log.i("Objeto nulo", "O campo " + nomeColuna + " possivelmente não existe no map");
            e.getStackTrace();
        }
        return map;
    }

    public int countIdEntidade(Tabela tabela) {
        SQLiteDatabase db = getReadableDatabase();
        return countIdEntidade(db,tabela);
    }

    public int countIdEntidade(SQLiteDatabase db, Tabela tabela){
        String nomeTabela = tabela.getNomeTabela(false);
        String id = tabela.getIdNome();
        String[] columns = {"COUNT(" + id + ")"};
        Cursor cursor = db.query(nomeTabela, columns, null, null, id, null, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    /**Ignorar valores vazios somente caso ZEROS não sejam uteis*/
    public void update(Tabela tabela,boolean ignorarValoresVazios){

        String nomeEntidade = tabela.getNomeTabela(false);
        HashMap<String, Object> map = tabela.getMapAtributos(true);
//        if (tabela.isEntidade()) {
//            Entidade entidade = (Entidade) tabela;
//            map.put(entidade.getIdNome(), entidade.getId());
//        }
        SQLiteDatabase db = getWritableDatabase();
        Set<String> set = map.keySet();
        String sql = "UPDATE " + nomeEntidade;
        String colunas = " SET ";
        for (String s : set) {
            Object atributo = map.get(s);
            if ((atributo == null || (atributo + "").equals("0") || (atributo + "").equals("0.0")
                    || atributo.equals("")|| atributo.equals(" ")) && ignorarValoresVazios) {
                continue;
            }
            if(atributo.getClass().getSimpleName().contains("List"))
                continue;

            if (atributo.getClass().toString().trim().toLowerCase().contains("string")) {
                String dps = FuncoesGerais.removeCaracteresEspeciais((String) map.get(s));
                colunas += s +" = '" + dps + "',";

            } else if (FuncoesGerais.isTabela(map.get(s))) {
                Tabela e = (Tabela) atributo;
                colunas += s + " = " + e.getId() +",";
            } else if(VerificaTipos.isCalendar(map.get(s),null)) {
                colunas += s + " = " + FuncoesGerais.calendarToString((Calendar) atributo, FuncoesGerais.yyyyMMdd_HHMMSS, true) + ",";
            }else if(VerificaTipos.isBoolean(map.get(s),null)){
                colunas += s + " = " + FuncoesGerais.booleanToint((Boolean) map.get(s))+",";
            } else{ // caso seja real ou integer
                colunas += s + " = " + atributo +",";
            }
        }
        colunas += ",,";
        colunas = colunas.replace(",,,", " ").replace(",,", " ");

        sql += colunas + " WHERE " + tabela.getIdNome() + " = "+ tabela.getId() + ";";
        db.execSQL(sql);
        db.close();
    }

    @Override
    public void execSQL(String s) {
        db.execSQL(s);
    }

    public void deleteLogico(Tabela tabela){
        SQLiteDatabase db = getWritableDatabase();
        String sql = "UPDATE " + tabela.getNomeTabela(false)
                + " SET " + tabela.getDataExclusaoNome() + " = " + FuncoesGerais.calendarToString(Calendar.getInstance(), FuncoesGerais.yyyyMMdd_HHMMSS, true)
                + " WHERE " + tabela.getIdNome() + " = " +tabela.getId();
        db.execSQL(sql);
        db.close();
    }

    public void updateQtdProdutoVenda(TabelaProdutosVenda tpv){
            String sql = "UPDATE " + tpv.getNomeTabela(false) + " SET qtd = " + tpv.getQtd()
                    + " WHERE " + tpv.getIdNome() + " = " + tpv.getId();
            SQLiteDatabase dao = getWritableDatabase();
            dao.execSQL(sql);
            dao.close();
    }
}
