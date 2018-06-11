package br.com.jmdesenvolvimento.appcomercial.controller.funcionais;

import java.text.DecimalFormat;
import java.util.List;

import br.com.jmdesenvolvimento.appcomercial.model.entidades.vendas.Venda;
import br.com.jmdesenvolvimento.appcomercial.model.tabelas.TabelaProdutosVenda;

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
    public static String formataValoresDouble(double valor){
        DecimalFormat df = new DecimalFormat("#.##");
        return df.format(valor).replace(".",",");
    }
}
