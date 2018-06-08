package br.com.jmdesenvolvimento.appcomercial.controller.funcionais;

import android.content.Context;

import java.util.HashMap;

import br.com.jmdesenvolvimento.appcomercial.model.dao.SQLiteDatabaseDao;
import br.com.jmdesenvolvimento.appcomercial.model.tabelas.configuracoes.Configuracoes;

public class FuncoesConfiguracao {

    public static void iniciaConfiguracoes(Context context){
        carregaConfiguracoesSimples(context);
    }

    public static void carregaConfiguracoesSimples(Context context){
        SQLiteDatabaseDao dao = new SQLiteDatabaseDao(context);

        VariaveisControle.CONFIGURACOES_SIMPLES = (Configuracoes) dao.select(new Configuracoes(),null);
    }
}
