package br.com.jmdesenvolvimento.appcomercial.view.adapters.arraysAdapter.tabelas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import br.com.jmdesenvolvimento.appcomercial.R;
import com.jmdesenvolvimento.appcomercial.controller.funcoesGerais.FuncoesGerais;
import com.jmdesenvolvimento.appcomercial.controller.funcoesGerais.FuncoesMatematicas;
import com.jmdesenvolvimento.appcomercial.model.Tabela;
import com.jmdesenvolvimento.appcomercial.model.entidades.vendas.Venda;

public class ArrayAdapterHitoricoClientes extends BaseAdapter {
    private List<Tabela> list;
    private Context context;

    public ArrayAdapterHitoricoClientes(Context context, List<?> listTabela) {
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
        View view = layoutInflater.inflate(R.layout.list_model_historico_cliente, null);
        Venda venda = (Venda) list.get(position);
        TextView vendedor = view.findViewById(R.id.textViewHistoricoClienteVendedor);
        vendedor.setText("Vendedor: " + venda.getVendedor().getPessoa().getNome());
        TextView numeroVenda = view.findViewById(R.id.textViewHistoricoClienteNumero);
        numeroVenda.setText(FuncoesGerais.addZeros(venda.getId(),5));
        TextView dataVenda = view.findViewById(R.id.textViewListClientesCpf);
        dataVenda.setText(FuncoesGerais.calendarToString(venda.getDataFechamento(),FuncoesGerais.yyyyMMdd, false));
        TextView valor = view.findViewById(R.id.textViewHistoricoClienteValor);
        valor.setText(FuncoesMatematicas.calculaValorTotalVendaComDesconto(venda));

        return view;
    }

}