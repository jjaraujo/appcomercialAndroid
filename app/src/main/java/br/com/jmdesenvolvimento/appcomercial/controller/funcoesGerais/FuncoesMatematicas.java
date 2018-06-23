package br.com.jmdesenvolvimento.appcomercial.controller.funcoesGerais;

import java.lang.reflect.Modifier;
import java.text.DecimalFormat;
import java.util.List;

import br.com.jmdesenvolvimento.appcomercial.model.entidades.vendas.Venda;
import br.com.jmdesenvolvimento.appcomercial.model.tabelasIntermediarias.TabelaProdutosVenda;

public class FuncoesMatematicas {

    public static String calculaValorTotalProdutoVenda(TabelaProdutosVenda tpv) {
        double valorTotal = 0;
        double valorProduto = tpv.getProduto().getPreco();
        double qtd = tpv.getQtd();
        valorTotal += valorProduto * qtd;
        return formataValoresDouble(valorTotal);
    }

    public static String calculaValorTotalVenda(Venda venda) {

        if(venda == null){
            return  formataValoresDouble(0);
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
        return formataValoresDouble(valorTotal);
    }

    public static double calculaValorTotalVendaDouble(Venda venda) {

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

    public static String calculaValorTotalVendaComDesconto(Venda venda) {

        double valorTotal = Double.parseDouble(calculaValorTotalVenda(venda).replace(",","."));
        valorTotal = valorTotal - venda.getDesconto();
        return formataValoresDouble(valorTotal);
    }

    public static String formataValoresDouble(double valor){
        DecimalFormat df = new DecimalFormat("#.##");
        return df.format(valor).replace(".",",");
    }
}
