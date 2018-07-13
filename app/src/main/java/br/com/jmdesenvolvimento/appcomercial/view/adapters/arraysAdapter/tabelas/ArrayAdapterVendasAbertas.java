package br.com.jmdesenvolvimento.appcomercial.view.adapters.arraysAdapter.tabelas;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import br.com.jmdesenvolvimento.appcomercial.R;;
import app.jm.funcional.controller.funcoesGerais.FuncoesGerais;
import app.jm.funcional.controller.VariaveisControle;
import app.jm.funcional.model.Tabela;
import app.jm.funcional.model.entidades.vendas.Venda;

public class ArrayAdapterVendasAbertas extends BaseAdapter {
    private List<Tabela> list;
    private Context context;

    public ArrayAdapterVendasAbertas(Context context, List<?> listTabela) {
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

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.list_model_vendas_abertas, null);

        Venda v = (Venda) list.get(position);
        TextView textViewNumeroVenda = view.findViewById(R.id.textViewNumeroVenda);
        TextView textViewClienteTipoVenda = view.findViewById(R.id.textViewClienteTipoVenda);

        if(v.getCliente() == null || v.getCliente().getId() == 0){
            textViewNumeroVenda.setText(FuncoesGerais.addZeros(v.getNumeroMesaComanda(),2));
            textViewClienteTipoVenda.setText(VariaveisControle.configuracoesSimples.getNomeTipoVenda());
        } else{
            textViewNumeroVenda.setText(v.getId()+"");
           String[] palavrasNome = v.getCliente().getPessoa().getNome().split(" ");
           String nome = palavrasNome[0] + " " + palavrasNome[palavrasNome.length -1];
            textViewClienteTipoVenda.setText(nome);
        }

        if(v.getDataRegistro() != null){
            Log.i("É usado","Sim");
            view.setBackgroundColor(Color.RED);
        }

        return view;
    }
}