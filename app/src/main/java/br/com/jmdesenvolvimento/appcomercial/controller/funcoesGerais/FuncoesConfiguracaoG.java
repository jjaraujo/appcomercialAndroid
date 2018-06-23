package br.com.jmdesenvolvimento.appcomercial.controller.funcoesGerais;

import br.com.jmdesenvolvimento.appcomercial.controller.funcionaisAndroid.VariaveisControleAndroid;
import br.com.jmdesenvolvimento.appcomercial.model.dao.IConnection;
import br.com.jmdesenvolvimento.appcomercial.model.tabelasIntermediarias.Configuracoes;

public class FuncoesConfiguracaoG {

    public static void iniciaConfiguracoes(IConnection con){
        carregaConfiguracoesSimples(con);
    }

    public static void carregaConfiguracoesSimples(IConnection con){

        VariaveisControleG.configuracoesSimples = (Configuracoes) con.select(new Configuracoes(),"1",null,null,null,null);
        VariaveisControleG.configuracoesSimples.setMapAtributos(VariaveisControleG.configuracoesSimples.getMapAtributos(false));

    }
}
