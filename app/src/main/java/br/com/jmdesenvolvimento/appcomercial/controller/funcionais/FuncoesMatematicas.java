package br.com.jmdesenvolvimento.appcomercial.controller.funcionais;

import java.util.List;

import br.com.jmdesenvolvimento.appcomercial.model.entidades.vendas.Venda;
import br.com.jmdesenvolvimento.appcomercial.model.tabelas.TabelaProdutosVenda;

public class FuncoesMatematicas {

    public static double calculaValorTotalProdutoVenda(TabelaProdutosVenda tpv) {
        double valorTotal = 0;
        double valorProduto = tpv.getProduto().getPreco();
        double qtd = tpv.getQtd();
        valorTotal += valorProduto * qtd;
        return valorTotal;
    }

    public static double calculaValorTotalVenda(Venda venda) {
        if(venda == null){
            return  0;
        } // corrigir erro de nullpointerException ao inciar app
        List<TabelaProdutosVenda> list = venda.getTabelaProdutosVenda();
        double valorTotal = 0;
        if (list != null) {
            for (TabelaProdutosVenda tpv : list) {
                double valorProduto = tpv.getProduto().getPreco();
                double qtd = tpv.getQtd();
                valorTotal += valorProduto * qtd;
            }
        }
        return valorTotal;
    }

}
