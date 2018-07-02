package br.com.jmdesenvolvimento.appcomercial.view.adapters.arraysAdapter.tabelas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import br.com.jmdesenvolvimento.appcomercial.R;
import com.jmdesenvolvimento.appcomercial.controller.funcoesGerais.FuncoesGerais;
import com.jmdesenvolvimento.appcomercial.controller.funcoesGerais.FuncoesMatematicas;
import com.jmdesenvolvimento.appcomercial.model.Tabela;
import com.jmdesenvolvimento.appcomercial.model.entidades.contas.ContaReceber;

public class ArrayAdapterContasReceber extends BaseAdapter {
    private List<Tabela> list;
    private Context context;

    public ArrayAdapterContasReceber(Context context, List<?> listTabela) {
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
        View view = layoutInflater.inflate(R.layout.list_model_contas_receber, null);
        ContaReceber contaReceber = (ContaReceber) list.get(position);
        TextView textViewNomeCliente = view.findViewById(R.id.textViewContaRecebrNomeCliente);
        TextView textViewDataVencimento = view.findViewById(R.id.textViewContaReceberDataVencimento);
        TextView textViewNumeroParcela = view.findViewById(R.id.textViewContaReceberNumeroParcela);
        TextView textViewTipoPagamento = view.findViewById(R.id.textViewContaReceberTipoPagamento);
        TextView textValor = view.findViewById(R.id.textViewValorContaReceber);
        ImageView imageViewIconeCartao = view.findViewById(R.id.imageViewContaReceberImagemTipoPagamento);

        textViewNomeCliente.setText(contaReceber.getCliente().getPessoa().getNome());
        textViewDataVencimento.setText(FuncoesGerais.calendarToString(contaReceber.getDataVencimento(),FuncoesGerais.ddMMyyyy,false));

        int qtdParcelas = contaReceber.getParcelasPagamento().getTotalParcelas();
        int parcela = contaReceber.getParcelasPagamento().getNumeroParcela();
        textViewNumeroParcela.setText(parcela +" de " + qtdParcelas);

        textViewTipoPagamento.setText( contaReceber.getTipoPagamento().getNome());

        textValor.setText(ajustaValorTexto(contaReceber) );

        return view;
    }

    private String ajustaValorTexto(ContaReceber contaReceber){
       String valor = FuncoesMatematicas.formataValoresDouble(contaReceber.getParcelasPagamento().getValor());
       return valor.length()>6 ? "R$\n"+valor : "R$"+valor;
    }
}