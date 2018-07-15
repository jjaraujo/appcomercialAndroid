package br.com.jmdesenvolvimento.appcomercial.model.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import app.jm.funcional.controller.FuncoesSql;
import app.jm.funcional.model.TabelasMapeadas;
import app.jm.funcional.controller.funcoesGerais.FuncoesGerais;
import app.jm.funcional.controller.funcoesGerais.VerificaTipos;
import app.jm.funcional.model.Tabela;
import app.jm.funcional.model.dao.IConnection;
import app.jm.funcional.model.entidades.cadastral.pessoas.Pessoa;
import app.jm.funcional.model.tabelasIntermediarias.TabelaProdutosVenda;

public class SQLiteDatabaseDao extends SQLiteOpenHelper implements IConnection {
    public static final String NOME_BANCO = "appcomercial";
    private static final int VERSAO = 6;
    private SQLiteDatabase db;

    public SQLiteDatabaseDao(Context context) {
        super(context, NOME_BANCO, null, VERSAO);
    }


    public void onCreate(SQLiteDatabase db) {
        this.db = db;
        FuncoesSql.createTables(this, FuncoesSql.SQLITE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion == 6) {
            for (Tabela t : TabelasMapeadas.tabelas) {
                String sql = "DROP TABLE IF EXISTS " + t.getNomeTabela(false) + ";";
                Log.i("DROP TABLE", t.getNomeTabela(false));
                db.execSQL(sql);
            }
            onCreate(db);
        }
    }

    public void insert(List<?> tabelas) {
        for (int i = 0; i < tabelas.size(); i++) {
            Tabela t = (Tabela) tabelas.get(i);
            insert(t);
        }
    }

    public void insert(Tabela tabela) {

      //  tabela.getMapAtributos(false).put("dataExclusao",null);
        if(!tabela.usaInsert())
            return;

        if (tabela.getNomeTabela(true).equals("cliente")) {
            Log.i("cliente", "");
        }

        if (tabela.getId() == 0) {
            tabela.geraId(this);
        }

        SQLiteDatabase db = getWritableDatabase();
        String sql = FuncoesSql.montaSqlInsert(tabela, FuncoesSql.SQLITE);
        db.execSQL(sql);
        db.close();
        Log.i("SUCESSO", sql);
    }

    public List<?> selectAll(Tabela tabela, String where, boolean pegaExcluidos) {
        return selectAll(tabela, where, pegaExcluidos, null, null, null, null);
    }

    public List<?> selectAll(Tabela tabela, String where, boolean pegaExcluidos, String orderBy, String limit) {
        return selectAll(tabela, where, pegaExcluidos, null, null, orderBy, limit);
    }

    /**
     * Informe classes que extendem de Tabela. where == null: buscará todos os campos.
     *
     * @param @pegaExcluidos: informe true caso queira pegar valores excluidos de forma lógica
     */
    public List<?> selectAll(Tabela tabela, String where, boolean pegaExcluidos, String[] selectionArgs, String groupBy, String orderBy, String limit) {

        // verifica se é para pegar registros excluídos
        if (pegaExcluidos == false) {
            if (where != null) {
                where += " AND " + tabela.getDataExclusaoNome() + " IS NULL";
            } else {
                where = tabela.getDataExclusaoNome() + " IS NULL";
            }
        }
        //   Log.i("buscaTodos", "Entrou");
        SQLiteDatabase db = getReadableDatabase();
        String[] s = selectionArgs == null ? tabela.getNomesAtributos() : selectionArgs;
        CursorRegistros cursor = new CursorRegistros(db.query(tabela.getNomeTabela(false), s, where, selectionArgs, groupBy, null, orderBy, limit));

        List<Tabela> listEntidades = new ArrayList();

        while (cursor.moveToNext()) {
            Tabela e = FuncoesSql.percorreColunasSqlEAdicionaNoMap(this, cursor, tabela);
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
     */
    public Tabela select(Tabela tabela, String id,String[] colunasPesquisar, String where, String groupBy, String orderBy, String limit) {

        SQLiteDatabase db = getReadableDatabase();
        String[] s = tabela.getNomesAtributos();
        HashMap<String, Object> map = tabela.getMapAtributos(false);
        if (id != null) {
            where = tabela.getIdNome() + " = " + id;
        }
        String nomeTabela = tabela.getNomeTabela(false);
        CursorRegistros cursor = new CursorRegistros(db.query(nomeTabela, s, where, null, groupBy, null, orderBy, limit));

        while (cursor.moveToNext()) {
            db.close();
            return FuncoesSql.percorreColunasSqlEAdicionaNoMap(this, cursor, tabela);
        }
        cursor.close();
        return null;
    }


    // verificar a possibilidade de passsar esse metodo para o FuncoesSql
    @Override
    public int countIdEntidadeCriacao(Tabela tabela) {
        return countIdEntidade(db, tabela);
    }


    public List<Tabela> buscaPessoaPorNomeCpf(Tabela entidade, String colunaWhere, String query) {

        Pessoa pessoa = new Pessoa();
        String condicao;
        if (colunaWhere.toLowerCase().contains("cpf")) {
            condicao = colunaWhere + " like '" + query + "'";
        } else {
            condicao = colunaWhere + " like '%" + query.replace(" ", "%") + "%'";
        }
        //String nomesAtibutosInLinha = entidade.nomesAtibutosInLinha();
        String sql = "SELECT * FROM " + entidade.getNomeTabela(false)
                + " JOIN Pessoa P ON pessoa = P." + pessoa.getIdNome()
                + " where " + condicao;
        Log.i("Pesquisa", sql);
        SQLiteDatabase db = getReadableDatabase();
        CursorRegistros cursor = new CursorRegistros(db.rawQuery(sql, null));
        List<Tabela> listTabela = new ArrayList<>();
        while (cursor.moveToNext()) {
            HashMap<String, Object> map = entidade.getMapAtributos(false);
            entidade = FuncoesSql.percorreColunasSqlEAdicionaNoMap(this, cursor, entidade);
            entidade.setMapAtributos(map);
            listTabela.add(entidade);
        }
        cursor.close();
        return listTabela;

    }


    public int countIdEntidade(SQLiteDatabase db, Tabela tabela) {
        String nomeTabela = tabela.getNomeTabela(false);
        String id = tabela.getIdNome();
        String[] columns = {"COUNT(" + id + ")"};
        Cursor cursor = db.query(nomeTabela, columns, null, null, id, null, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    @Override
    public void execSQL(String s) {
        if(db == null || !db.isOpen()){
            db = getWritableDatabase();
            db.execSQL(s);
            db.close();
        } else {
            db.execSQL(s);
        }
    }

    public void updateQtdProdutoVenda(TabelaProdutosVenda tpv) {
        String sql = "UPDATE " + tpv.getNomeTabela(false) + " SET qtd = " + tpv.getQtd()
                + " WHERE " + tpv.getIdNome() + " = " + tpv.getId();
        SQLiteDatabase dao = getWritableDatabase();
        dao.execSQL(sql);
        dao.close();
    }
}
