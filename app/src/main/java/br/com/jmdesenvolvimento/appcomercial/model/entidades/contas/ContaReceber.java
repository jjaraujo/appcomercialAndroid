package br.com.jmdesenvolvimento.appcomercial.model.entidades.contas;

import java.util.Calendar;
import java.util.HashMap;

import br.com.jmdesenvolvimento.appcomercial.controller.funcoesGerais.FuncoesGerais;
import br.com.jmdesenvolvimento.appcomercial.model.entidades.Entidade;
import br.com.jmdesenvolvimento.appcomercial.model.entidades.cadastral.pessoas.Cliente;
import br.com.jmdesenvolvimento.appcomercial.model.entidades.vendas.TipoPagamento;
import br.com.jmdesenvolvimento.appcomercial.model.entidades.vendas.Venda;
import br.com.jmdesenvolvimento.appcomercial.model.tabelasIntermediarias.TabelaParcelasPagamento;

public class ContaReceber extends Entidade {

    private TipoPagamento tipoPagamento;
    private TabelaParcelasPagamento tabelaParcelasPagamento;
    private Cliente cliente;
    private Calendar dataVencimento;

    @Override
    public void setMapAtributos(HashMap<String, Object> map) {
        id = (int) map.get(getIdNome());
        tabelaParcelasPagamento = (TabelaParcelasPagamento) map.get("tabelaParcelasPagamento");//+FuncoesGerais.prefixoChaveEstrangeira());
        tipoPagamento = (TipoPagamento) map.get("tipoPagamento");//+ FuncoesGerais.prefixoChaveEstrangeira());
        cliente = (Cliente) map.get("cliente");//+ FuncoesGerais.prefixoChaveEstrangeira());
        dataVencimento = (Calendar) map.get("dataVencimento");//+ FuncoesGerais.prefixoChaveEstrangeira());
    }

    public TipoPagamento getTipoPagamento() {
        return tipoPagamento;
    }

    public void setTipoPagamento(TipoPagamento tipoPagamento) {
        this.tipoPagamento = tipoPagamento;
    }

    public TabelaParcelasPagamento getParcelasPagamento() {
        return tabelaParcelasPagamento;
    }

    public void setParcelasPagamento(TabelaParcelasPagamento parcelasPagamento) {
        this.tabelaParcelasPagamento = parcelasPagamento;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente venda) {
        this.cliente = venda;
    }

    public TabelaParcelasPagamento getTabelaParcelasPagamento() {
        return tabelaParcelasPagamento;
    }

    public void setTabelaParcelasPagamento(TabelaParcelasPagamento tabelaParcelasPagamento) {
        this.tabelaParcelasPagamento = tabelaParcelasPagamento;
    }

    public Calendar getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(Calendar dataVencimento) {
        this.dataVencimento = dataVencimento;
    }
}
