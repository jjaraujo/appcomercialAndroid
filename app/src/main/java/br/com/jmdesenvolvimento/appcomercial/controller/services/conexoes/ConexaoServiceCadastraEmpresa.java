package br.com.jmdesenvolvimento.appcomercial.controller.services.conexoes;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.jmdesenvolvimento.appcomercial.model.entidades.cadastral.pessoas.APessoa;

import java.io.IOException;

import br.com.jmdesenvolvimento.appcomercial.controller.exceptions.ExceptionInternet;
import br.com.jmdesenvolvimento.appcomercial.controller.funcionaisAndroid.FuncoesViewAndroid;
import br.com.jmdesenvolvimento.appcomercial.controller.json.LeituraJson;
import br.com.jmdesenvolvimento.appcomercial.controller.services.RetrofitInicializador;
import br.com.jmdesenvolvimento.appcomercial.model.dao.SQLiteDatabaseDao;
import br.com.jmdesenvolvimento.appcomercial.view.activitys.iniciais.MainActivity;
import retrofit2.Response;

public class ConexaoServiceCadastraEmpresa extends AsyncTask<Void, Void, Integer> {

    private AlertDialog alertDialog;
    private Response response;
    private ProgressDialog dialog;
    private AppCompatActivity context;
    private APessoa empresaCliente;
    private final int  FALHA_SEM_INTERNET = 1;
    private final int  FALHA_SERVIDOR = 2;
    private final int SUCESSO = 3;


    public ConexaoServiceCadastraEmpresa( AppCompatActivity context, APessoa tabela){
        this.context = context;
        this.empresaCliente = tabela;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog = new ProgressDialog(context);
        dialog.setMessage("Aguarde, efetuando seu cadastro");
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        SQLiteDatabaseDao dao = new SQLiteDatabaseDao(context);
        try {

            dao.insert(empresaCliente.getPessoa());

            String json = LeituraJson.tranformaParaJson(context, empresaCliente);
            Log.i("JSON empresaCliente",json);
            response = new RetrofitInicializador(context).getService().cadastraNovaEmpresa(json).execute();
          //  empresaCliente.setId(Integer.parseInt(response.body()+""));
            empresaCliente.setId(1);
        } catch (IOException e) {
            Log.e("Erro","erro no servidor");
            dialog.dismiss();
          //  return FALHA_SERVIDOR;
        } catch (ExceptionInternet e){
            dialog.dismiss();
         //   return FALHA_SEM_INTERNET;
        }

        dao.insert(empresaCliente);
        dao.close();
        return SUCESSO;
    }

    @Override
    protected void onPostExecute(Integer tipoErro) {
        super.onPostExecute(tipoErro);
        dialog.dismiss();
        switch (tipoErro) {
            case SUCESSO:
                Intent intent = new Intent(context, MainActivity.class);
                context.startActivity(intent);
                break;

            case FALHA_SERVIDOR:
                FuncoesViewAndroid.addAlertDialogErro(context, "Falha na conexão",
                        "Nosso sistema não está respondendo. Tente novamente em instantes e caso o problema persista, entre em contato com o suporte!", true).show();
                break;

            case FALHA_SEM_INTERNET:
                FuncoesViewAndroid.addAlertDialogErro(context,"Falha na conexão",
                        "Parece que você não está conectado à Internet. Verifique sua conexão e tente novamente",
                        true).show();
                break;
        }
    }
}
