package br.com.jmdesenvolvimento.appcomercial.controller.funcionaisAndroid;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import java.util.Calendar;

import app.jm.funcional.controller.VariaveisControle;
import app.jm.funcional.controller.funcoesGerais.FuncoesConfiguracaoG;
import app.jm.funcional.controller.funcoesGerais.FuncoesGerais;
import app.jm.funcional.model.entidades.cadastral.pessoas.EmpresaCliente;

import app.jm.funcional.model.entidades.cadastral.pessoas.Usuario;
import app.jm.funcional.model.entidades.vendas.Caixa;
import br.com.jmdesenvolvimento.appcomercial.controller.exceptions.ExceptionInternet;
import br.com.jmdesenvolvimento.appcomercial.controller.services.RetrofitInicializador;
import br.com.jmdesenvolvimento.appcomercial.model.dao.SQLiteDatabaseDao;

public final class FuncoesConfiguracao {

    public static void inicaDadosBasicos(AppCompatActivity context){
       carregaEmpresaEUsuario(context);
       carregaConfiguracoesSimples(context);
       carregaVariaveisControle(context);
       carregaCaixa(context);

    }

    public static void carregaConfiguracoesSimples(Context context){
        SQLiteDatabaseDao dao = new SQLiteDatabaseDao(context);
        FuncoesConfiguracaoG.carregaConfiguracoesSimples(dao);
        dao.close();
    }

    public static void carregaEmpresaEUsuario(Context context){
        SQLiteDatabaseDao dao = new SQLiteDatabaseDao(context);
        VariaveisControle.empresaCliente = dao.selectEmpresaCliente();
        VariaveisControle.usuarioFuncionarioLogado = (Usuario) dao.select(new Usuario(), null, null, null, null,null);
        dao.close();
    }

    public static void gravaDadosEmpresa(Context context,EmpresaCliente empresaCliente){
        SQLiteDatabaseDao dao = new SQLiteDatabaseDao(context);
        dao.insert(empresaCliente.getPessoa());
        dao.insert(empresaCliente);
        dao.close();

    }

    public static void carregaCaixa(Context context){
        SQLiteDatabaseDao dao = new SQLiteDatabaseDao(context);
        String where = "data = " + FuncoesGerais.calendarToString(Calendar.getInstance(),FuncoesGerais.yyyyMMdd_HHMMSS, true);
        Caixa caixa = (Caixa) dao.select(new Caixa(),where,null);
        if(caixa == null){
            caixa = new Caixa();
            caixa.setData(Calendar.getInstance());
            dao.insert(caixa);
        }
    }

    public static void carregaVariaveisControle(AppCompatActivity context){
        VariaveisControle.iConnection = new SQLiteDatabaseDao(context);


        try {
            VariaveisControleAndroid.service = new RetrofitInicializador(context).getService();
        } catch (ExceptionInternet exceptionInternet) {
            exceptionInternet.printStackTrace();
        }

    }

}
