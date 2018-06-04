package br.com.jmdesenvolvimento.appcomercial.model.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import br.com.jmdesenvolvimento.appcomercial.model.tabelas.TabelaProdutosVenda;

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
}
