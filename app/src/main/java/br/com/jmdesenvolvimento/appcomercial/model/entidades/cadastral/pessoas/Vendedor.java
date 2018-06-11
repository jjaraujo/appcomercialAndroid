package br.com.jmdesenvolvimento.appcomercial.model.entidades.cadastral.pessoas;


import java.util.HashMap;

import br.com.jmdesenvolvimento.appcomercial.model.entidades.Entidade;

public class Vendedor extends Pessoa implements IPessoa {

    private Pessoa pessoa;
    private double comissao;

    public void setPessoa(Pessoa pessoa){
        this.pessoa = pessoa;
    }

    public Pessoa getPessoa(){
        return this.pessoa;
    }


    public void setComissao(Double comissao){
        this.comissao = comissao;
    }

    public Double getComissao(){
        return this.comissao;
    }

    @Override
    public void setMapAtributos(HashMap<String, Object> map) {
        id = (int) map.get("id_vendedor");
         pessoa = (Pessoa) map.get("pessoa");
         comissao = (double) map.get("comissao");
    }


}
