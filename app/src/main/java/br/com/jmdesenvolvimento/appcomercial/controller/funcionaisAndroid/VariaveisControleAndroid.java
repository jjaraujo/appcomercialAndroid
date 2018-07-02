package br.com.jmdesenvolvimento.appcomercial.controller.funcionaisAndroid;

import android.support.design.widget.FloatingActionButton;
import android.widget.EditText;

import br.com.jmdesenvolvimento.appcomercial.view.activitys.ProdutosVendaActivity;
import br.com.jmdesenvolvimento.appcomercial.view.activitys.entidades.PagamentoActivity;
import br.com.jmdesenvolvimento.appcomercial.view.activitys.entidades.contas.ContaReceberActivity;
import br.com.jmdesenvolvimento.appcomercial.view.dialogFragment.DialogEscolherEntidade;
import br.com.jmdesenvolvimento.appcomercial.view.dialogFragment.DialogQuantidadeProduto;

public final class VariaveisControleAndroid {

 //   public static TextView textViewVendaSelectionada;
  //  public static Button buttonValorTotal;
    public static String codigoDeBarrasLido;
    public static FloatingActionButton buttonAddPessoaForSnackbar;
    public static EditText editTextCodigoBarrasCadastroProduto;

    /**Armazena a quantidade selecionada pelo usuário no dialog de quantidade do produto*/
    public static int qtdSelecionadaProdutoVenda;

    public static DialogEscolherEntidade dialogEscolherEntidade;
    public static DialogQuantidadeProduto dialogQuantidadeProduto;

    public static PagamentoActivity activityPagamento;
    public static ContaReceberActivity contaReceberActivity;
    public static ProdutosVendaActivity produtosVendaActivity;

    // public static FragmentVendasAbertas fragmentVendasAbertas;
   // public static FragmentProdutosVendas fragmentProdutos;

}
