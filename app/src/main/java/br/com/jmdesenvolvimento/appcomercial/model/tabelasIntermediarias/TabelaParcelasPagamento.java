package br.com.jmdesenvolvimento.appcomercial.model.tabelasIntermediarias;

import java.util.Calendar;
import java.util.HashMap;

public class TabelaParcelasPagamento extends TabelaIntermediaria {

    private int numeroParcela;
    private double valor;
    private Calendar data;
    private int tabelaPagamento;
    private int totalParcelas;

    @Override
    public void setMapAtributos(HashMap<String, Object> map) {
        id = (int) map.get(getIdNome());
        numeroParcela = (int) map.get("numeroParcela");
        valor = (double) map.get("valor");
        data = (Calendar) map.get("data");
        tabelaPagamento = (int) map.get("tabelaPagamento");//+ FuncoesGerais.prefixoChaveEstrangeira());
        totalParcelas = (int) map.get("totalParcelas");
    }

    public Integer getNumeroParcela() {
        return numeroParcela;
    }

    public void setNumeroParcela(int numeroParcela) {
        this.numeroParcela = numeroParcela;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public Calendar getData() {
        return data;
    }

    public void setData(Calendar data) {
        this.data = data;
    }

    public int getTabelaPagamento() {
        return tabelaPagamento;
    }

    public void setTabelaPagamento(int tabelaPagamento) {
        this.tabelaPagamento = tabelaPagamento;
    }

    public int getTotalParcelas() {
        return totalParcelas;
    }

    public void setTotalParcelas(int totalParcelas) {
        this.totalParcelas = totalParcelas;
    }

}
