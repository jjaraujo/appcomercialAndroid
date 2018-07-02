package br.com.jmdesenvolvimento.appcomercial.controller.funcionaisAndroid;

import android.content.Context;

import com.jmdesenvolvimento.appcomercial.controller.funcoesGerais.FuncoesConfiguracaoG;
import br.com.jmdesenvolvimento.appcomercial.model.dao.SQLiteDatabaseDao;

public final class FuncoesConfiguracao {

    public static void iniciaConfiguracoes(Context context){
        carregaConfiguracoesSimples(context);
    }

    public static void carregaConfiguracoesSimples(Context context){
        SQLiteDatabaseDao dao = new SQLiteDatabaseDao(context);
        FuncoesConfiguracaoG.carregaConfiguracoesSimples(dao);
        dao.close();
    }
}
