package br.com.jmdesenvolvimento.appcomercial.model.entidades.cadastral.pessoas;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.HashMap;

import br.com.jmdesenvolvimento.appcomercial.controller.funcoesGerais.FuncoesGerais;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Cliente extends Pessoa implements IPessoa{

    private Pessoa pessoa;
    private double limite;
    private String ultimaVenda;


    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public double getLimite() {
        return limite;
    }

    public void setLimite(double limite) {
        this.limite = limite;
    }

    public String getUltimaVenda() {
        return ultimaVenda;
    }

    public void setUltimaVenda(String ultimaVenda) {
        this.ultimaVenda = ultimaVenda;
    }

    @Override
    public String toString() {
        if(pessoa == null){
            return super.toString() +" Pessoa nula";
        }
        return getPessoa().getNome() + " CPF: " + pessoa.getCpfCNPJ();
    }

    @Override
    public void setMapAtributos(HashMap<String, Object> map) {
        id = (int) map.get(getIdNome());
        pessoa = (Pessoa) map.get("pessoa");//+ FuncoesGerais.prefixoChaveEstrangeira());
        limite = (double) map.get("limite");
        ultimaVenda = (String) map.get("ultimaCompra");
    }
}
