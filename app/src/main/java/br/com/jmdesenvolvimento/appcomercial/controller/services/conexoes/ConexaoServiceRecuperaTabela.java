package br.com.jmdesenvolvimento.appcomercial.controller.services.conexoes;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;

import java.io.IOException;
import java.util.List;

import app.jm.funcional.controller.LeituraJson;
import app.jm.funcional.controller.VariaveisControle;
import app.jm.funcional.controller.funcoesGerais.EstrategiaLogin;
import app.jm.funcional.model.Tabela;
import app.jm.funcional.model.TabelasMapeadas;
import app.jm.funcional.model.entidades.cadastral.pessoas.EmpresaCliente;
import app.jm.funcional.model.entidades.cadastral.pessoas.Usuario;
import br.com.jmdesenvolvimento.appcomercial.controller.exceptions.ExceptionInternet;
import br.com.jmdesenvolvimento.appcomercial.controller.funcionaisAndroid.FuncoesViewAndroid;
import br.com.jmdesenvolvimento.appcomercial.controller.services.RetrofitInicializador;
import br.com.jmdesenvolvimento.appcomercial.model.dao.SQLiteDatabaseDao;
import br.com.jmdesenvolvimento.appcomercial.view.activitys.iniciais.MainActivity;
import retrofit2.Response;

public class ConexaoServiceRecuperaTabela extends AsyncTask<Void, Void, Integer> {

    private ProgressDialog progressDialog;
    private AppCompatActivity context;

    public ConexaoServiceRecuperaTabela(AppCompatActivity context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Recuperando os dados da loja...");
        progressDialog.setCancelable(false);
        progressDialog.show();

    }

    @Override
    protected Integer doInBackground(Void... voids) {

        try {

            SQLiteDatabaseDao dao = new SQLiteDatabaseDao(context);
            for(Tabela tabela : TabelasMapeadas.tabelas) {
                if (tabela.isbackup()) {
                 //   progressDialog.set("Recuperando dados de " +tabela.getNomeTabela(false));
                    Response response = new RetrofitInicializador(context)
                            .getService()
                            .getListTabela(tabela.getNomeTabela(false), VariaveisControle.empresaCliente.getId())
                            .execute();

                    if (!response.isSuccessful())
                        throw new IOException();

                    if (response.body()==null) {
                        continue;
                    }

                    List<Tabela> tabelas = LeituraJson.jsonParaList(String.valueOf(response.body()));
                    dao.insert(tabelas);
                }
                dao.close();
            }
        } catch (IOException e) {
            return TipoOcorrenciasConexao.FALHA_SERVIDOR;

        } catch (ExceptionInternet exceptionInternet) {
            exceptionInternet.addMensagem(context, false);
            return TipoOcorrenciasConexao.FALHA_SEM_INTERNET;

        } catch (Exception e) {
            return TipoOcorrenciasConexao.FALHA_SERVIDOR;
        }

        return TipoOcorrenciasConexao.SUCESSO;
    }

    @Override
    protected void onPostExecute(Integer tipoErro) {
        super.onPostExecute(tipoErro);
        progressDialog.dismiss();

        switch (tipoErro) {
            case 0:
                FuncoesViewAndroid.addAlertDialogErro(context, "Falha no login", "Usuário ou senha inválidos!", false).show();
                break;
            case TipoOcorrenciasConexao.SUCESSO:
         //       context.finish();

                break;

            case TipoOcorrenciasConexao.FALHA_SERVIDOR:
                FuncoesViewAndroid.addAlertDialogErro(context, "Falha na conexão",
                        "Nosso sistema não está respondendo. Tente novamente em instantes e caso o problema persista, entre em contato com o suporte!", false).show();
                break;

            default:
                FuncoesViewAndroid.addAlertDialogErro(context, "Falha no login", "Algo deu errado, contate o suporte", false).show();
        }
    }
}
