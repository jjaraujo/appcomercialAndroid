package br.com.jmdesenvolvimento.appcomercial.model.entidades.estoque;

import java.io.Serializable;
import java.util.HashMap;

import br.com.jmdesenvolvimento.appcomercial.model.entidades.Entidade;

public class Csons extends Entidade implements Serializable {
    private String nome_csons;

    public String getNome_csons() {
        return nome_csons;
    }

    public void setNome_csons(String nome_csons) {
        this.nome_csons = nome_csons;
    }

    @Override
    public void setMapAtributos(HashMap<String, Object> map) {
        id = (int) map.get(getIdNome());
        nome_csons = (String) map.get("nome_csons");
    }
}
