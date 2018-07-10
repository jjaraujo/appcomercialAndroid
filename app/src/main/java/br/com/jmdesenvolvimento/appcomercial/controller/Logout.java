package br.com.jmdesenvolvimento.appcomercial.controller;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;

import com.jmdesenvolvimento.appcomercial.controller.VariaveisControleG;
import com.jmdesenvolvimento.appcomercial.model.Configuracoes;
import com.jmdesenvolvimento.appcomercial.model.entidades.cadastral.pessoas.Cliente;
import com.jmdesenvolvimento.appcomercial.model.entidades.cadastral.pessoas.EmpresaCliente;
import com.jmdesenvolvimento.appcomercial.model.entidades.cadastral.pessoas.Fornecedor;
import com.jmdesenvolvimento.appcomercial.model.entidades.cadastral.pessoas.Funcionario;
import com.jmdesenvolvimento.appcomercial.model.entidades.contas.ContaReceber;
import com.jmdesenvolvimento.appcomercial.model.entidades.estoque.Produto;
import com.jmdesenvolvimento.appcomercial.model.entidades.vendas.Venda;
import com.jmdesenvolvimento.appcomercial.model.tabelasIntermediarias.TabelaPagamento;
import com.jmdesenvolvimento.appcomercial.model.tabelasIntermediarias.TabelaParcelasPagamento;
import com.jmdesenvolvimento.appcomercial.model.tabelasIntermediarias.TabelaProdutosVenda;

import br.com.jmdesenvolvimento.appcomercial.controller.funcionaisAndroid.VariaveisControleAndroid;
import br.com.jmdesenvolvimento.appcomercial.model.dao.SQLiteDatabaseDao;
import br.com.jmdesenvolvimento.appcomercial.view.activitys.iniciais.LoginActivity;

public class Logout extends AsyncTask<Void, Void, Void>{

    private AppCompatActivity appCompatActivity;
    private ProgressDialog progressDialog;

    public Logout(AppCompatActivity appCompatActivity){
        this.appCompatActivity = appCompatActivity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(appCompatActivity);
        progressDialog.setMessage("Saindo...");
        progressDialog.show();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        VariaveisControleG.funcionarioLogado = null;
        VariaveisControleG.empresaCliente = null;
        SQLiteDatabaseDao dao = new SQLiteDatabaseDao(appCompatActivity);
        String sql =
        "DELETE FROM " + new EmpresaCliente().getNomeTabela(true) + ";" +
        "DELETE FROM " + new Venda().getNomeTabela(true) + ";" +
        "DELETE FROM " + new Cliente().getNomeTabela(true) + ";" +
        "DELETE FROM " + new Produto().getNomeTabela(true) + ";" +
        "DELETE FROM " + new Configuracoes().getNomeTabela(true) + ";" +
        "DELETE FROM " + new Funcionario().getNomeTabela(true) + ";" +
        "DELETE FROM " + new Fornecedor().getNomeTabela(true) + ";" +
        "DELETE FROM " + new TabelaPagamento().getNomeTabela(true) + ";" +
        "DELETE FROM " + new TabelaParcelasPagamento().getNomeTabela(true) + ";" +
        "DELETE FROM " + new TabelaProdutosVenda().getNomeTabela(true) + ";" +
        "DELETE FROM " + new ContaReceber().getNomeTabela(true) + ";";
        dao.execSQL(sql);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        Intent intent = new Intent(appCompatActivity,LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        progressDialog.dismiss();
        appCompatActivity.startActivity(intent);
        appCompatActivity.finish();
    }



}
