package br.com.jmdesenvolvimento.appcomercial.model.entidades.estoque;

import java.io.Serializable;
import java.util.HashMap;

import br.com.jmdesenvolvimento.appcomercial.model.entidades.Entidade;

public class Ncm extends Entidade implements Serializable {

    private String codigo;
    private String ipi;
    private String descricao;

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getIpi() {
        return ipi;
    }

    public void setIpi(String ipi) {
        this.ipi = ipi;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }


    @Override
    public void setMapAtributos(HashMap<String, Object> map) {
        id = (int) map.get(getIdNome());
        codigo = (String) map.get("codigo");
        ipi = (String) map.get("ipi");
        descricao = (String) map.get("descricao");
    }

}
