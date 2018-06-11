package br.com.jmdesenvolvimento.appcomercial.view.adapters;

import android.content.Context;
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
import br.com.jmdesenvolvimento.appcomercial.model.entidades.cadastral.pessoas.Cliente;
import br.com.jmdesenvolvimento.appcomercial.model.entidades.cadastral.pessoas.Fornecedor;
import br.com.jmdesenvolvimento.appcomercial.model.entidades.estoque.Produto;
import br.com.jmdesenvolvimento.appcomercial.model.entidades.vendas.Venda;
import br.com.jmdesenvolvimento.appcomercial.model.tabelas.TabelaProdutosVenda;

public class ArrayAdapterTabelas extends BaseAdapter {
    private List<Tabela> list = new ArrayList<>();
    private Context context;
    public final static int TIPO_PRODUTOS = 1;
    public final static int TIPO_VENDAS_ABERTAS = 2;
    public final static int TIPO_TABELA_PRODUTOS_VENDA = 3;
    public final static int TIPO_CLIENTES = 4;
    public final static int TIPO_FORNECEDORES = 5;
    private int tipoTabelaArray;

    public ArrayAdapterTabelas(Context context, List<?> listTabela,int tipoTabelaArray) {
        this.list = (List<Tabela>) listTabela;
        this.context = context;
        this.tipoTabelaArray = tipoTabelaArray;
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
        switch (tipoTabelaArray){
            case TIPO_CLIENTES:
                return getAdapterCliente(position);
            case TIPO_PRODUTOS:
                return getAdapterProduto(position);
            case TIPO_TABELA_PRODUTOS_VENDA:
                return getAdapterProdutoVenda(position);
            case TIPO_FORNECEDORES:
                return getAdapterFornecedores(position);
            case TIPO_VENDAS_ABERTAS:
                return getAdapterVendasAbertas(position);
            default:
            return null;
        }
    }

    public View getAdapterCliente(int position) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.list_model_clientes, null);
        Cliente c = (Cliente) list.get(position);
        TextView nome = view.findViewById(R.id.textViewListClientesNome);
        nome.setText(c.getPessoa().getNome());
        TextView cpf = view.findViewById(R.id.textViewListClientesCpf);
        cpf.setText(c.getPessoa().getCpfCNPJ());
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
        View view = layoutInflater.inflate(R.layout.list_model_clientes, null);
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

    public View getAdapterVendasAbertas(int position) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.list_model_vendas_abertas, null);
        Venda v = (Venda) list.get(position);
        TextView textViewNumeroVenda = view.findViewById(R.id.textViewNumeroVenda);
        TextView textViewClienteTipoVenda = view.findViewById(R.id.textViewClienteTipoVenda);
        if(v.getCliente() == null){
            textViewNumeroVenda.setText(v.getNumeroMesaComanda()+"");
            textViewClienteTipoVenda.setText(VariaveisControle.configuracoesSimples.getNomeTipoVenda());
        } else{
            textViewNumeroVenda.setText(v.getId()+"");
            textViewClienteTipoVenda.setText(v.getCliente().getPessoa().getNome());
        }

        return view;
    }

    public View getAdapterProduto(int position) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View view = layoutInflater.inflate(R.layout.list_model_produtos, null);
        Produto produto = (Produto) list.get(position);
        TextView textId = view.findViewById(R.id.textViewListProdutoCodigo);
        textId.setText(Funcoes.addZeros(produto.getId(),4));
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
        final View view = layoutInflater.inflate(R.layout.list_model_produtos_venda, null);

        ImageView deletarProdutoVenda = view.findViewById(R.id.listProdutosVendaImageViewDelete);

        deletarProdutoVenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TabelaProdutosVenda produtosVenda = (TabelaProdutosVenda) list.get(position);
                Funcoes.addAlertDialogExcluir(context, produtosVenda,"Produto excluído",
                        "Excluir o produto " + produtosVenda.getProduto().getNome_produto() + "?");
            }
        });

        TabelaProdutosVenda tabelaProdutosVenda = (TabelaProdutosVenda) list.get(position);

        TextView nomeProduto = view.findViewById(R.id.listProdutosVendaTextViewNomeProduto);
        nomeProduto.setText((Funcoes.addZeros(tabelaProdutosVenda.getProduto().getId(),4))+" - "+tabelaProdutosVenda.getProduto().getNome_produto());

        TextView qtd = view.findViewById(R.id.listProdutosVendaTextViewQuantidade);
        qtd.setText("x" + tabelaProdutosVenda.getQtd() + " " + tabelaProdutosVenda.getProduto().getUnidade().getNome_unidade());

        TextView total = view.findViewById(R.id.listProdutosVendaTextViewTotal);
        total.setText("R$ " + (FuncoesMatematicas.calculaValorTotalProdutoVenda(tabelaProdutosVenda)));

        return view;
    }
}