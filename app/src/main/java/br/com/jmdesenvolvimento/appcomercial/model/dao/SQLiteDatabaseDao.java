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
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import br.com.jmdesenvolvimento.appcomercial.controller.funcionais.Funcoes;
import br.com.jmdesenvolvimento.appcomercial.model.Tabela;
import br.com.jmdesenvolvimento.appcomercial.model.entidades.Entidade;
import br.com.jmdesenvolvimento.appcomercial.model.entidades.cadastral.Estado;
import br.com.jmdesenvolvimento.appcomercial.model.entidades.cadastral.Municipio;
import br.com.jmdesenvolvimento.appcomercial.model.entidades.cadastral.pessoas.Cliente;
import br.com.jmdesenvolvimento.appcomercial.model.entidades.cadastral.pessoas.Fornecedor;
import br.com.jmdesenvolvimento.appcomercial.model.entidades.cadastral.pessoas.Pessoa;
import br.com.jmdesenvolvimento.appcomercial.model.entidades.cadastral.pessoas.Vendedor;
import br.com.jmdesenvolvimento.appcomercial.model.entidades.estoque.Cfop;
import br.com.jmdesenvolvimento.appcomercial.model.entidades.estoque.Csons;
import br.com.jmdesenvolvimento.appcomercial.model.entidades.estoque.Cst;
import br.com.jmdesenvolvimento.appcomercial.model.entidades.estoque.Grupo;
import br.com.jmdesenvolvimento.appcomercial.model.entidades.estoque.Ncm;
import br.com.jmdesenvolvimento.appcomercial.model.entidades.estoque.Produto;
import br.com.jmdesenvolvimento.appcomercial.model.entidades.estoque.TipoItem;
import br.com.jmdesenvolvimento.appcomercial.model.entidades.estoque.Unidade;
import br.com.jmdesenvolvimento.appcomercial.model.entidades.vendas.TipoPagamentos;
import br.com.jmdesenvolvimento.appcomercial.model.entidades.vendas.Venda;
import br.com.jmdesenvolvimento.appcomercial.model.tabelas.TabelaProdutosVenda;
import br.com.jmdesenvolvimento.appcomercial.model.tabelas.Configuracoes;

public class SQLiteDatabaseDao extends SQLiteOpenHelper {
    public static final String NOME_BANCO = "appcomercial";
    private static final int VERSAO = 4;
    Context context;
    private boolean jaPegouListValoresIniciais;

    private Tabela[] tabelas = {
            new TipoPagamentos(),new Pessoa(), new Cliente(), new Vendedor(), new Estado(),
            new Municipio(), new Produto(), new Ncm(), new Grupo(),
            new Fornecedor(), new Cfop(), new Csons(), new TipoItem(),
            new Unidade(), new Vendedor(), new TabelaProdutosVenda(), new Venda(),
             new Cst(), new Configuracoes(), new TipoPagamentos()
    };

    public SQLiteDatabaseDao(Context context) {
        super(context, NOME_BANCO, null, VERSAO);
        this.context = context;
    }

    public void onCreate(SQLiteDatabase db) {
        for (int i = 0; i < tabelas.length; i++) {
            String sql = "CREATE TABLE IF NOT EXISTS " + tabelas[i].getNomeTabela(false) + "(";

            String[] nomeAtributos = tabelas[i].getNomesAtributos();
            // verificar a possibilidade de pegar esses nomes direto do map
            for (int j = 0; j < nomeAtributos.length; j++) {
                String nome = nomeAtributos[j];

                if (nome == null) {
                    continue;
                }
                sql += "," + substituiTiposVariaveis(nomeAtributos[j], tabelas[i]);
            }
            sql = sql.replace("(,", "(");
            sql = sql + ");";
            Log.i("Criando tabela", sql);
            db.execSQL(sql);
            criaRegistrosIniciais(db, tabelas[i]);
        }
    }

