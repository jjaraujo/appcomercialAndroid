package br.com.jmdesenvolvimento.appcomercial.model.entidades;


import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

import br.com.jmdesenvolvimento.appcomercial.controller.funcionais.Funcoes;
import br.com.jmdesenvolvimento.appcomercial.model.Tabela;

public abstract class Entidade extends Tabela{
  //  protected HashMap<String, Object> map = new HashMap<>();

    @Override
    public abstract void setMapAtributos(HashMap<String, Object> map);



    @Override
    public String toString() {
        return this.getId() + "";
    }
}
