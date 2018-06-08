package br.com.jmdesenvolvimento.appcomercial.controller.funcionais;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import br.com.jmdesenvolvimento.appcomercial.model.entidades.cadastral.pessoas.Cliente;
import br.com.jmdesenvolvimento.appcomercial.model.entidades.vendas.Venda;
import br.com.jmdesenvolvimento.appcomercial.model.tabelas.configuracoes.Configuracoes;
import br.com.jmdesenvolvimento.appcomercial.view.dialogFragment.DialogEscolherEntidade;
import br.com.jmdesenvolvimento.appcomercial.view.dialogFragment.DialogQuantidadeProduto;
import br.com.jmdesenvolvimento.appcomercial.view.fragments.FragmentProdutos;
import br.com.jmdesenvolvimento.appcomercial.view.fragments.FragmentVendasAbertas;

public abstract class VariaveisControle {
    public static Venda VENDA_SELECIONADA;
    public static TextView vendaSelectionada;
    public static Button valorTotal;
    public static FragmentVendasAbertas fragmentVendasAbertas;
    public static FragmentProdutos fragmentProdutos;
    public static boolean produtoEdicao;
    public static DialogEscolherEntidade dialogEscolherEntidade;
    public static DialogQuantidadeProduto dialogQuantidadeProduto;
    public static int qtdSelecionadaProdutoVenda;
    public static Configuracoes CONFIGURACOES_SIMPLES;


}
