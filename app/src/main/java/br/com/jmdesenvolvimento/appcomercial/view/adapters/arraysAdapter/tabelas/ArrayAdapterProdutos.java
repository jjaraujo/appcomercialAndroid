package br.com.jmdesenvolvimento.appcomercial.view.adapters.arraysAdapter.tabelas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import br.com.jmdesenvolvimento.appcomercial.R;;
import app.jm.funcional.controller.funcoesGerais.FuncoesGerais;
import app.jm.funcional.model.Tabela;
import app.jm.funcional.model.entidades.estoque.Produto;

public class ArrayAdapterProdutos extends BaseAdapter {
    private List<Tabela> list;
    private Context context;


    public ArrayAdapterProdutos(Context context, List<?> listTabela) {
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

        View view = layoutInflater.inflate(R.layout.list_model_produtos, null);
        Produto produto = (Produto) list.get(position);
        TextView textId = view.findViewById(R.id.textViewListProdutoCodigo);
        textId.setText(FuncoesGerais.addZeros(produto.getId(),4));
        TextView nome = view.findViewById(R.id.textViewListProdutosNome);
        nome.setText(produto.getNome_produto());
        TextView grupo = view.findViewById(R.id.textViewListProdutosGrupo);
        //   grupo.setText(produto.getGrupo().getNome_grupo());
        TextView preco = view.findViewById(R.id.textViewListProdutosPreco);
        preco.setText("R$ " + (produto.getPreco() + "").replace(".", ","));

        TextView qtd = view.findViewById(R.id.textViewListProdutosQuantidade);
      //  String unidade = produto.getUnidade().getNome_unidade();
        qtd.setText(produto.getQtd() + " und");
        return view;
    }
}