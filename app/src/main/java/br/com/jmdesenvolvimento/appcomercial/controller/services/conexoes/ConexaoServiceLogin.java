package br.com.jmdesenvolvimento.appcomercial.controller.services.conexoes;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;

import app.jm.funcional.controller.LeituraJson;
import app.jm.funcional.controller.VariaveisControle;
import app.jm.funcional.controller.funcoesGerais.EstrategiaLogin;
import app.jm.funcional.model.entidades.cadastral.pessoas.EmpresaCliente;
import app.jm.funcional.model.entidades.cadastral.pessoas.Usuario;

import java.io.IOException;

import br.com.jmdesenvolvimento.appcomercial.controller.exceptions.ExceptionInternet;
import br.com.jmdesenvolvimento.appcomercial.controller.funcionaisAndroid.FuncoesViewAndroid;
import br.com.jmdesenvolvimento.appcomercial.controller.services.RetrofitInicializador;
import br.com.jmdesenvolvimento.appcomercial.model.dao.SQLiteDatabaseDao;
import br.com.jmdesenvolvimento.appcomercial.view.activitys.iniciais.MainActivity;
import retrofit2.Response;

public class ConexaoServiceLogin extends AsyncTask<Void, Void, Integer> {

    private ProgressDialog progressDialog;
    private AppCompatActivity context;
    private String login;
    private String senha;

    public ConexaoServiceLogin(AppCompatActivity context, String login, String senha) {
        this.context = context;
        this.login = login;
        this.senha = senha;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Entrando...");
        progressDialog.setCancelable(false);
        progressDialog.show();

    }

    @Override
    protected Integer doInBackground(Void... voids) {

        try {
            String senhaCriptografada = EstrategiaLogin.criptografaSenha(senha);
            String loginMaiusculo = login.toUpperCase();

            Response response = new RetrofitInicializador(context).getService().login(loginMaiusculo, senhaCriptografada).execute();

            if (!response.isSuccessful())
                throw new IOException();

            if(response.body().equals("null")) {
                FuncoesViewAndroid.addAlertDialogErro(context, "Falha no login", "Usuário ou senha inválidos!", false).show();
                return  0;
            }

            Usuario usuario = (Usuario) LeituraJson.jsonParaTabela(String.valueOf(response.body()));

            // caso o usuário seja empresa, nao precisará fazer a segunda requisição
            if(usuario.getNomeCompletoTabela().equals(new EmpresaCliente().getNomeCompletoTabela())){
                sucessoLogin(usuario, (EmpresaCliente) usuario);
                return TipoOcorrenciasConexao.SUCESSO;
            }

            Response responseEmpresaCliente = new RetrofitInicializador(context).getService().getTabela(new EmpresaCliente().getNomeTabela(false), usuario.getEmpresaClienteId() == 0 ? usuario.getId() : usuario.getEmpresaClienteId()).execute();

            if (!responseEmpresaCliente.isSuccessful() || responseEmpresaCliente.body() == null)
                throw new IOException();

            EmpresaCliente empresaCliente = (EmpresaCliente) LeituraJson.jsonParaTabela(String.valueOf(responseEmpresaCliente.body()));

            sucessoLogin(usuario, empresaCliente);

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
                break;
            case TipoOcorrenciasConexao.SUCESSO:
                context.finish();

                break;

            case TipoOcorrenciasConexao.FALHA_SERVIDOR:
                FuncoesViewAndroid.addAlertDialogErro(context, "Falha na conexão",
                        "Nosso sistema não está respondendo. Tente novamente em instantes e caso o problema persista, entre em contato com o suporte!", false).show();
                break;

            default:
                FuncoesViewAndroid.addAlertDialogErro(context, "Falha no login", "Algo deu errado, contate o suporte", false).show();
        }
    }

    private void sucessoLogin(Usuario usuario, EmpresaCliente empresaCliente) {
        try {
            SQLiteDatabaseDao dao = new SQLiteDatabaseDao(context);

            if(!usuario.getNomeCompletoTabela().equals(new EmpresaCliente().getNomeCompletoTabela())){

                Usuario usuarioBanco = new Usuario();
                dao.insert(usuario.getPessoa());

                usuarioBanco.setPessoa(usuario.getPessoa());
                usuarioBanco.setSenha(usuario.getSenha());
                usuarioBanco.setLogin(usuario.getLogin());
                dao.insert(usuarioBanco);
            }

            dao.insert(empresaCliente.getPessoa());
            dao.insert(empresaCliente);

            dao.close();
            Intent intent = new Intent(context, MainActivity.class);
            context.startActivity(intent);

        } catch (Exception e) {
            FuncoesViewAndroid.addAlertDialogErro(context, "Falha no login", "Não foi possível efetuar seu login, contate o suporte! Mensagem: " + e.getMessage(), false);
        }
    }

}
