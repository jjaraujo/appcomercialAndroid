package br.com.jmdesenvolvimento.appcomercial.model.entidades.estoque;


import java.io.Serializable;
import java.util.Calendar;
import java.util.HashMap;

import br.com.jmdesenvolvimento.appcomercial.controller.funcoesGerais.FuncoesGerais;
import br.com.jmdesenvolvimento.appcomercial.model.entidades.Entidade;
import br.com.jmdesenvolvimento.appcomercial.model.entidades.cadastral.pessoas.Fornecedor;

public class Produto extends Entidade implements Serializable{

    private String nome_produto;
    private long codigoBarras;
    private String descricao;
    private Unidade unidade;
    private double preco;
    private double custoCompra;
    private String ultimaCompra;
    private int qtd;
    private int qtdMinima;
    private Grupo grupo;
    private Ncm ncm;
    private double aliquota;
    private double comissao;
    private double lucroBruto;
    private Calendar dataCompra;
    private Fornecedor fornecedor;
    private double impostoIbpt;
    private Csons csons;
    private String cit;
    private String cest;
    private TipoItem tipoItem;
    private Csons csonsNfce;
    private Cfop cfop;


    @Override
    public void setMapAtributos(HashMap<String, Object> map) {
        id = (int) map.get(getIdNome());
        nome_produto = (String) map.get("nome_produto");
        codigoBarras = (long) map.get("codigoBarras");
        descricao = (String) map.get("descricao");
        unidade = (Unidade) map.get("unidade");//+ FuncoesGerais.prefixoChaveEstrangeira());
        preco = (double) map.get("preco");
        custoCompra = (double) map.get("custoCompra");
        ultimaCompra = (String) map.get("ultimaCompra");
        qtd = (int) map.get("qtd");
        qtdMinima = (int) map.get("qtdMinima");
        grupo = (Grupo) map.get("grupo");//+ FuncoesGerais.prefixoChaveEstrangeira());
        ncm = (Ncm) map.get("ncm");//+ FuncoesGerais.prefixoChaveEstrangeira());
        aliquota = (double) map.get("aliquota");
        comissao = (double) map.get("comissao");
        lucroBruto = (double) map.get("lucroBruto");
        dataCompra = (Calendar) map.get("dataCompra");
        fornecedor = (Fornecedor) map.get("fornecedor");//+ FuncoesGerais.prefixoChaveEstrangeira());
        impostoIbpt = (double) map.get("impostoIbpt");
        csons = (Csons) map.get("csons");//+ FuncoesGerais.prefixoChaveEstrangeira());
        cit = (String) map.get("cit");
        tipoItem = (TipoItem) map.get("tipoItem");//+ FuncoesGerais.prefixoChaveEstrangeira());
        csonsNfce = (Csons) map.get("csonsNfce");//+ FuncoesGerais.prefixoChaveEstrangeira());
        cfop = (Cfop) map.get("cfop");//+ FuncoesGerais.prefixoChaveEstrangeira());
    }

    @Override
    public String toString() {
        return id + " " + nome_produto;
    }

    public String getNome_produto() {
        return nome_produto;
    }

    public void setNome_produto(String nome_produto) {
        this.nome_produto = nome_produto;
    }

    public long getCodigoBarras() {
        return codigoBarras;
    }

    public void setCodigoBarras(long codigoBarras) {
        this.codigoBarras = codigoBarras;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Unidade getUnidade() {
        return unidade;
    }

    public void setUnidade(Unidade unidade) {
        this.unidade = unidade;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public double getCustoCompra() {
        return custoCompra;
    }

    public void setCustoCompra(double custoCompra) {
        this.custoCompra = custoCompra;
    }

    public String getUltimaCompra() {
        return ultimaCompra;
    }

    public void setUltimaCompra(String ultimaCompra) {
        this.ultimaCompra = ultimaCompra;
    }

    public Integer getQtd() {
        return qtd;
    }

    public void setQtd(int qtd) {
        this.qtd = qtd;
    }

    public Integer getQtdMinima() {
        return qtdMinima;
    }

    public void setQtdMinima(int qtdMinima) {
        this.qtdMinima = qtdMinima;
    }

    public Grupo getGrupo() {
        return grupo;
    }

    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }

    public Ncm getNcm() {
        return ncm;
    }

    public void setNcm(Ncm ncm) {
        this.ncm = ncm;
    }

    public double getAliquota() {
        return aliquota;
    }

    public void setAliquota(double aliquota) {
        this.aliquota = aliquota;
    }

    public double getComissao() {
        return comissao;
    }

    public void setComissao(double comissao) {
        this.comissao = comissao;
    }

    public double getLucroBruto() {
        return lucroBruto;
    }

    public void setLucroBruto(double lucroBruto) {
        this.lucroBruto = lucroBruto;
    }

    public String getCest() {
        return cest;
    }

    public void setCest(String cest) {
        this.cest = cest;
    }

    public Calendar getDataCompra() {
        return dataCompra;
    }

    public void setDataCompra(Calendar dataCompra) {
        this.dataCompra = dataCompra;
    }

    public Fornecedor getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }

    public double getImpostoIbpt() {
        return impostoIbpt;
    }

    public void setImpostoIbpt(double impostoIbpt) {
        this.impostoIbpt = impostoIbpt;
    }

    public Csons getCsons() {
        return csons;
    }

    public void setCsons(Csons csons) {
        this.csons = csons;
    }

    public String getCit() {
        return cit;
    }

    public void setCit(String cit) {
        this.cit = cit;
    }

    public TipoItem getTipoItem() {
        return tipoItem;
    }

    public void setTipoItem(TipoItem tipoItem) {
        this.tipoItem = tipoItem;
    }

    public Csons getCsonsNfce() {
        return csonsNfce;
    }

    public void setCsonsNfce(Csons csonsNfce) {
        this.csonsNfce = csonsNfce;
    }

    public Cfop getCfop() {
        return cfop;
    }

    public void setCfop(Cfop cfop) {
        this.cfop = cfop;
    }

}
