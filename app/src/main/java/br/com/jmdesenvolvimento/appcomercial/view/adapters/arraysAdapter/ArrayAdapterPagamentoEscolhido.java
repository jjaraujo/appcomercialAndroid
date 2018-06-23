package br.com.jmdesenvolvimento.appcomercial.view.adapters.arraysAdapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Method;
import java.util.List;

import br.com.jmdesenvolvimento.appcomercial.R;
import br.com.jmdesenvolvimento.appcomercial.controller.funcionaisAndroid.FuncoesViewAndroid;
import br.com.jmdesenvolvimento.appcomercial.controller.funcoesGerais.FuncoesGerais;
import br.com.jmdesenvolvimento.appcomercial.controller.funcionaisAndroid.VariaveisControleAndroid;
import br.com.jmdesenvolvimento.appcomercial.controller.funcoesGerais.FuncoesMatematicas;
import br.com.jmdesenvolvimento.appcomercial.model.Tabela;
import br.com.jmdesenvolvimento.appcomercial.model.tabelasIntermediarias.TabelaPagamento;
import br.com.jmdesenvolvimento.appcomercial.model.tabelasIntermediarias.TabelaParcelasPagamento;

public class ArrayAdapterPagamentoEscolhido extends BaseAdapter {
    private List<Tabela> list;
    private Context context;

    public ArrayAdapterPagamentoEscolhido(Context context, List<?> listTabela) {
        this.list = (List<Tabela>) listTabela;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return list.get(position).getId();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = View.inflate(context,R.layout.list_model_pagamentos_escolhidos,null);
        TextView textViewTipoPagamento = view.findViewById(R.id.textViewPagamentoEscolhidoTipo);
        TextView textViewParcelas = view.findViewById(R.id.textViewPagamentoEscolhidoParcelasValor);
        TextView textViewValorTotal = view.findViewById(R.id.textViewValorTotalPagamentoEscolhido);
        ImageView imagemDelete = view.findViewById(R.id.imageViewPagamentoEscolhidoDeletar);
        ImageView imagemIconePagamento = view.findViewById(R.id.imageViewIconePagamentoEscolhido);
        final TabelaPagamento tabelaPagamento = (TabelaPagamento) list.get(position);

        if(tabelaPagamento.getTabelaParcelasPagamento() != null) {
            double valorParcelas = tabelaPagamento.getValor() / tabelaPagamento.getTabelaParcelasPagamento().size();
            textViewParcelas.setText(tabelaPagamento.getTabelaParcelasPagamento().size() + "x  R$" + FuncoesMatematicas.formataValoresDouble(valorParcelas));
            textViewTipoPagamento.setText(tabelaPagamento.getTipoPagamentos().getNome());
            textViewValorTotal.setText("R$"+FuncoesMatematicas.formataValoresDouble(tabelaPagamento.getValor()));
        }
        imagemDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               try{
                   Object[] o = {list.get(position)};
                    Method method = VariaveisControleAndroid.activityPagamento.getClass().getDeclaredMethod("excluiPagamentoDaVenda", Object.class);

                    FuncoesViewAndroid.addAlertDialogExcluir(context,tabelaPagamento,"Pagamento excluído",
                            "Excluir o pagamento " + tabelaPagamento.getTipoPagamentos().getNome() + "?",
                           method, VariaveisControleAndroid.activityPagamento,o);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
        });
        return view;
    }
}