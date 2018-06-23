package br.com.jmdesenvolvimento.appcomercial.controller.funcoesGerais;

import java.util.Calendar;

import br.com.jmdesenvolvimento.appcomercial.model.dao.IConnection;
import br.com.jmdesenvolvimento.appcomercial.model.entidades.contas.ContaReceber;
import br.com.jmdesenvolvimento.appcomercial.model.entidades.vendas.Venda;
import br.com.jmdesenvolvimento.appcomercial.model.tabelasIntermediarias.TabelaPagamento;
import br.com.jmdesenvolvimento.appcomercial.model.tabelasIntermediarias.TabelaParcelasPagamento;

public class FuncoesContas {

    public static void criaContaReceber(IConnection dao, Venda venda, TabelaPagamento tabelaPagamento, TabelaParcelasPagamento parcela){
        ContaReceber contaReceber = new ContaReceber();
        contaReceber.setTipoPagamento(tabelaPagamento.getTipoPagamentos());
        contaReceber.setParcelasPagamento(parcela);
        contaReceber.setCliente(venda.getCliente());
        contaReceber.setDataVencimento(parcela.getData());
        dao.insert(contaReceber);
        dao.close();
    }

}
