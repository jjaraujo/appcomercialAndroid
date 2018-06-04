package br.com.jmdesenvolvimento.appcomercial.model.entidades.cadastral.pessoas;

import java.io.Serializable;
import java.util.HashMap;

import br.com.jmdesenvolvimento.appcomercial.controller.funcionais.Funcoes;
import br.com.jmdesenvolvimento.appcomercial.model.entidades.Entidade;

public class Fornecedor extends Entidade implements Serializable {

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    private Pessoa pessoa;

    @Override
    public void setMapAtributos(HashMap<String, Object> map) {
        id = (int) map.get(getIdNome());
        pessoa = (Pessoa) map.get("pessoa"+ Funcoes.prefixoChaveEstrangeira());
    }
}
