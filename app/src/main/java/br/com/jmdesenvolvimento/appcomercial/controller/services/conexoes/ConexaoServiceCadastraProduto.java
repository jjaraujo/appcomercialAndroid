package br.com.jmdesenvolvimento.appcomercial.controller.services.conexoes;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.IOException;

import app.jm.funcional.controller.LeituraJson;
import app.jm.funcional.controller.VariaveisControle;
import app.jm.funcional.model.entidades.estoque.Produto;
import br.com.jmdesenvolvimento.appcomercial.controller.exceptions.ExceptionInternet;
import br.com.jmdesenvolvimento.appcomercial.controller.funcionaisAndroid.FuncoesViewAndroid;
import br.com.jmdesenvolvimento.appcomercial.controller.services.RetrofitInicializador;
import br.com.jmdesenvolvimento.appcomercial.model.dao.SQLiteDatabaseDao;
import retrofit2.Response;

public class ConexaoServiceCadastraProduto extends AsyncTask<Void, Void, Integer> {

    private AlertDialog alertDialog;
    private Response response;
    private ProgressDialog dialog;
    private AppCompatActivity context;
    private Produto produto;
    private String mensagemDialog;


    public ConexaoServiceCadastraProduto(AppCompatActivity context, Produto funcionario){
        this.context = context;
        this.produto = funcionario;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog = new ProgressDialog(context);
        dialog.setMessage("Cadastrando produto...");
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        SQLiteDatabaseDao dao = new SQLiteDatabaseDao(context);
        try {
            if(!VariaveisControle.configuracoesSimples.isBackupEmNuvem()){
                produto.setIdNaEmpresa(dao.countIdNaEmpresa(produto));
                dao.insert(produto);
                return TipoOcorrenciasConexao.SUCESSO;
            }
            String json = LeituraJson.tabelaParaJson(produto);
            Log.i("JSON",json);
            response = new RetrofitInicializador(context).getService().cadastraProduto(json).execute();
            produto.setIdNaEmpresa(Integer.parseInt(response.body()+""));
      //      empresaCliente.setId(1);
            if(!response.isSuccessful() || response.body().equals("false") || response.body()== null){
                throw new IOException();
            }
            dao.insert(produto);
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
