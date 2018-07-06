package br.com.jmdesenvolvimento.appcomercial.controller;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;

import com.jmdesenvolvimento.appcomercial.controller.VariaveisControleG;
import com.jmdesenvolvimento.appcomercial.model.Tabela;
import com.jmdesenvolvimento.appcomercial.model.entidades.cadastral.pessoas.EmpresaCliente;

import br.com.jmdesenvolvimento.appcomercial.controller.funcionaisAndroid.FuncoesConfiguracao;
import br.com.jmdesenvolvimento.appcomercial.controller.funcionaisAndroid.FuncoesViewAndroid;
import br.com.jmdesenvolvimento.appcomercial.model.dao.SQLiteDatabaseDao;
import br.com.jmdesenvolvimento.appcomercial.view.activitys.iniciais.VendasAbertasActivity;

public class IniciaAplicacaoTask extends AsyncTask<Void, Void, Void>{

    private ProgressDialog prograssDialog;
    private AppCompatActivity context;
    private String activityInicial;

    public IniciaAplicacaoTask(AppCompatActivity context){
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        prograssDialog = new ProgressDialog(context);
        prograssDialog.setMessage("Carregando dados iniciais...");
        prograssDialog.show();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        FuncoesConfiguracao.inicaDadosBasicos(context);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        prograssDialog.dismiss();
        Class activity = FuncoesViewAndroid.getActivityInicial();
        Intent intent = new Intent(context,activity);
        prograssDialog.dismiss();
        context.startActivity(intent);
    }
}
