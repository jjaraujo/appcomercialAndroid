package br.com.jmdesenvolvimento.appcomercial.model.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.jmdesenvolvimento.appcomercial.controller.VariaveisControleG;
import com.jmdesenvolvimento.appcomercial.model.entidades.estoque.Produto;
import com.jmdesenvolvimento.appcomercial.model.tabelasIntermediarias.TabelaProdutosVenda;

public class ProdutoDAO  extends SQLiteDatabaseDao{

    public ProdutoDAO(Context context) {
        super(context);
    }

    public void subtraiEstoque(TabelaProdutosVenda tpv){
        if(!VariaveisControleG.configuracoesSimples.isVendaSemEstoque()) {
            SQLiteDatabase db = getWritableDatabase();
            String sql = "UPDATE PRODUTO set qtd = qtd - " + tpv.getQtd()
                    + " where " + tpv.getProduto().getIdNome() + " = " + tpv.getProduto().getId();
            db.execSQL(sql);
            db.close();
        }
    }

    public void addEstoque( long id, int qtd){
        if(!VariaveisControleG.configuracoesSimples.isVendaSemEstoque()) {
            SQLiteDatabase db = getWritableDatabase();
            String sql = "UPDATE PRODUTO set qtd = qtd + " + qtd
                    + " where " + new Produto().getIdNome() + " = " + id;
            db.execSQL(sql);
            db.close();
        }
    }
}