    private void criaRegistrosIniciais(SQLiteDatabase db, Tabela tabela){

        if (tabela.getPrecisaRegistroInicial()) {
            if (tabela.getListValoresIniciais() != null && jaPegouListValoresIniciais == false) {
                jaPegouListValoresIniciais = true;
                for (Tabela t : tabela.getListValoresIniciais()) {
                    criaRegistrosIniciais(db, t);
                }
            } else {
                tabela.setId(countIdEntidadeCriacao(tabela,db) + 1);
                String sql = "INSERT INTO " + tabela.getNomeTabela(false);
                HashMap<String, Object> map = tabela.getMapAtributos();
                map.put(tabela.getDataExclusaoNome(),"");
                String colunas = "";
                String valores = "";
                for (String s : map.keySet()) {
                    colunas += s + ",";
                    Object o = map.get(s);
                    String classNome = map.get(s).getClass().getName();
                    if (classNome.toLowerCase().contains("string")) {
                        if(((map.get(s).equals("") || map.get(s) == null))) {
                            o = "null";
                        } else{
                            o = "'" + o + "'";
                        }
                    }
                    valores += o + ",";
                }
                sql += "(" + colunas.substring(0, colunas.length() - 1) + ") VALUES (";
                sql += valores.substring(0, valores.length() - 1) + ")";
                db.execSQL(sql);
            }
        }
    }

