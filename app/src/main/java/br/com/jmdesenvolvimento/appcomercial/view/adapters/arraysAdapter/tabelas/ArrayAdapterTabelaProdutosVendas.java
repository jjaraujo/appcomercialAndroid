package br.com.jmdesenvolvimento.appcomercial.view.adapters.arraysAdapter.tabelas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Method;
import java.util.List;

import br.com.jmdesenvolvimento.appcomercial.R;;
import br.com.jmdesenvolvimento.appcomercial.controller.funcionaisAndroid.FuncoesViewAndroid;
import app.jm.funcional.controller.funcoesGerais.FuncoesGerais;
import app.jm.funcional.controller.funcoesGerais.FuncoesMatematicas;
import br.com.jmdesenvolvimento.appcomercial.controller.funcionaisAndroid.VariaveisControleAndroid;
import app.jm.funcional.model.Tabela;
import app.jm.funcional.model.tabelasIntermediarias.TabelaProdutosVenda;

public class ArrayAdapterTabelaProdutosVendas extends BaseAdapter {
    private List<Tabela> list;
    private Context context;

    public ArrayAdapterTabelaProdutosVendas(Context context, List<?> listTabela) {
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
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        final View view = layoutInflater.inflate(R.layout.list_model_produtos_venda, null);

        ImageView deletarProdutoVenda = view.findViewById(R.id.listProdutosVendaImageViewDelete);

        deletarProdutoVenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TabelaProdutosVenda produtosVenda = (TabelaProdutosVenda) list.get(position);
                try {
                    Method method = VariaveisControleAndroid.produtosVendaActivity.getClass().getDeclaredMethod("carregaLista",null);
                    FuncoesViewAndroid.addAlertDialogExcluir(context, produtosVenda,"Produto excluído",
                            "Excluir o produto " + produtosVenda.getProduto().getNome_produto() + "?",
                            method, VariaveisControleAndroid.produtosVendaActivity,null);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
        });

        TabelaProdutosVenda tabelaProdutosVenda = (TabelaProdutosVenda) list.get(position);

        TextView nomeProduto = view.findViewById(R.id.listProdutosVendaTextViewNomeProduto);
        nomeProduto.setText((FuncoesGerais.addZeros(tabelaProdutosVenda.getProduto().getIdNaEmpresa(),4))+" - "+tabelaProdutosVenda.getProduto().getNome_produto());

        TextView qtd = view.findViewById(R.id.listProdutosVendaTextViewQuantidade);
        qtd.setText("x" + tabelaProdutosVenda.getQtd() + " " + tabelaProdutosVenda.getProduto().getUnidade().getNome_unidade());

        TextView total = view.findViewById(R.id.listProdutosVendaTextViewTotal);
        total.setText("R$ " + (FuncoesMatematicas.calculaValorTotalProdutoVenda(tabelaProdutosVenda)));

        return view;
    }

}