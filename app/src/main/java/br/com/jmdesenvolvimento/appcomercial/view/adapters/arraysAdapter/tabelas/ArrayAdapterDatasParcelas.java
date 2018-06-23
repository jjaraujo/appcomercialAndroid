package br.com.jmdesenvolvimento.appcomercial.view.adapters.arraysAdapter.tabelas;

import android.app.FragmentTransaction;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import br.com.jmdesenvolvimento.appcomercial.R;
import br.com.jmdesenvolvimento.appcomercial.controller.funcionaisAndroid.FuncoesViewAndroid;
import br.com.jmdesenvolvimento.appcomercial.controller.funcoesGerais.FuncoesGerais;
import br.com.jmdesenvolvimento.appcomercial.controller.funcoesGerais.FuncoesMatematicas;
import br.com.jmdesenvolvimento.appcomercial.model.Tabela;
import br.com.jmdesenvolvimento.appcomercial.model.tabelasIntermediarias.TabelaParcelasPagamento;
import br.com.jmdesenvolvimento.appcomercial.view.dialogFragment.DialogAlterarValorParcela;

public class ArrayAdapterDatasParcelas extends BaseAdapter {
    private List<Tabela> list;
    private Context context;

    public ArrayAdapterDatasParcelas(Context context, List<?> listTabela) {
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

        final TabelaParcelasPagamento parcelasPagamentos = (TabelaParcelasPagamento) list.get(position);
        View view = View.inflate(context, R.layout.list_model_data_parcelas,null);
        TextView textViewData = view.findViewById(R.id.textViewDataParcela);
        FuncoesViewAndroid.addCalendar((AppCompatActivity) context,textViewData);
        final TextView textViewValor = view.findViewById(R.id.textViewValorParcela);
        final TextView textViewNumeroParcela = view.findViewById(R.id.textViewNumeroParcela);
        textViewData.setText(FuncoesGerais.calendarToString(parcelasPagamentos.getData(), FuncoesGerais.ddMMyyyy, false));
        textViewNumeroParcela.setText(parcelasPagamentos.getNumeroParcela() + " - ");
        textViewValor.setText("R$ " + FuncoesMatematicas.formataValoresDouble(parcelasPagamentos.getValor()));

        textViewValor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogAlterarValorParcela dialogAlterarValorParcela = new DialogAlterarValorParcela(textViewValor, parcelasPagamentos);
                FragmentTransaction ft = ((AppCompatActivity) context).getFragmentManager().beginTransaction();
                dialogAlterarValorParcela.show(ft,"dialogAlterarValorParcela");
            }
        });

        return  view;
    }
}