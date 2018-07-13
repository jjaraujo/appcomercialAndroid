package br.com.jmdesenvolvimento.appcomercial.model.filtrosOrdenacao;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import app.jm.funcional.model.Tabela;
import app.jm.funcional.model.entidades.cadastral.pessoas.Cliente;

public class FiltroContasReceber extends Tabela {

    private Cliente cliente;
    private Calendar dataInicio;
    private Calendar dataFim;
    private double valorMinimo;
    private double valorMaximo;

    @Override
    public void setMapAtributos(HashMap<String, Object> map) {
        id = (int) map.get(getIdNome());
        cliente = (Cliente) map.get("cliente");
        dataInicio = (Calendar) map.get("dataInicio");
        dataFim = (Calendar) map.get("dataFim");
        valorMinimo = (double) map.get("valorMinimo");
        valorMaximo = (double) map.get("valorMaximo");

    }

    @Override
    public List<Tabela> getListValoresIniciais() {
        return null;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Calendar getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(Calendar dataInicio) {
        this.dataInicio = dataInicio;
    }

    public Calendar getDataFim() {
        return dataFim;
    }

    public void setDataFim(Calendar dataFim) {
        this.dataFim = dataFim;
    }

    public double getValorMinimo() {
        return valorMinimo;
    }

    public void setValorMinimo(double valorMinimo) {
        this.valorMinimo = valorMinimo;
    }

    public double getValorMaximo() {
        return valorMaximo;
    }

    public void setValorMaximo(double valorMaximo) {
        this.valorMaximo = valorMaximo;
    }
}
