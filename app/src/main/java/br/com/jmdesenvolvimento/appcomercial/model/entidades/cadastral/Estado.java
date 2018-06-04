package br.com.jmdesenvolvimento.appcomercial.model.entidades.cadastral;


import java.io.Serializable;
import java.util.HashMap;

import br.com.jmdesenvolvimento.appcomercial.model.entidades.Entidade;

public class Estado extends Entidade implements Serializable {

	private String nome_estado;

	public String getNome_estado() {
		return nome_estado;
	}
	public void setNome_estado(String nome_estado) {
		this.nome_estado = nome_estado;
	}

	@Override
	public void setMapAtributos(HashMap<String, Object> map) {
		id = (Integer) map.get(getIdNome());
		nome_estado = (String) map.get("nome_estado");
	}

	@Override
	public String toString() {
		return id + " "+ nome_estado;
	}
}
