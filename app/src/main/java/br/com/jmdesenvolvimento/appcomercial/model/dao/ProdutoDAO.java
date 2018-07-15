package br.com.jmdesenvolvimento.appcomercial.model.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import app.jm.funcional.controller.VariaveisControle;
import app.jm.funcional.model.entidades.estoque.Produto;
import app.jm.funcional.model.tabelasIntermediarias.TabelaProdutosVenda;

public class ProdutoDAO  extends SQLiteDatabaseDao{

    public ProdutoDAO(Context context) {
        super(context);
    }

    public void subtraiEstoque(TabelaProdutosVenda tpv){
            SQLiteDatabase db = getWritableDatabase();
            String sql = "UPDATE PRODUTO set qtd = qtd - " + tpv.getQtd()
                    + " where " + tpv.getProduto().getIdNome() + " = " + tpv.getProduto().getId();
            db.execSQL(sql);
            db.close();
    }

    public void addEstoque( long id, int qtd){
        if(!VariaveisControle.configuracoesSimples.isVendaSemEstoque()) {
            SQLiteDatabase db = getWritableDatabase();
            String sql = "UPDATE PRODUTO set qtd = qtd + " + qtd
                    + " where " + new Produto().getIdNome() + " = " + id;
            db.execSQL(sql);
            db.close();
        }
    }
}
