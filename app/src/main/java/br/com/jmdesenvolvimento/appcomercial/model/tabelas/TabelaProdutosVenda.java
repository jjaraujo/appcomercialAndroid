package br.com.jmdesenvolvimento.appcomercial.model.tabelas;

import java.util.HashMap;
import java.util.List;

import br.com.jmdesenvolvimento.appcomercial.controller.funcionais.Funcoes;
import br.com.jmdesenvolvimento.appcomercial.model.entidades.estoque.Produto;

public class TabelaProdutosVenda extends TabelaIntermediaria {

    private int venda_id;
    private Produto produto;
    private int qtd;
    private String dataCadastro;
    private String dataCancelamento;
    private String motivoCancelamento;

    @Override
    public void setMapAtributos(HashMap<String, Object> map) {
        id = (int) map.get(getIdNome());
        venda_id = (int) map.get("venda_id");
        qtd = (int) map.get("qtd");
        produto = (Produto) map.get("produto"+ Funcoes.prefixoChaveEstrangeira());
        dataCadastro = (String) map.get("dataCadastro");
        dataCancelamento = (String) map.get("dataCancelamento");
        motivoCancelamento = (String) map.get("dataCadastro");
    }

    public int getVenda_id() {
        return venda_id;
    }

    public void setVenda_id(int venda_id) {
        this.venda_id = venda_id;
    }

    public int getQtd() {
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

    public String getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(String dataCadastro) {
        this.dataCadastro = dataCadastro;
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

    @Override
    public String toString() {
        return produto.getId()+ " " + produto.getNome_produto() ;
    }
}
