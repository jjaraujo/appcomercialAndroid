package br.com.jmdesenvolvimento.appcomercial.controller.funcionais;

import android.support.design.widget.FloatingActionButton;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import br.com.jmdesenvolvimento.appcomercial.model.entidades.vendas.Venda;
import br.com.jmdesenvolvimento.appcomercial.model.tabelas.configuracoes.Configuracoes;
import br.com.jmdesenvolvimento.appcomercial.view.dialogFragment.DialogEscolherEntidade;
import br.com.jmdesenvolvimento.appcomercial.view.dialogFragment.DialogQuantidadeProduto;
import br.com.jmdesenvolvimento.appcomercial.view.fragments.FragmentProdutosVendas;
import br.com.jmdesenvolvimento.appcomercial.view.fragments.FragmentVendasAbertas;

public abstract class VariaveisControle {
    public static Venda VENDA_SELECIONADA;
    public static TextView textViewVendaSelectionada;
    public static Button buttonValorTotal;
    public static FragmentVendasAbertas fragmentVendasAbertas;
    public static FragmentProdutosVendas fragmentProdutos;
    public static DialogEscolherEntidade dialogEscolherEntidade;
    public static DialogQuantidadeProduto dialogQuantidadeProduto;
    public static int qtdSelecionadaProdutoVenda;
    public static Configuracoes configuracoesSimples;
    public static FloatingActionButton buttonAddPessoaForSnackbar;
    public static EditText editTextCodigoBarrasCadastroProduto;
    public static String codigoDeBarrasLido;


}
