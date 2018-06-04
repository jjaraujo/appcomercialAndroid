package br.com.jmdesenvolvimento.appcomercial.model.entidades.estoque;

import java.io.Serializable;
import java.util.HashMap;

import br.com.jmdesenvolvimento.appcomercial.model.entidades.Entidade;

public class TipoItem extends Entidade implements Serializable {
    String nome_tipo_item;

    public String getNome_tipo_item() {
        return nome_tipo_item;
    }

    public void setNome_tipo_item(String nome_tipo_item) {
        this.nome_tipo_item = nome_tipo_item;
    }

    @Override
    public void setMapAtributos(HashMap<String, Object> map) {

        id = (int) map.get(getIdNome());
        nome_tipo_item = (String) map.get("nome_tipoItem");
    }
}
