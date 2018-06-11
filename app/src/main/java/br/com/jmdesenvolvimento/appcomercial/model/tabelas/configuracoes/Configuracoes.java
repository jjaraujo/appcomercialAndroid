package br.com.jmdesenvolvimento.appcomercial.model.tabelas.configuracoes;

import java.util.HashMap;

import br.com.jmdesenvolvimento.appcomercial.controller.funcionais.Funcoes;
import br.com.jmdesenvolvimento.appcomercial.model.Tabela;

public class Configuracoes extends Tabela{

    private boolean vendaSemCliente;
    private boolean vendaSemEstoque;
    private String nomeTipoVenda;



    @Override
    public void setMapAtributos(HashMap<String, Object> map) {
        id = (int) map.get(getIdNome());
        vendaSemCliente = Funcoes.intToBoolean((Integer) map.get("vendaSemCliente"));
        vendaSemEstoque = Funcoes.intToBoolean((Integer) map.get("vendaSemEstoque"));
        dataExclusao = (String) map.get("dataExclusao");
        nomeTipoVenda = (String) map.get("nomeTipoVenda");
    }

    public void setVendaSemCliente(boolean vendaSemCliente) {
        this.vendaSemCliente = vendaSemCliente;
    }

    public void setVendaSemEstoque(boolean vendaSemEstoque) {
        this.vendaSemEstoque = vendaSemEstoque;
    }

    public String getNomeTipoVenda() {
        return nomeTipoVenda;
    }

    public void setNomeTipoVenda(String nomeTipoVenda) {
        this.nomeTipoVenda = nomeTipoVenda;
    }

    public boolean isVendaSemCliente() {
        return vendaSemCliente;
    }

    public boolean isVendaSemEstoque() {
        return vendaSemEstoque;
    }

    public  boolean getPrecisaRegistroInicial(){
        return true;
    }

}
