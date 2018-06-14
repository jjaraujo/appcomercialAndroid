package br.com.jmdesenvolvimento.appcomercial.model.entidades.vendas;

import java.util.HashMap;
import java.util.List;

import br.com.jmdesenvolvimento.appcomercial.controller.funcionais.Funcoes;
import br.com.jmdesenvolvimento.appcomercial.model.entidades.Entidade;
import br.com.jmdesenvolvimento.appcomercial.model.entidades.cadastral.pessoas.Cliente;
import br.com.jmdesenvolvimento.appcomercial.model.tabelas.TabelaPagamento;
import br.com.jmdesenvolvimento.appcomercial.model.tabelas.TabelaProdutosVenda;

public class Venda extends Entidade {

    private Cliente cliente;
    private int numeroMesaComanda;
    private List<TabelaProdutosVenda> tabelaProdutosVenda;
    private String dataRegistro;
    private List<TabelaPagamento> tipoPagamentos;
    private String dataFechamento;
    private String dataCancelamento;
    private String motivoCancelamento;
    private double desconto;

    @Override
    public void setMapAtributos(HashMap<String, Object> map) {
        id = (int) map.get(getIdNome());
        cliente = (Cliente) map.get("cliente" + Funcoes.prefixoChaveEstrangeira());
        dataRegistro = (String) map.get("dataRegistro");
        tipoPagamentos =(List<TabelaPagamento>) map.get("tabelaPagamentos");
        tabelaProdutosVenda = ( List<TabelaProdutosVenda>) map.get("tabelaProdutosVenda");
        dataFechamento = (String) map.get("dataFechamento");
        dataCancelamento = (String) map.get("dataCancelamento");
        motivoCancelamento = (String) map.get("motivoCancelamento");
        numeroMesaComanda = (int) map.get("numeroMesaComanda");
        desconto = (double) map.get("desconto");
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<TabelaProdutosVenda> getTabelaProdutosVenda() {
        return tabelaProdutosVenda;
    }

    public void setTabelaProdutosVenda(List<TabelaProdutosVenda> tabelaProdutosVenda) {
        this.tabelaProdutosVenda = tabelaProdutosVenda;
    }

    public int getNumeroMesaComanda() {
        return numeroMesaComanda;
    }

    public void setNumeroMesaComanda(int numeroMesaComanda) {
        this.numeroMesaComanda = numeroMesaComanda;
    }

    public String getDataRegistro() {
        return dataRegistro;
    }

    public void setDataRegistro(String dataRegistro) {
        this.dataRegistro = dataRegistro;
    }

    public List<TabelaPagamento> getTipoPagamentos() {
        return tipoPagamentos;
    }

    public void setTipoPagamentos(List<TabelaPagamento> tipoPagamentos) {
        this.tipoPagamentos = tipoPagamentos;
    }

    public String getDataFechamento() {
        return dataFechamento;
    }

    public void setDataFechamento(String dataFechamento) {
        this.dataFechamento = dataFechamento;
    }

    public String getDataCancelamento() {
        return dataCancelamento;
    }

    public void setDataCancelamento(String dataCancelamento) {
        this.dataCancelamento = dataCancelamento;
    }

    public String getMotivoCancelamento() {
        return motivoCancelamento;
    }

    public void setMotivoCancelamento(String motivoCancelamento) {
        this.motivoCancelamento = motivoCancelamento;
    }

    public double getDesconto() {
        return desconto;
    }

    public void setDesconto(double desconto) {
        this.desconto = desconto;
    }

    @Override
    public String toString() {
        return getId() + " - Cliente: " + cliente.getId();
    }
}
