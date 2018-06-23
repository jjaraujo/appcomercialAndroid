package br.com.jmdesenvolvimento.appcomercial.model.entidades.vendas;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import br.com.jmdesenvolvimento.appcomercial.controller.funcoesGerais.FuncoesGerais;
import br.com.jmdesenvolvimento.appcomercial.controller.funcionaisAndroid.VariaveisControleAndroid;
import br.com.jmdesenvolvimento.appcomercial.controller.funcoesGerais.VariaveisControleG;
import br.com.jmdesenvolvimento.appcomercial.model.entidades.Entidade;
import br.com.jmdesenvolvimento.appcomercial.model.entidades.cadastral.pessoas.Cliente;
import br.com.jmdesenvolvimento.appcomercial.model.tabelasIntermediarias.TabelaPagamento;
import br.com.jmdesenvolvimento.appcomercial.model.tabelasIntermediarias.TabelaProdutosVenda;

public class Venda extends Entidade {

    private Cliente cliente;
    private int numeroMesaComanda;
    private List<TabelaProdutosVenda> tabelaProdutosVenda;
    private Calendar dataRegistro;
    private List<TabelaPagamento> tabelaPagamentos;
    private Calendar dataFechamento;
    private Calendar dataCancelamento;
    private String motivoCancelamento;
    private double desconto;

    @Override
    public void setMapAtributos(HashMap<String, Object> map) {
        id = (int) map.get(getIdNome());
        cliente = (Cliente) map.get("cliente");//+ FuncoesGerais.prefixoChaveEstrangeira());
        dataRegistro = (Calendar) map.get("dataRegistro");
        tabelaPagamentos =(List<TabelaPagamento>) map.get("tabelaPagamentos");
        tabelaProdutosVenda = ( List<TabelaProdutosVenda>) map.get("tabelaProdutosVenda");
        dataFechamento = (Calendar) map.get("dataFechamento");
        dataCancelamento = (Calendar) map.get("dataCancelamento");
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

    public Integer getNumeroMesaComanda() {
        return numeroMesaComanda;
    }

    public void setNumeroMesaComanda(int numeroMesaComanda) {
        this.numeroMesaComanda = numeroMesaComanda;
    }

    public Calendar getDataRegistro() {
        return dataRegistro;
    }

    public void setDataRegistro(Calendar dataRegistro) {
        this.dataRegistro = dataRegistro;
    }

    public List<TabelaPagamento> getTabelaPagamentos() {
        return tabelaPagamentos;
    }

    public void setTabelaPagamentos(List<TabelaPagamento> tabelaPagamentos) {
        this.tabelaPagamentos = tabelaPagamentos;
    }

    public Calendar getDataFechamento() {
        return dataFechamento;
    }

    public void setDataFechamento(Calendar dataFechamento) {
        this.dataFechamento = dataFechamento;
    }

    public Calendar getDataCancelamento() {
        return dataCancelamento;
    }

    public void setDataCancelamento(Calendar dataCancelamento) {
        this.dataCancelamento = dataCancelamento;
    }

    public String getMotivoCancelamento() {
        return motivoCancelamento;
    }

    public void setMotivoCancelamento(String motivoCancelamento) {
        this.motivoCancelamento = motivoCancelamento;
    }

    public void removePagamento(TabelaPagamento tabelaPagamento){
        getTabelaPagamentos().remove(tabelaPagamento);
        VariaveisControleG.valorRestante = VariaveisControleG.valorRestante + tabelaPagamento.getValor();
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
