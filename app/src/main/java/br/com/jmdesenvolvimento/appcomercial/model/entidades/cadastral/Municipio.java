package br.com.jmdesenvolvimento.appcomercial.model.entidades.cadastral;


import java.io.Serializable;
import java.util.HashMap;

import br.com.jmdesenvolvimento.appcomercial.controller.funcoesGerais.FuncoesGerais;
import br.com.jmdesenvolvimento.appcomercial.model.entidades.Entidade;

public class Municipio extends Entidade implements Serializable {

	private String nome_municipio;
	private Estado estado;

	public String getNome() {
		return nome_municipio;
	}
	public void setNome(String nome) {
		this.nome_municipio = nome;
	}
	public Estado getEstado() {
		return estado;
	}
	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	@Override
	public void setMapAtributos(HashMap<String, Object> map) {
		id = (Integer) map.get(getIdNome());
		nome_municipio = (String) map.get("nome_municipio");
		estado = (Estado) map.get("estado");//+ FuncoesGerais.prefixoChaveEstrangeira());
	}
}
