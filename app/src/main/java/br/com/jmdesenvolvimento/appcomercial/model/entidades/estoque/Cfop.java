package br.com.jmdesenvolvimento.appcomercial.model.entidades.estoque;

import java.io.Serializable;
import java.util.HashMap;

import br.com.jmdesenvolvimento.appcomercial.model.entidades.Entidade;

public class Cfop extends Entidade implements Serializable {
    private String nome_cfop;
    private Cst cst;
    private Csons csons;
    private TipoItem tipoItem;

    public String getNome_cfop() {
        return nome_cfop;
    }

    public void setNome_cfop(String nome_cfop) {
        this.nome_cfop = nome_cfop;
    }

    public Cst getCst() {
        return cst;
    }

    public void setCst(Cst cst) {
        this.cst = cst;
    }

    public Csons getCsons() {
        return csons;
    }

    public void setCsons(Csons csons) {
        this.csons = csons;
    }

    public TipoItem getTipoItem() {
        return tipoItem;
    }

    public void setTipoItem(TipoItem tipoItem) {
        this.tipoItem = tipoItem;
    }

    @Override
    public void setMapAtributos(HashMap<String, Object> map) {
        id = (int) map.get(getIdNome());
        nome_cfop = (String) map.get("nome_cfop");
        csons = (Csons) map.get(csons.getIdNome());
        tipoItem = (TipoItem) map.get(tipoItem.getIdNome());
    }

    @Override
    public String toString() {
        return id + " " + nome_cfop;
    }
}
