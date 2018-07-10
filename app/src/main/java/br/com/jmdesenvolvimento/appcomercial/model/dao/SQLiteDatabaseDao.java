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

import com.jmdesenvolvimento.appcomercial.controller.FuncoesSql;
import com.jmdesenvolvimento.appcomercial.model.TabelasMapeadas;
import com.jmdesenvolvimento.appcomercial.controller.funcoesGerais.FuncoesGerais;
import com.jmdesenvolvimento.appcomercial.controller.funcoesGerais.VerificaTipos;
import com.jmdesenvolvimento.appcomercial.model.Tabela;
import com.jmdesenvolvimento.appcomercial.model.dao.IConnection;
import com.jmdesenvolvimento.appcomercial.model.entidades.cadastral.pessoas.Pessoa;
import com.jmdesenvolvimento.appcomercial.model.tabelasIntermediarias.TabelaProdutosVenda;

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

        if(!tabela.usaInsert())
            return;

        if (tabela.getNomeTabela(true).equals("cliente")) {
            Log.i("cliente", "");
        }

        if (tabela.getId() == 0) {
            tabela.setId(countIdEntidade(tabela) + 1);
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
        int i = countIdEntidade(tabela);
        //   Log.i("buscaTodos", "Entrou");
        SQLiteDatabase db = getReadableDatabase();
        String[] s = tabela.getNomesAtributos();
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
    public Tabela select(Tabela tabela, String id, String where, String groupBy, String orderBy, String limit) {

        SQLiteDatabase db = getReadableDatabase();
        String[] s = tabela.getNomesAtributos();
        HashMap<String, Object> map = tabela.getMapAtributos(false);
        if (id != null) {
            where = tabela.getIdNome() + " = " + id;
        }
        String nomeTabela = tabela.getNomeTabela(false);
        CursorRegistros cursor = new CursorRegistros(db.query(nomeTabela, s, where, null, groupBy, null, orderBy, limit));

        while (cursor.moveToNext()) {
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
                + " JOIN PESSOA ON " + entidade.getNomeTabelaNomeId() + " = PESSOA." + pessoa.getIdNome()
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

    public int countIdEntidade(Tabela tabela) {
        SQLiteDatabase db = getReadableDatabase();
        return countIdEntidade(db, tabela);
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

    /**
     * Ignorar valores vazios somente caso ZEROS não sejam uteis
     */
    public void update(Tabela tabela, boolean ignorarValoresVazios) {

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
                    || atributo.equals("") || atributo.equals(" ")) && ignorarValoresVazios) {
                continue;
            }
            if (atributo.getClass().getSimpleName().contains("List"))
                continue;

            if (atributo.getClass().toString().trim().toLowerCase().contains("string")) {
                String dps = FuncoesGerais.removeCaracteresEspeciais((String) map.get(s));
                colunas += s + " = '" + dps + "',";

            } else if (VerificaTipos.isTabela(map.get(s))) {
                Tabela e = (Tabela) atributo;
                colunas += s + " = " + e.getId() + ",";
            } else if (VerificaTipos.isCalendar(map.get(s), null)) {
                colunas += s + " = " + FuncoesGerais.calendarToString((Calendar) atributo, FuncoesGerais.yyyyMMdd_HHMMSS, true) + ",";
            } else if (VerificaTipos.isBoolean(map.get(s), null)) {
                colunas += s + " = " + FuncoesGerais.booleanToint((Boolean) map.get(s)) + ",";
            } else { // caso seja real ou integer
                colunas += s + " = " + atributo + ",";
            }
        }
        colunas += ",,";
        colunas = colunas.replace(",,,", " ").replace(",,", " ");

        sql += colunas + " WHERE " + tabela.getIdNome() + " = " + tabela.getId() + ";";
        db.execSQL(sql);
        db.close();
    }

    @Override
    public void execSQL(String s) {
        db = db == null ? getWritableDatabase() : db;
        db.execSQL(s);
    }

    public void deleteLogico(Tabela tabela) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "UPDATE " + tabela.getNomeTabela(false)
                + " SET " + tabela.getDataExclusaoNome() + " = " + FuncoesGerais.calendarToString(Calendar.getInstance(), FuncoesGerais.yyyyMMdd_HHMMSS, true)
                + " WHERE " + tabela.getIdNome() + " = " + tabela.getId();
        db.execSQL(sql);
        db.close();
    }

    public void updateQtdProdutoVenda(TabelaProdutosVenda tpv) {
        String sql = "UPDATE " + tpv.getNomeTabela(false) + " SET qtd = " + tpv.getQtd()
                + " WHERE " + tpv.getIdNome() + " = " + tpv.getId();
        SQLiteDatabase dao = getWritableDatabase();
        dao.execSQL(sql);
        dao.close();
    }

    public void drop(Tabela tabela) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "DROP TABLE " + tabela.getNomeTabela(false);
        db.execSQL(sql);
    }
}
