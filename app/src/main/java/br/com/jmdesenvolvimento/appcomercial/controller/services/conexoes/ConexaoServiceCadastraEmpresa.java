package br.com.jmdesenvolvimento.appcomercial.controller.services.conexoes;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.jmdesenvolvimento.appcomercial.model.Dispositivo;
import com.jmdesenvolvimento.appcomercial.model.entidades.cadastral.pessoas.APessoa;

import java.io.IOException;

import br.com.jmdesenvolvimento.appcomercial.controller.exceptions.ExceptionInternet;
import br.com.jmdesenvolvimento.appcomercial.controller.funcionaisAndroid.FuncoesViewAndroid;
import br.com.jmdesenvolvimento.appcomercial.controller.json.LeituraJson;
import br.com.jmdesenvolvimento.appcomercial.controller.services.RetrofitInicializador;
import br.com.jmdesenvolvimento.appcomercial.model.dao.SQLiteDatabaseDao;
import br.com.jmdesenvolvimento.appcomercial.view.activitys.iniciais.MainActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConexaoServiceCadastraEmpresa extends AsyncTask<Void, Void, Integer> {

    private AlertDialog alertDialog;
    private ProgressDialog dialog;
    private AppCompatActivity context;
    private APessoa empresaCliente;
    private final int  FALHA_SEM_INTERNET = 1;
    private final int  FALHA_SERVIDOR = 2;
    private final int SUCESSO = 3;
    private int tipoResposta;


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
        int retorno;
        try {
            dao.insert(empresaCliente.getPessoa());
            Dispositivo dispositivo = (Dispositivo) dao.select(new Dispositivo(),null, null, null,null,null);
            String jsonEmpresa = LeituraJson.tranformaParaJson(context, empresaCliente);
            String token = dispositivo.getToken();
            Log.i("JSON empresaCliente", jsonEmpresa);

           Response response =  new RetrofitInicializador(context).getService().cadastraNovaEmpresa(jsonEmpresa, token).execute();
           if(response.isSuccessful()){
               empresaCliente.setId(Integer.parseInt(response.body()+""));
               dao.insert(empresaCliente);
               retorno =  SUCESSO;
           } else{
               throw new IOException();
           }
        } catch (NumberFormatException e){
            Log.e("ERRO","Servidor não retornou um número correto.");
            retorno = FALHA_SERVIDOR;
         }catch (ExceptionInternet e){
            retorno = FALHA_SEM_INTERNET;
        } catch (IOException e) {
            retorno = FALHA_SERVIDOR;
        } catch (Exception e){
            e.printStackTrace();
            retorno = FALHA_SERVIDOR;
        }
        dao.close();
        return retorno;
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
                FuncoesViewAndroid.addAlertaErroServidor(context,true).show();
                break;

            case FALHA_SEM_INTERNET:
                FuncoesViewAndroid.addAlertaSemInternet(context,true).show();
                break;
            default:
                FuncoesViewAndroid.addAlertaErroServidor(context,true).show();
                break;
        }
    }
}
