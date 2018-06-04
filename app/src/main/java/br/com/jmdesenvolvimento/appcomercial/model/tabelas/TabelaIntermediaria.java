package br.com.jmdesenvolvimento.appcomercial.model.tabelas;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

import br.com.jmdesenvolvimento.appcomercial.controller.funcionais.Funcoes;
import br.com.jmdesenvolvimento.appcomercial.model.Tabela;

public abstract class TabelaIntermediaria extends Tabela {

    public abstract  void setMapAtributos(HashMap<String, Object> map);

}
