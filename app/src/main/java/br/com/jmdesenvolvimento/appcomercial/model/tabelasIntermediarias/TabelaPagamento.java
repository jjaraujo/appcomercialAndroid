package br.com.jmdesenvolvimento.appcomercial.model.tabelasIntermediarias;

import java.util.HashMap;
import java.util.List;

import br.com.jmdesenvolvimento.appcomercial.controller.funcoesGerais.FuncoesGerais;
import br.com.jmdesenvolvimento.appcomercial.model.entidades.vendas.TipoPagamento;

public class TabelaPagamento extends TabelaIntermediaria {
    private int venda;
    private TipoPagamento tipoPagamento;
    private double valor;
    private List<TabelaParcelasPagamento> tabelaParcelasPagamento;

    public TabelaPagamento(int venda){
        this.venda = venda;
    }
    public TabelaPagamento(){
    }

    @Override
    public void setMapAtributos(HashMap<String, Object> map) {
        id = (int) map.get(getIdNome());
        tipoPagamento = (TipoPagamento) map.get("tipoPagamentos");//+ FuncoesGerais.prefixoChaveEstrangeira());
        venda = (int) map.get("venda");
        valor = (double) map.get("valor");
        tabelaParcelasPagamento = (List<TabelaParcelasPagamento>) map.get("tabelaParcelasPagamento");
    }

    public Integer getVenda() {
        return venda;
    }

    public void setVenda(int venda) {
        this.venda = venda;
    }

    public TipoPagamento getTipoPagamentos() {
        return tipoPagamento;
    }

    public void setTipoPagamentos(TipoPagamento tipoPagamentos) {
        this.tipoPagamento = tipoPagamentos;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public List<TabelaParcelasPagamento> getTabelaParcelasPagamento() {
        return tabelaParcelasPagamento;
    }

    public void seTabelaParcelasPagamento(List<TabelaParcelasPagamento>  tabelaParcelasPagamento) {
        this.tabelaParcelasPagamento = tabelaParcelasPagamento;
    }
}
