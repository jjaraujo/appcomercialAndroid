package br.com.jmdesenvolvimento.appcomercial.model.entidades.cadastral.pessoas;

import java.io.Serializable;
import java.util.HashMap;

import br.com.jmdesenvolvimento.appcomercial.controller.funcionais.Funcoes;
import br.com.jmdesenvolvimento.appcomercial.model.entidades.Entidade;
import br.com.jmdesenvolvimento.appcomercial.model.entidades.cadastral.Estado;
import br.com.jmdesenvolvimento.appcomercial.model.entidades.cadastral.Municipio;

public class Pessoa extends Entidade implements Serializable,IPessoa {

  //  public abstract void setNome_pessoa();

    private String nome_pessoa;
    private int sexo;
    private String logradouro;
    private String bairro;
    private int cep;
    private int numero;
    private Municipio municipio;
    private Estado estado;
    private int rgIE;
    private String cpfCNPJ;
    private String nascimento;
    private String telefone1;
    private String telefone2;
    private String email;

    public String getNome() {
        return nome_pessoa;
    }

    public void setNome(String nome) {
        this.nome_pessoa = nome;
    }

    public int getSexo() {
        return sexo;
    }

    public void setSexo(int sexo) {
        this.sexo = sexo;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public Integer getCep() {
        return cep;
    }

    public void setCep(Integer cep) {
        this.cep = cep;
    }

    public Municipio getMunicipio() {
        return municipio;
    }

    public void setMunicipio(Municipio municipio) {
        this.municipio = municipio;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public Integer getRgIE() {
        return rgIE;
    }

    public void setRgIE(Integer rgIE) {
        this.rgIE = rgIE;
    }

    public String getCpfCNPJ() {
        return cpfCNPJ;
    }

    public void setCpfCNPJ(String cpfCNPJ) {
        this.cpfCNPJ = cpfCNPJ;
    }

    public String getNascimento() {
        return nascimento;
    }

    public void setNascimento(String nascimento) {
        this.nascimento = nascimento;
    }

    public String getTelefone1() {
        return telefone1;
    }

    public void setTelefone1(String telefone1) {
        this.telefone1 = telefone1;
    }

    public String getTelefone2() {
        return telefone2;
    }

    public void setTelefone2(String telefone2) {
        this.telefone2 = telefone2;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return this.id + " - " + this.nome_pessoa;
        //+ " - " + this.email;
    }


    @Override
    public void setMapAtributos(HashMap<String, Object> map) {

        id = (int) map.get(getIdNome());
        nome_pessoa = (String) map.get("nome_pessoa");
        sexo = (int) map.get("sexo");
        logradouro = (String) map.get("logradouro");
        bairro = (String) map.get("bairro");
        cep = (int) map.get("cep");
        numero = (int) map.get("numero");
        municipio = (Municipio) map.get("municipio"+Funcoes.prefixoChaveEstrangeira());
        estado = (Estado) map.get("estado"+ Funcoes.prefixoChaveEstrangeira());
        rgIE = (int) map.get("rgIE");
        cpfCNPJ = (String) map.get("cpfCNPJ");
        nascimento = (String) map.get("nascimento");
        telefone1 = (String) map.get("telefone1");
        telefone2 = (String) map.get("telefone2");
        email = (String) map.get("email");
    }

    @Override
    public Pessoa getPessoa() {
        return this;
    }

    @Override
    public void setPessoa(Pessoa pessoa) {

    }
}


