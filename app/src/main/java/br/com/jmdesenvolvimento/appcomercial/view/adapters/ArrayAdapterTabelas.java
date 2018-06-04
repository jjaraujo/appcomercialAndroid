package br.com.jmdesenvolvimento.appcomercial.view.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.jmdesenvolvimento.appcomercial.R;
import br.com.jmdesenvolvimento.appcomercial.controller.funcionais.Funcoes;
import br.com.jmdesenvolvimento.appcomercial.controller.funcionais.FuncoesMatematicas;
import br.com.jmdesenvolvimento.appcomercial.controller.funcionais.VariaveisControle;
import br.com.jmdesenvolvimento.appcomercial.model.Tabela;
import br.com.jmdesenvolvimento.appcomercial.model.dao.SQLiteDatabaseDao;
import br.com.jmdesenvolvimento.appcomercial.model.entidades.Entidade;
import br.com.jmdesenvolvimento.appcomercial.model.entidades.cadastral.pessoas.Cliente;
import br.com.jmdesenvolvimento.appcomercial.model.entidades.cadastral.pessoas.Fornecedor;
import br.com.jmdesenvolvimento.appcomercial.model.entidades.estoque.Produto;
import br.com.jmdesenvolvimento.appcomercial.model.tabelas.TabelaProdutosVenda;

public class ArrayAdapterTabelas extends BaseAdapter {
    private List<Tabela> list = new ArrayList<>();
    private Context context;

    public ArrayAdapterTabelas(Context context, List<?> listTabela) {
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

        if ((list.get(position)).getNomeTabela(true).equals("cliente")) {
            return getAdapterCliente(position);
        } else if ((list.get(position)).getNomeTabela(true).equals("produto")) {
            return getAdapterProduto(position);
        } else if ((list.get(position)).getNomeTabela(false).equals("TabelaProdutosVenda")) {
            return getAdapterProdutoVenda(position);
        }
        if ((list.get(position)).getNomeTabela(true).equals("fornecedor")) {
            return getAdapterFornecedores(position);
        }
        return null;
    }

    public View getAdapterCliente(int position) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.list_clientes, null);
        Cliente c = (Cliente) list.get(position);
        TextView nome = view.findViewById(R.id.textViewListClientesNome);
        nome.setText(c.getPessoa().getNome());
        TextView cpf = view.findViewById(R.id.textViewListClientesCpf);
        cpf.setText(c.getPessoa().getCpfCNPJ() + "");
        TextView ultimaCompra = view.findViewById(R.id.textViewListClientesUltimaCompra);
//        if(c.getUltimaVenda().equals("")){
//            ultimaCompra.setText("");
//        } else{
//            ultimaCompra.setText(c.getUltimaVenda());
//        }
        return view;
    }

    public View getAdapterFornecedores(int position) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.list_clientes, null);
        Fornecedor fornecedor = (Fornecedor) list.get(position);
        TextView nome = view.findViewById(R.id.textViewListClientesNome);
        nome.setText(fornecedor.getPessoa().getNome());
        TextView cpf = view.findViewById(R.id.textViewListClientesCpf);
        cpf.setText(fornecedor.getPessoa().getCpfCNPJ() + "");
        TextView ultimaCompra = view.findViewById(R.id.textViewListClientesUltimaCompra);
//        if(fornecedor.getUltimaVenda().equals("")){
//            ultimaCompra.setText("");
//        } else{
//            ultimaCompra.setText(fornecedor.getUltimaVenda());
//        }
        return view;
    }

    public View getAdapterProduto(int position) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View view = layoutInflater.inflate(R.layout.list_produtos, null);
        Produto produto = (Produto) list.get(position);
        TextView nome = view.findViewById(R.id.textViewListProdutosNome);
        nome.setText(produto.getNome_produto());
        TextView grupo = view.findViewById(R.id.textViewListProdutosGrupo);
        //   grupo.setText(produto.getGrupo().getNome_grupo());
        TextView preco = view.findViewById(R.id.textViewListProdutosPreco);
        preco.setText("R$ " + (produto.getPreco() + "").replace(".", ","));

        TextView qtd = view.findViewById(R.id.textViewListProdutosQuantidade);
        String unidade = produto.getUnidade().getNome_unidade();
        qtd.setText(produto.getQtd() + " und");
        return view;
    }

    public View getAdapterProdutoVenda(final int position) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        final View view = layoutInflater.inflate(R.layout.list_produtos_venda, null);

        ImageView deletarProdutoVenda = view.findViewById(R.id.listProdutosVendaImageViewDelete);

        deletarProdutoVenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TabelaProdutosVenda produtosVenda = (TabelaProdutosVenda) list.get(position);
                Funcoes.alertDialogExcluirEntidade(context, produtosVenda, "Excluir o produto " + produtosVenda.getProduto().getNome_produto() + "?");
            }
        });

        TabelaProdutosVenda tabelaProdutosVenda = (TabelaProdutosVenda) list.get(position);

        TextView nomeProduto = view.findViewById(R.id.listProdutosVendaTextViewNomeProduto);
        nomeProduto.setText(tabelaProdutosVenda.getProduto().getNome_produto());

        TextView qtd = view.findViewById(R.id.listProdutosVendaTextViewQuantidade);
        qtd.setText("x" + tabelaProdutosVenda.getQtd() + " " + tabelaProdutosVenda.getProduto().getUnidade().getNome_unidade());

        TextView total = view.findViewById(R.id.listProdutosVendaTextViewTotal);
        total.setText("R$ " + (FuncoesMatematicas.calculaValorTotalProdutoVenda(tabelaProdutosVenda) + ""));

        return view;
    }
}