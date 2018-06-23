package br.com.jmdesenvolvimento.appcomercial.model.tabelasIntermediarias;

import java.util.HashMap;
import java.util.List;

import br.com.jmdesenvolvimento.appcomercial.model.Tabela;

public abstract class TabelaIntermediaria extends Tabela {

    public abstract  void setMapAtributos(HashMap<String, Object> map);

    @Override
    public List<Tabela> getListValoresIniciais() {
        return null;
    }
}
