package br.com.jmdesenvolvimento.appcomercial.controller.funcionaisAndroid;

import android.content.Context;

import com.jmdesenvolvimento.appcomercial.controller.VariaveisControleG;
import com.jmdesenvolvimento.appcomercial.controller.funcoesGerais.FuncoesConfiguracaoG;
import com.jmdesenvolvimento.appcomercial.controller.funcoesGerais.FuncoesGerais;
import com.jmdesenvolvimento.appcomercial.model.entidades.cadastral.pessoas.EmpresaCliente;

import br.com.jmdesenvolvimento.appcomercial.model.dao.SQLiteDatabaseDao;

public final class FuncoesConfiguracao {

    public static void inicaDadosBasicos(Context context){
       carregaEmpresa(context);
       carregaConfiguracoesSimples(context);
       VariaveisControleG.iConnection = new SQLiteDatabaseDao(context);
    }

    public static void carregaConfiguracoesSimples(Context context){
        SQLiteDatabaseDao dao = new SQLiteDatabaseDao(context);
        FuncoesConfiguracaoG.carregaConfiguracoesSimples(dao);
        dao.close();
    }

    public static void carregaEmpresa(Context context){
        SQLiteDatabaseDao dao = new SQLiteDatabaseDao(context);
        VariaveisControleG.empresaCliente = dao.selectEmpresaCliente();
    }

    public static void gravaDadosEmpresa(Context context,EmpresaCliente empresaCliente){
        SQLiteDatabaseDao dao = new SQLiteDatabaseDao(context);
        dao.insert(empresaCliente.getPessoa());
        dao.insert(empresaCliente);
    }
}
