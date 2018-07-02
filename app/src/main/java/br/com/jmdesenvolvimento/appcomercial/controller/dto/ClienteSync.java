package br.com.jmdesenvolvimento.appcomercial.controller.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

import com.jmdesenvolvimento.appcomercial.model.entidades.cadastral.pessoas.Cliente;


public class ClienteSync {
    private List<Cliente> clientes;

    public List<Cliente> getClientes() {
        return clientes;
    }

    public void setClientes(List<Cliente> clientes) {
        this.clientes = clientes;
    }


}
