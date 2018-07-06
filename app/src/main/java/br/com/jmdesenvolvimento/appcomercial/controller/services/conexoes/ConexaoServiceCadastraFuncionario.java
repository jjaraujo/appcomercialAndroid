package br.com.jmdesenvolvimento.appcomercial.controller.services.conexoes;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.jmdesenvolvimento.appcomercial.model.entidades.cadastral.pessoas.APessoa;
import com.jmdesenvolvimento.appcomercial.model.entidades.cadastral.pessoas.Funcionario;

import java.io.IOException;

import br.com.jmdesenvolvimento.appcomercial.controller.exceptions.ExceptionInternet;
import br.com.jmdesenvolvimento.appcomercial.controller.funcionaisAndroid.FuncoesViewAndroid;
import br.com.jmdesenvolvimento.appcomercial.controller.json.LeituraJson;
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
    private final int  FALHA_SEM_INTERNET = 1;
    private final int  FALHA_SERVIDOR = 2;
    private final int SUCESSO = 3;


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

            String json = LeituraJson.tranformaParaJson(context, funcionario);
            Log.i("JSON",json);
            response = new RetrofitInicializador(context).getService().cadastraFuncionario(json).execute();
         //   empresaCliente.setId(Integer.parseInt(response.body()+""));
      //      empresaCliente.setId(1);
//            dao.insert(empresaCliente);
//            dao.close();
        } catch (IOException e) {
            Log.e("Erro","erro no servidor");
            dialog.dismiss();
          //  return FALHA_SERVIDOR;
        } catch (ExceptionInternet e){
            dialog.dismiss();
            e.addMensagem(context, true);
        //    return FALHA_SEM_INTERNET;
        }
        dao.insert(funcionario);
        dao.close();
        return SUCESSO;
    }

    @Override
    protected void onPostExecute(Integer tipoErro) {
        super.onPostExecute(tipoErro);
        dialog.dismiss();
        switch (tipoErro) {
            case SUCESSO:
                context.finish();
                Intent intent = new Intent(context, MainActivity.class);
                context.startActivity(intent);

                break;

            case FALHA_SERVIDOR:
                FuncoesViewAndroid.addAlertDialogErro(context, "Falha na conexão",
                        "Nosso sistema não está respondendo. Tente novamente em instantes e caso o problema persista, entre em contato com o suporte!", true).show();
                break;

            case FALHA_SEM_INTERNET:

                break;
        }
    }
}
