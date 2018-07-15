package br.com.jmdesenvolvimento.appcomercial.controller.services.conexoes;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import app.jm.funcional.controller.LeituraJson;
import app.jm.funcional.controller.VariaveisControle;
import app.jm.funcional.model.Dispositivo;
import app.jm.funcional.model.entidades.cadastral.pessoas.APessoa;

import java.io.IOException;

import app.jm.funcional.model.entidades.cadastral.pessoas.EmpresaCliente;
import app.jm.funcional.model.entidades.cadastral.pessoas.Usuario;
import br.com.jmdesenvolvimento.appcomercial.controller.exceptions.ExceptionInternet;
import br.com.jmdesenvolvimento.appcomercial.controller.funcionaisAndroid.FuncoesViewAndroid;
import br.com.jmdesenvolvimento.appcomercial.controller.services.RetrofitInicializador;
import br.com.jmdesenvolvimento.appcomercial.model.dao.SQLiteDatabaseDao;
import br.com.jmdesenvolvimento.appcomercial.view.activitys.iniciais.MainActivity;
import retrofit2.Response;

public class ConexaoServiceCadastraEmpresa extends AsyncTask<Void, Void, Integer> {

    private AlertDialog alertDialog;
    private ProgressDialog dialog;
    private AppCompatActivity context;
    private Usuario empresaCliente;


    public ConexaoServiceCadastraEmpresa( AppCompatActivity context, EmpresaCliente tabela){
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
            empresaCliente.setTipoUsuario(VariaveisControle.LOGIN_EMPRESA);

            String jsonEmpresa = LeituraJson.tabelaParaJson(empresaCliente);

            Dispositivo dispositivo = (Dispositivo) dao.select(new Dispositivo(),null, null, null,null,null);
            String token = dispositivo.getToken();
            Log.i("JSON empresaCliente", jsonEmpresa);

           Response response =  new RetrofitInicializador(context).getService().cadastraNovaEmpresa(jsonEmpresa, token).execute();
           if(!response.isSuccessful() || response.body().equals("false") || response.body() == null){
               throw new IOException();

           } else{
               empresaCliente.setId(Integer.parseInt(response.body()+""));
               dao.insert(empresaCliente);

               Usuario usuario = new Usuario();
               usuario.setLogin(empresaCliente.getLogin());
               usuario.setSenha(empresaCliente.getSenha());
               usuario.setPessoa(empresaCliente.getPessoa());
               usuario.setEmpresaClienteId(empresaCliente.getEmpresaClienteId());

               dao.insert(usuario);
               retorno =  TipoOcorrenciasConexao.SUCESSO;
           }
        } catch (NumberFormatException e){
            Log.e("ERRO","Servidor não retornou um número correto.");
            retorno =  TipoOcorrenciasConexao.FALHA_SERVIDOR;
         }catch (ExceptionInternet e){
            retorno =  TipoOcorrenciasConexao.FALHA_SEM_INTERNET;
        } catch (IOException e) {
            retorno =  TipoOcorrenciasConexao.FALHA_SERVIDOR;
        } catch (Exception e){
            e.printStackTrace();
            retorno =  TipoOcorrenciasConexao.FALHA_SERVIDOR;
        }
        dao.close();
        return retorno;
    }

    @Override
    protected void onPostExecute(Integer tipoErro) {
        super.onPostExecute(tipoErro);
        dialog.dismiss();
        switch (tipoErro) {
            case  TipoOcorrenciasConexao.SUCESSO:
                Intent intent = new Intent(context, MainActivity.class);
                context.startActivity(intent);
                break;

            case  TipoOcorrenciasConexao.FALHA_SERVIDOR:
                FuncoesViewAndroid.addAlertaErroServidor(context,true).show();
                break;

            default:
                FuncoesViewAndroid.addAlertaErroServidor(context,true).show();
                break;
        }
    }
}
