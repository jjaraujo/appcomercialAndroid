package br.com.jmdesenvolvimento.appcomercial.model.tabelasIntermediarias;

import java.util.Calendar;
import java.util.HashMap;

import br.com.jmdesenvolvimento.appcomercial.controller.funcoesGerais.FuncoesGerais;
import br.com.jmdesenvolvimento.appcomercial.model.entidades.estoque.Produto;

public class TabelaProdutosVenda extends TabelaIntermediaria {

    private int venda;
    private Produto produto;
    private int qtd;
    private Calendar dataCadastro;
    private Calendar dataCancelamento;
    private String motivoCancelamento;

    @Override
    public void setMapAtributos(HashMap<String, Object> map) {
        id = (int) map.get(getIdNome());
        venda = (int) map.get("venda");
        qtd = (int) map.get("qtd");
        produto = (Produto) map.get("produto");//+ FuncoesGerais.prefixoChaveEstrangeira());
        dataCadastro = (Calendar) map.get("dataCadastro");
        dataCancelamento = (Calendar) map.get("dataCancelamento");
        motivoCancelamento = (String) map.get("motivoCancelamento");
    }

    public Integer getVenda() {
        return venda;
    }

    public void setVenda(int venda) {
        this.venda = venda;
    }

    public Integer getQtd() {
        return qtd;
    }

    public void setQtd(int qtd) {
        this.qtd = qtd;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public Calendar getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(Calendar dataCadastro) {
        this.dataCadastro = dataCadastro;
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

    @Override
    public String toString() {
        return produto.getId()+ " " + produto.getNome_produto() ;
    }
}
