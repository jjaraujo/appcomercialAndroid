package br.com.jmdesenvolvimento.appcomercial.view.adapters.arraysAdapter.tabelas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import br.com.jmdesenvolvimento.appcomercial.R;;
import app.jm.funcional.model.Tabela;
import app.jm.funcional.model.entidades.vendas.TipoPagamento;

public class ArrayAdapterTiposPagamentos extends BaseAdapter {
    private List<Tabela> list;
    private Context context;

    public ArrayAdapterTiposPagamentos(Context context, List<?> listTabela) {
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
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_model_tipos_pagamento,null);
        TextView textView = view.findViewById(R.id.textViewTipoPagamento);
        ImageView imageView = view.findViewById(R.id.imageViewIconeTipoPagamento);
        TipoPagamento tipo = (TipoPagamento) list.get(position);
        textView.setText(tipo.getNome());
        imageView.setImageAlpha(tipo.getIdIcone());
        return view;
    }

}