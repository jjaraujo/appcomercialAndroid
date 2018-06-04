package br.com.jmdesenvolvimento.appcomercial.model.entidades.vendas;

import java.util.HashMap;

import br.com.jmdesenvolvimento.appcomercial.model.entidades.Entidade;

public class TipoPagamentos extends Entidade{
    private String nome;
    private int aceito;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public void setMapAtributos(HashMap<String, Object> map) {
        id = (int) map.get(getIdNome());
        nome = (String) map.get("nome");
        aceito = (int) map.get("aceito");
    }
}
