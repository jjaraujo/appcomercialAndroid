package br.com.jmdesenvolvimento.appcomercial.model.entidades.estoque;

import java.util.HashMap;

import br.com.jmdesenvolvimento.appcomercial.model.entidades.Entidade;

public class Unidade extends Entidade{
    private String nome_unidade;

    @Override
    public void setMapAtributos(HashMap<String, Object> map) {

        id = (int) map.get(getIdNome());
        nome_unidade = (String) map.get("nome_unidade");
    }


    public String getNome_unidade() {
        return nome_unidade;
    }

    public void setNome_unidade(String nome_unidade) {
        this.nome_unidade = nome_unidade;
    }


}