    private int countIdEntidadeCriacao(Tabela tabela, SQLiteDatabase db) {
        String nomeTabela = tabela.getNomeTabela(false);
        String id = tabela.getIdNome();
        String[] columns = {"COUNT(" + id + ")"};
        Cursor cursor = db.query(nomeTabela, columns, null, null, id, null, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(newVersion == 4){
            for(Tabela t : tabelas) {
                String sql = "DROP TABLE IF EXISTS " + t.getNomeTabela(false) +";";
                Log.i("DROP TABLE",t.getNomeTabela(false));
                db.execSQL(sql);
            }
            onCreate(db);
        }

    }

    private String substituiTiposVariaveis(String nome, Tabela t) {

        try {
            Field field;
            String sql;
            String nomeTipo;
            if (!nome.toLowerCase().contains("id_")) {
                if (nome.contains(Funcoes.prefixoChaveEstrangeira())) { // caso a coluna seja uma entidade
                    nomeTipo = " INTEGER ";
                } else if(nome.toLowerCase().contains("dataexclusao")) {
                    nomeTipo = " TEXT ";
                } else {
                    field = t.getClass().getDeclaredField(nome);
                    nomeTipo = " " + field.getType().getSimpleName() + " ";

                    if (nomeTipo.toLowerCase().trim().equals("string")) {
                        nomeTipo = " TEXT ";
                    } else if (nomeTipo.toLowerCase().trim().equals("double")) {
                        nomeTipo = " REAL ";
                    } else if(nomeTipo.toLowerCase().trim().equals("boolean")){
                        nomeTipo = " INTEGER ";
                    }
                }
            } else{
                nomeTipo = " INTEGER PRIMARY KEY ";
            }
            sql = nome + nomeTipo;
            return sql;
        } catch (NoSuchFieldException e1) {
            e1.printStackTrace();
        }
        Log.i("atributo nulo", nome + " - tabela" + t.getNomeTabela(false));
        return null;
    }


    public void insert(Tabela tabela) {
        if (tabela.getNomeTabela(true).equals("cliente")) {
            Log.i("cliente", "");
        }
        String nomeEntidade = tabela.getNomeTabela(false);

            if (tabela.getId() == 0) {
                tabela.setId(countIdEntidade(tabela) + 1);
            }

        HashMap<String, Object> map = tabela.getMapAtributos();

        // map.put(entidade.getIdNome(), entidade.getId());

        SQLiteDatabase db = getWritableDatabase();
        Set<String> set = map.keySet();
        String sql = "INSERT INTO " + nomeEntidade;
        String colunas = "(";
        String valores = "(";
        for (String s : set) {
            Object atributo = map.get(s);
            if (atributo == null || atributo.getClass().getSimpleName().contains("List")
                    || atributo.equals("")|| atributo.equals(" ")) {
                continue;
            }
            if (atributo.getClass().toString().trim().toLowerCase().contains("string")) {
                colunas += ", " + s;
                Log.i("ANtes de remover",map.get(s)+"");
                String dps = Funcoes.removeCaracteresEspeciais((String) map.get(s));
                valores += ", '" + dps + "'";
                Log.i("Depois de remover",dps+"");

            } else if (Funcoes.objectExtendsEntidade(map.get(s))) {
                Entidade e = (Entidade) atributo;
                colunas += ", " + s;
                valores += ", " + e.getId();
            } else { // caso seja real ou integer
                colunas += ", " + s;
                valores += ", " + map.get(s);
            }
        }
        valores = valores.replace("(,", "(");
        colunas = colunas.replace("(,", "(") + ")";
        sql += colunas + " VALUES " + valores + ");";
        db.execSQL(sql);
        db.close();
    }
    public List<?> selectAll(Tabela tabela,String where, boolean pegaExcluidos) {
        return selectAll(tabela,where,pegaExcluidos,null,null,null,null);
    }

        /**Informe classes que extendem de Tabela. where == null: buscará todos os campos.
         * pegaExcluidos: informe true caso queira pegar valores excluidos de forma lógica*/
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
        Log.i("buscaTodos", "Entrou");
        SQLiteDatabase db = getReadableDatabase();
        String[] s = tabela.getNomesAtributos();
        Cursor cursor = db.query(tabela.getNomeTabela(false), s, where, selectionArgs, groupBy, null, orderBy,limit);

        HashMap<String, Object> map = tabela.getMapAtributos();

        List<Tabela> listEntidades = new ArrayList();

        while (cursor.moveToNext()) {
            Tabela e = adicionaValoresMap(cursor, map, tabela);
           // e.setMapAtributos(map);
            listEntidades.add(e);
        }
        cursor.close();
        Log.i("buscaTodos", "saiu");
        return listEntidades;

    }
    /**
     * Busca apenas apenas um registro. Usar em casos de id conhecido ou em tabelas com apenas um registro.
     * Neste ultimo caso, informar id == null
     * */
    public Tabela select(Tabela tabela, String id, String where,String groupBy,String orderBy, String limit) {

        SQLiteDatabase db = getReadableDatabase();
        String[] s = tabela.getNomesAtributos();
        HashMap<String, Object> map = tabela.getMapAtributos();
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

    /**Adicionará os valores encontrados no registro no map de uma nova tabela e retornará essa tabela.
     * Parametros obrigatórios!*/
    private Tabela adicionaValoresMap(Cursor cursor, HashMap<String, Object> map, Tabela tabela) {

       int colunas = cursor.getColumnCount();
       for (int i = 0; i < colunas; i++) {

           String nomeColuna = cursor.getColumnName(i);
           try {
               Type type = map.get(nomeColuna).getClass();
               String nomeTipo = type.toString().replace("class ", "");

               if (!map.containsKey(nomeColuna)) {
                   continue;
               }
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

    public List<Tabela> buscaClientesPorNomeCpf(Tabela tabela, String colunaWhere, String query) {

        Pessoa pessoa = new Pessoa();
        String condicao;
        if(colunaWhere.toLowerCase().contains("cpf")){
            condicao = colunaWhere + " = " + query;
        } else{
            condicao = colunaWhere+" like '%" + query.replace(" ","%") + "%'";
        }
        String nomesAtibutosInLinha = tabela.nomesAtibutosInLinha();
        String sql = "SELECT "+ nomesAtibutosInLinha +" FROM "+tabela.getNomeTabela(false)
                +" JOIN PESSOA ON pessoa"+Funcoes.prefixoChaveEstrangeira()+" = " + pessoa.getIdNome()
                + " where " + condicao;

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        List<Tabela> listTabela = new ArrayList<>();
        while (cursor.moveToNext()) {
            HashMap<String, Object> map = tabela.getMapAtributos();
            tabela = (Tabela) adicionaValoresMap(cursor,map,tabela);
            tabela.setMapAtributos(tabela.getMapAtributos());
            listTabela.add(tabela);
        }
        cursor.close();
        return listTabela;

    }

    private HashMap<String, Object> verificaTiposColunas(String nomeTipo, Cursor cursor, String nomeColuna, HashMap<String, Object> map, int i, Tabela tabela) {

        try {
            if (Funcoes.objectExtendsEntidade(map.get(nomeColuna))) {
                int idEntidade = cursor.getInt(i);
                if(idEntidade > 0) { // buscará entidade somente se id > 0
                Tabela tabela1 = select((Entidade) Class.forName(nomeTipo).newInstance(), idEntidade+"", null,null,null,null);
                    map.put(nomeColuna, tabela1);
                } else{
                    tabela = (Entidade) Class.forName(nomeTipo).newInstance();
                    tabela.setMapAtributos(tabela.getMapAtributos());
                    map.put(nomeColuna,tabela);
                }
            } else if (nomeTipo.toLowerCase().contains("string")) {
                if (cursor.getString(i) == null) {
                    map.put(nomeColuna, "");
                } else
                    map.put(nomeColuna, cursor.getString(i));

            } else if (nomeTipo.toLowerCase().contains("integer")) {
                if (cursor.getString(i) == null) {
                    map.put(nomeColuna, 0);
                } else
                    map.put(nomeColuna, cursor.getInt(i));

            } else if (nomeTipo.toLowerCase().contains("double")) {
                if (cursor.getString(i) == null) {
                    map.put(nomeColuna, 0.0);
                } else
                    map.put(nomeColuna, cursor.getDouble(i));

            } else if (nomeTipo.toLowerCase().contains("long")) {
                map.put(nomeColuna, cursor.getLong(i));

            } else if (nomeTipo.toLowerCase().contains("list")) {
                Field listField = tabela.getClass().getField(nomeColuna);
                ParameterizedType listFieldGenericType = (ParameterizedType) listField.getGenericType();
                Class<?> stringListClass = (Class<?>) listFieldGenericType.getActualTypeArguments()[0];
                Tabela novaTabela = (Tabela) Class.forName(stringListClass.getSimpleName()).newInstance();
                List list = selectAll(novaTabela, null,false);
                map.put(nomeColuna, list);

            }
        } catch (NoSuchFieldException e) {
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

    private int countIdEntidade(Tabela tabela) {
        SQLiteDatabase db = getReadableDatabase();
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
        HashMap<String, Object> map = tabela.getMapAtributos();
        if (tabela.isEntidade()) {
            Entidade entidade = (Entidade) tabela;
            map.put(entidade.getIdNome(), entidade.getId());
        }
        SQLiteDatabase db = getWritableDatabase();
        Set<String> set = map.keySet();
        String sql = "UPDATE " + nomeEntidade;
        String colunas = " SET ";
        for (String s : set) {
            Object atributo = map.get(s);
            if(atributo.getClass().getSimpleName().contains("List"))
                continue;
            if ((atributo == null || (atributo + "").equals("0") || (atributo + "").equals("0.0")
                    || atributo.equals("")|| atributo.equals(" ")) && ignorarValoresVazios) {
                continue;
            }

            if (atributo.getClass().toString().trim().toLowerCase().contains("string")) {
                String dps = Funcoes.removeCaracteresEspeciais((String) map.get(s));
                colunas += s +" = '" + dps + "',";

            } else if (Funcoes.objectExtendsEntidade(map.get(s))) {
                Entidade e = (Entidade) atributo;
                colunas += s + " = " + e.getId() +",";
            } else { // caso seja real ou integer
                colunas += s + " = " + atributo +",";
            }
        }
        colunas += ",,";
        colunas = colunas.replace(",,,", " ").replace(",,", " ");

        sql += colunas + " WHERE " + tabela.getIdNome() + " = "+ tabela.getId() + ";";
        db.execSQL(sql);
        db.close();
    }

    public void deleteLogico(Tabela tabela){
        SQLiteDatabase db = getWritableDatabase();
        String sql = "UPDATE " + tabela.getNomeTabela(false)
                + " SET " + tabela.getDataExclusaoNome() + " = '" + Funcoes.getDataHojeDDMMAAAA() + "'"
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
