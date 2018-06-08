package br.com.jmdesenvolvimento.appcomercial.model.tabelas.configuracoes;

import java.util.HashMap;

import br.com.jmdesenvolvimento.appcomercial.controller.funcionais.Funcoes;
import br.com.jmdesenvolvimento.appcomercial.model.Tabela;

public class Configuracoes extends Tabela{

    private boolean vendaSemCliente;
    private boolean vendaSemEstoque;


    @Override
    public void setMapAtributos(HashMap<String, Object> map) {
        vendaSemCliente = Funcoes.intToBoolean((Integer) map.get("vendaSemCliente"));
        vendaSemEstoque = Funcoes.intToBoolean((Integer) map.get("vendaSemEstoque"));
    }

    public boolean isVendaSemCliente() {
        return vendaSemCliente;
    }

    public void setVendaSemCliente(boolean vendaSemCliente) {
        this.vendaSemCliente = vendaSemCliente;
    }

    public boolean isVendaSemEstoque() {
        return vendaSemEstoque;
    }

    public void setVendaSemEstoque(boolean vendaSemEstoque) {
        this.vendaSemEstoque = vendaSemEstoque;
    }
}
