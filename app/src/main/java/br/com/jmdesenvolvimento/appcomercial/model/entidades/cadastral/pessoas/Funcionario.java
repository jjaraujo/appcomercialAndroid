package br.com.jmdesenvolvimento.appcomercial.model.entidades.cadastral.pessoas;

import java.util.HashMap;

import br.com.jmdesenvolvimento.appcomercial.controller.funcionais.Funcoes;
import br.com.jmdesenvolvimento.appcomercial.model.entidades.Entidade;
import br.com.jmdesenvolvimento.appcomercial.model.entidades.cadastral.pessoas.Pessoa;

public class Funcionario extends Pessoa{

    private Pessoa pessoa;

    @Override
    public void setMapAtributos(HashMap<String, Object> map) {
        pessoa = (Pessoa) map.get(pessoa.getIdNome());
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }
}
