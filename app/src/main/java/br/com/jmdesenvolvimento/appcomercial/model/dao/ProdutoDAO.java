package br.com.jmdesenvolvimento.appcomercial.model.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import br.com.jmdesenvolvimento.appcomercial.controller.funcionais.VariaveisControle;
import br.com.jmdesenvolvimento.appcomercial.model.entidades.estoque.Produto;
import br.com.jmdesenvolvimento.appcomercial.model.tabelas.TabelaProdutosVenda;

public class ProdutoDAO  extends SQLiteDatabaseDao{

    public ProdutoDAO(Context context) {
        super(context);
    }

    public void subtraiEstoque(TabelaProdutosVenda tpv){
        if(!VariaveisControle.CONFIGURACOES_SIMPLES.isVendaSemEstoque()) {
            SQLiteDatabase db = getWritableDatabase();
            String sql = "UPDATE PRODUTO set qtd = qtd - " + tpv.getQtd()
                    + " where " + tpv.getProduto().getIdNome() + " = " + tpv.getProduto().getId();
            db.execSQL(sql);
            db.close();
        }
    }

    public void addEstoque( int id, int qtd){
        if(!VariaveisControle.CONFIGURACOES_SIMPLES.isVendaSemEstoque()) {
            SQLiteDatabase db = getWritableDatabase();
            String sql = "UPDATE PRODUTO set qtd = qtd + " + qtd
                    + " where " + new Produto().getIdNome() + " = " + id;
            db.execSQL(sql);
            db.close();
        }
    }
}
