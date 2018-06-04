package br.com.jmdesenvolvimento.appcomercial.model.entidades.estoque;

import java.io.Serializable;
import java.util.HashMap;

import br.com.jmdesenvolvimento.appcomercial.model.entidades.Entidade;

public class Grupo extends Entidade implements Serializable {
    private String nome_grupo;

    @Override
    public void setMapAtributos(HashMap<String, Object> map) {

        id = (int) map.get(getIdNome());
        nome_grupo = (String) map.get("nome_grupo");
    }

    public String getNome_grupo() {
        return nome_grupo;
    }

    public void setNome_grupo(String nome_grupo) {
        this.nome_grupo = nome_grupo;
    }
}
