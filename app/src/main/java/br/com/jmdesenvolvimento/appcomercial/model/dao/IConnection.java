package br.com.jmdesenvolvimento.appcomercial.model.dao;

import java.util.List;

import br.com.jmdesenvolvimento.appcomercial.model.Tabela;

public interface IConnection {
    int countIdEntidade(Tabela t);
    void insert(Tabela t);
    void insert(List<?> t);
    void update(Tabela t, boolean b);
    void close();
    List<?> selectAll(Tabela tabela,String where, boolean pegaExcluidos);
    List<?>  selectAll(Tabela tabela, String where, boolean pegaExcluidos,String[] selectionArgs,String groupBy,String orderBy,String limit);
    void deleteLogico(Tabela tabela);
    Tabela select(Tabela tabela, String id, String where,String groupBy,String orderBy, String limit);
}
