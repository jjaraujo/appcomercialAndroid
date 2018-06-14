package br.com.jmdesenvolvimento.appcomercial.controller.funcionais;

import android.content.Context;

import br.com.jmdesenvolvimento.appcomercial.model.dao.SQLiteDatabaseDao;
import br.com.jmdesenvolvimento.appcomercial.model.tabelas.Configuracoes;

public class FuncoesConfiguracao {

    public static void iniciaConfiguracoes(Context context){
        carregaConfiguracoesSimples(context);
    }

    public static void carregaConfiguracoesSimples(Context context){

        SQLiteDatabaseDao dao = new SQLiteDatabaseDao(context);

        VariaveisControle.configuracoesSimples = (Configuracoes) dao.select(new Configuracoes(),"1",null,null,null,null);
        VariaveisControle.configuracoesSimples.setMapAtributos(VariaveisControle.configuracoesSimples.getMapAtributos());
    }
}
