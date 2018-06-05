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
import br.com.jmdesenvolvimento.appcomercial.model.tabelas.TabelaIntermediaria;
import br.com.jmdesenvolvimento.appcomercial.model.tabelas.TabelaProdutosVenda;

public class SQLiteDatabaseDao extends SQLiteOpenHelper {
    public static final String NOME_BANCO = "appcomercial";
    private static final int VERSAO = 2;
    Context context;

    private Tabela[] tabelas = {
            new Pessoa(), new Cliente(), new Vendedor(), new Estado(),
            new Municipio(), new Produto(), new Ncm(), new Grupo(),
            new Fornecedor(), new Cfop(), new Csons(), new TipoItem(),
            new Unidade(), new Vendedor(), new TabelaProdutosVenda(), new Venda(),
            new TipoPagamentos(), new Cst()
    };
///h
    private TabelaIntermediaria[] tabelaIntermediarias = {
            new TabelaProdutosVenda()
    };

    public SQLiteDatabaseDao(Context context) {
        super(context, NOME_BANCO, null, VERSAO);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        for (int i = 0; i < tabelas.length; i++) {
            String sql = "CREATE TABLE IF NOT EXISTS " + tabelas[i].getNomeTabela(false) + "(";

            String[] nomeAtributos = tabelas[i].nomesAtibutos();
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
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(newVersion == 2){
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




    public void insere(Tabela tabela) {
        if (tabela.getNomeTabela(true).equals("cliente")) {
            Log.i("cliente", "");
        }
        String nomeEntidade = tabela.getNomeTabela(false);
        HashMap<String, Object> map = tabela.getMapAtributos();

            if (tabela.getId() == 0) {
                tabela.setId(countIdEntidade(tabela) + 1);
            }
        // map.put(entidade.getIdNome(), entidade.getId());

        SQLiteDatabase db = getWritableDatabase();
        Set<String> set = map.keySet();
        String sql = "INSERT INTO " + nomeEntidade;
        String colunas = "(";
        String valores = "(";
        for (String s : set) {
            Object atributo = map.get(s);
            if (atributo == null || atributo.getClass().getSimpleName().contains("List")
                    || (atributo + "").equals("0") || (atributo + "").equals("0.0")
                    || atributo.equals("")|| atributo.equals(" ")) {
                continue;
            }
            if (atributo.getClass().toString().trim().toLowerCase().contains("string")) {
                colunas += "," + s;
                Log.i("ANtes de remover",map.get(s)+"");
                String dps = Funcoes.removeCaracteresEspeciais((String) map.get(s));
                valores += ",'" + dps + "'";
                Log.i("Depois de remover",dps+"");

            } else if (Funcoes.objectExtendsEntidade(map.get(s))) {
                Entidade e = (Entidade) atributo;
                colunas += "," + s;
                valores += "," + e.getId();
            } else { // caso seja real ou integer
                colunas += "," + s;
                valores += "," + map.get(s);
            }
        }
        valores = valores.replace("(,", "(");
        colunas = colunas.replace("(,", "(") + ")";
        sql += colunas + " VALUES " + valores + ");";
        db.execSQL(sql);
        db.close();
    }

    public List<?> buscaTodos(Tabela tabela, String selection, boolean pegaExcluidos) {

        // verifica se é para pegar registros excluídos
        if(pegaExcluidos == false){
            if(selection != null){
                selection += " AND " + tabela.getDataExclusaoNome() + " IS NULL";
            } else{
                selection = tabela.getDataExclusaoNome() + " IS NULL";
            }
        }
        Log.i("buscaTodos", "Entrou");
        SQLiteDatabase db = getReadableDatabase();
        String[] s = tabela.nomesAtibutos();
        Cursor cursor = db.query(tabela.getNomeTabela(false), s, selection, null, null, null, null);
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

    public Tabela busca(Tabela tabela, String id) {

        SQLiteDatabase db = getReadableDatabase();
        String[] s = tabela.nomesAtibutos();
        String selection = null;
        HashMap<String, Object> map = tabela.getMapAtributos();
        if (tabela.isEntidade()) {
            selection = tabela.getIdNome() + " = " + id;
      //      map.put(((Entidade) tabela).getIdNome(), 0);
        }
        String nomeTabela = tabela.getNomeTabela(false);
        Cursor cursor = db.query(nomeTabela, s, selection, null, null, null, null);


        while (cursor.moveToNext()) {
            tabela = adicionaValoresMap(cursor, map, tabela);
            tabela.setMapAtributos(map);
        }
        cursor.close();
        return tabela;
    }

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
               Log.i("coluna nula",nomeColuna);
               e.printStackTrace();
           }
       }

       try {
           Tabela novaTabela = (Tabela) Class.forName(tabela.getClass().getName()).newInstance();
           novaTabela.setMapAtributos(map);
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

    private Tabela[] adicionaValoresMapJoin(Cursor cursor) {

        int colunas = cursor.getColumnCount();

        for (int i = 0; i < colunas; i++) {
            String nomeColuna = cursor.getColumnName(i);
            for (Tabela t : tabelas) {
                HashMap<String, Object> map = t.getMapAtributos();

                if (map.containsKey(nomeColuna)) {
                    Type type = map.get(nomeColuna).getClass();
                    String nomeTipo = type.toString().replace("class ", "");
                    verificaTiposColunas(nomeTipo, cursor, nomeColuna,map , i, t);
                }
            }
        }
        return tabelas;
    }

//    private void teste(Cursor cursor, HashMap<String, Object> map, String nomeColuna, int i, Tabela tabela) {
//
//        try {
//
//            if (Funcoes.objectExtendsEntidade(map.get(nomeColuna))) {
//                int idEntidade = cursor.getInt(i);
//                Tabela tabela1 = busca((Entidade) Class.forName(nomeTipo).newInstance(), idEntidade + "");
//                map.put(nomeColuna, tabela1);
//            } else {
//                verificaTiposColunas(nomeTipo.toLowerCase(), cursor, nomeColuna, map, i, tabela);
//            }
//        } catch (ClassNotFoundException e) {
//            Log.e("Erro", "Erro ao tentar criar nova instancia Entidade");
//            e.printStackTrace();
//        } catch (InstantiationException e) {
//            Log.e("InstantiationException", e.getMessage());
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            Log.e("IllegalAccessException", e.getMessage());
//            e.printStackTrace();
//        } catch (NullPointerException e) {
//            Log.i("Objeto nulo", "O campo " + nomeColuna + " possivelmente não existe no map");
//            e.getStackTrace();
//        }
//    }

    public List<Tabela> buscaClientesPorNomeCpf(String colunaWhere, String query) {
        Cliente cliente = new Cliente();
        Pessoa pessoa = new Pessoa();
        String condicao;
        if(colunaWhere.toLowerCase().contains("cpf")){
            condicao = colunaWhere + " = " + query;
        } else{
            condicao = colunaWhere+" like '%" + query.replace(" ","%") + "%'";
        }
        String nomesAtibutosInLinha = cliente.nomesAtibutosInLinha();
        String sql = "SELECT "+ nomesAtibutosInLinha +" FROM  CLIENTE CLIENTE JOIN PESSOA PESSOA " +
                     "ON Cliente.pessoa"+Funcoes.prefixoChaveEstrangeira()+" = " + pessoa.getIdNome() +
                     " where " + condicao;

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        List<Tabela> listTabela = new ArrayList<>();
        while (cursor.moveToNext()) {
            HashMap<String, Object> map = cliente.getMapAtributos();
            cliente = (Cliente) adicionaValoresMap(cursor,map,cliente);
            cliente.setMapAtributos(cliente.getMapAtributos());
            listTabela.add(cliente);
        }
        cursor.close();
        return listTabela;

    }

    private void verificaTiposColunas(String nomeTipo, Cursor cursor, String nomeColuna, HashMap<String, Object> map, int i, Tabela tabela) {

        try {
            if (Funcoes.objectExtendsEntidade(map.get(nomeColuna))) {
                int idEntidade = cursor.getInt(i);
                if(idEntidade > 0) { // buscará entidade somente se id > 0
                    Tabela tabela1 = busca((Entidade) Class.forName(nomeTipo).newInstance(), idEntidade + "");
                    map.put(nomeColuna, tabela1);
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
                List list = buscaTodos(novaTabela, null, false);
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

    public void update(Tabela tabela){

        String nomeEntidade = tabela.getNomeTabela(false);
        HashMap<String, Object> map = tabela.getMapAtributos();
        if (tabela.isEntidade()) {
            Entidade entidade = (Entidade) tabela;
        //    if (entidade.getId() == 0) {
         //       entidade.setId(countIdEntidade(entidade) + 1);
          //  }
            map.put(entidade.getIdNome(), entidade.getId());
        }
        SQLiteDatabase db = getWritableDatabase();
        Set<String> set = map.keySet();
        String sql = "UPDATE " + nomeEntidade;
        String colunas = " SET ";
        for (String s : set) {
            Object atributo = map.get(s);
            if (atributo == null || atributo.getClass().getSimpleName().contains("List")
                    || (atributo + "").equals("0") || (atributo + "").equals("0.0")
                    || atributo.equals("")|| atributo.equals(" ")) {
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
