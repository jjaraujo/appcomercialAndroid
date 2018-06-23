package br.com.jmdesenvolvimento.appcomercial.controller.funcoesGerais;

import android.support.design.widget.FloatingActionButton;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import br.com.jmdesenvolvimento.appcomercial.model.entidades.vendas.Venda;
import br.com.jmdesenvolvimento.appcomercial.model.tabelasIntermediarias.Configuracoes;
import br.com.jmdesenvolvimento.appcomercial.view.activitys.entidades.PagamentoActivity;
import br.com.jmdesenvolvimento.appcomercial.view.dialogFragment.DialogEscolherEntidade;
import br.com.jmdesenvolvimento.appcomercial.view.dialogFragment.DialogQuantidadeProduto;
import br.com.jmdesenvolvimento.appcomercial.view.fragments.FragmentProdutosVendas;
import br.com.jmdesenvolvimento.appcomercial.view.fragments.FragmentVendasAbertas;

public final class VariaveisControleG {

    /**Armazena a venda selecionada pelo usuário*/
    public static Venda vendaSelecionada;
    public static Configuracoes configuracoesSimples;
    public static double valorRestante;
}
