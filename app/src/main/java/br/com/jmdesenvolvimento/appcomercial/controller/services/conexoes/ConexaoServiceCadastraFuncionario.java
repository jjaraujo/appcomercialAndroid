package br.com.jmdesenvolvimento.appcomercial.controller.services.conexoes;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import app.jm.funcional.controller.LeituraJson;
import app.jm.funcional.model.entidades.cadastral.pessoas.APessoa;
import app.jm.funcional.model.entidades.cadastral.pessoas.Funcionario;

import java.io.IOException;

import br.com.jmdesenvolvimento.appcomercial.controller.exceptions.ExceptionInternet;
import br.com.jmdesenvolvimento.appcomercial.controller.funcionaisAndroid.FuncoesViewAndroid;
import br.com.jmdesenvolvimento.appcomercial.controller.services.RetrofitInicializador;
import br.com.jmdesenvolvimento.appcomercial.model.dao.SQLiteDatabaseDao;
import br.com.jmdesenvolvimento.appcomercial.view.activitys.iniciais.MainActivity;
import retrofit2.Response;

public class ConexaoServiceCadastraFuncionario extends AsyncTask<Void, Void, Integer> {

    private AlertDialog alertDialog;
    private Response response;
    private ProgressDialog dialog;
    private AppCompatActivity context;
    private Funcionario funcionario;
    private String mensagemDialog;


    public ConexaoServiceCadastraFuncionario(AppCompatActivity context, Funcionario funcionario){
        this.context = context;
        this.funcionario = funcionario;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog = new ProgressDialog(context);
        dialog.setMessage("Cadastrando funcionário...");
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        SQLiteDatabaseDao dao = new SQLiteDatabaseDao(context);
        try {


            dao.insert(funcionario.getPessoa());

            String json = LeituraJson.tabelaParaJson(funcionario);
            Log.i("JSON",json);
            response = new RetrofitInicializador(context).getService().cadastraFuncionario(json).execute();
            funcionario.setId(Integer.parseInt(response.body()+""));
      //      empresaCliente.setId(1);
            if(!response.isSuccessful() || response.body().equals("false") || response.body()== null){
                throw new IOException();
            }
            dao.insert(funcionario);
            dao.close();
        } catch (IOException e) {
            Log.e("Erro","erro no servidor");
            dialog.dismiss();
           return  TipoOcorrenciasConexao.FALHA_SERVIDOR;
        } catch (ExceptionInternet e){
            dialog.dismiss();
            e.addMensagem(context, true);
           return  TipoOcorrenciasConexao.FALHA_SEM_INTERNET;
        }
        return  TipoOcorrenciasConexao.SUCESSO;
    }

    @Override
    protected void onPostExecute(Integer tipoErro) {
        super.onPostExecute(tipoErro);
        dialog.dismiss();
        switch (tipoErro) {
            case  TipoOcorrenciasConexao.SUCESSO:
                context.finish();

                break;

            case  TipoOcorrenciasConexao.FALHA_SERVIDOR:
                FuncoesViewAndroid.addAlertDialogErro(context, "Falha na conexão",
                        "Nosso sistema não está respondendo. Tente novamente em instantes e caso o problema persista, entre em contato com o suporte!", true).show();
                break;
        }
    }
}
