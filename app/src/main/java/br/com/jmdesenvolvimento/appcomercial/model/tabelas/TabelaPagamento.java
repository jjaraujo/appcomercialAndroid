package br.com.jmdesenvolvimento.appcomercial.model.tabelas;

import java.util.HashMap;

import br.com.jmdesenvolvimento.appcomercial.model.Tabela;
import br.com.jmdesenvolvimento.appcomercial.model.entidades.vendas.TipoPagamentos;

public class TabelaPagamento extends Tabela {
    private int venda_id;
    private TipoPagamentos tipoPagamentos;
    private double valor;
    private double parcelas;

    @Override
    public void setMapAtributos(HashMap<String, Object> map) {
        id = (int) map.get(getIdNome());
        tipoPagamentos = (TipoPagamentos) map.get("tipoPagamentos");
        valor = (double) map.get("valor");
        parcelas = (double) map.get("parcelas");
    }

    public int getVenda_id() {
        return venda_id;
    }

    public void setVenda_id(int venda_id) {
        this.venda_id = venda_id;
    }

    public TipoPagamentos getTipoPagamentos() {
        return tipoPagamentos;
    }

    public void setTipoPagamentos(TipoPagamentos tipoPagamentos) {
        this.tipoPagamentos = tipoPagamentos;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public double getParcelas() {
        return parcelas;
    }

    public void setParcelas(double parcelas) {
        this.parcelas = parcelas;
    }
}
