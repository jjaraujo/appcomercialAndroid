package br.com.jmdesenvolvimento.appcomercial.controller.funcoesGerais;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import br.com.jmdesenvolvimento.appcomercial.controller.funcionaisAndroid.VariaveisControleAndroid;
import br.com.jmdesenvolvimento.appcomercial.model.dao.IConnection;
import br.com.jmdesenvolvimento.appcomercial.model.entidades.contas.ContaReceber;
import br.com.jmdesenvolvimento.appcomercial.model.entidades.vendas.Venda;
import br.com.jmdesenvolvimento.appcomercial.model.tabelasIntermediarias.TabelaPagamento;
import br.com.jmdesenvolvimento.appcomercial.model.tabelasIntermediarias.TabelaParcelasPagamento;

public final class FuncoesVendasG {

    public static void finalizaVenda(IConnection dao){
        Venda vendaSelecionada = VariaveisControleG.vendaSelecionada;
        for(TabelaPagamento t : vendaSelecionada.getTabelaPagamentos()){
            t.setId(dao.countIdEntidade(t)+1); // ID adicionado aqui para que possa ser inserido da TabelaParcelasPagamento
            for(TabelaParcelasPagamento parcelaPagamento : t.getTabelaParcelasPagamento()){
                parcelaPagamento.setTabelaPagamento(t.getId());
                parcelaPagamento.setTotalParcelas(t.getTabelaParcelasPagamento().size());
                dao.insert(parcelaPagamento);

                if(t.getTipoPagamentos().isAceitaParcela()) {
                    String dataPagamento = FuncoesGerais.calendarToString(Calendar.getInstance(), FuncoesGerais.ddMMyyyy, false);
                    Calendar dataVencimento = parcelaPagamento.getData();
                    String dataVencimentoS =  FuncoesGerais.calendarToString(dataVencimento,FuncoesGerais.ddMMyyyy,false);
                    if ( !dataPagamento.equals(dataVencimentoS)) {
                        FuncoesContas.criaContaReceber(dao, vendaSelecionada, t, parcelaPagamento);
                    }
                }
            }
            dao.insert(t);
        }
        vendaSelecionada.setDataFechamento(Calendar.getInstance());
        dao.update(vendaSelecionada,true);
    }

    /**Método para criar vendas por cliente ou comanda/mesa*/
    public static void criaVenda(IConnection dao, Venda venda){
        VariaveisControleG.vendaSelecionada = venda;
        venda.setTabelaPagamentos(new ArrayList<TabelaPagamento>());
        venda.setDataRegistro(Calendar.getInstance());
        venda.setMapAtributos(venda.getMapAtributos(true));
        dao.insert(venda);
        dao.close();
        VariaveisControleAndroid.fragmentVendasAbertas.carregaLista();
        VariaveisControleAndroid.fragmentProdutos.carregaLista();
    }
}
