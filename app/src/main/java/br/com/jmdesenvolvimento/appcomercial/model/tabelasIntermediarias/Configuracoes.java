package br.com.jmdesenvolvimento.appcomercial.model.tabelasIntermediarias;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import br.com.jmdesenvolvimento.appcomercial.controller.funcoesGerais.FuncoesGerais;
import br.com.jmdesenvolvimento.appcomercial.model.Tabela;

public class Configuracoes extends Tabela{

    private boolean vendaSemCliente;
    private boolean vendaSemEstoque;
    private String nomeTipoVenda;



    @Override
    public void setMapAtributos(HashMap<String, Object> map) {
        id = (int) map.get(getIdNome());
        vendaSemCliente = (boolean) map.get("vendaSemCliente");
        vendaSemEstoque = (boolean) map.get("vendaSemEstoque");
        dataExclusao = (Calendar) map.get("dataExclusao");
        nomeTipoVenda = (String) map.get("nomeTipoVenda");
    }

    @Override
    public List<Tabela> getListValoresIniciais() {
        this.vendaSemCliente = true;
        this.vendaSemEstoque = false;
        this.nomeTipoVenda = "Comanda";
        List list = new ArrayList();
        list.add(this);
        return list;
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
