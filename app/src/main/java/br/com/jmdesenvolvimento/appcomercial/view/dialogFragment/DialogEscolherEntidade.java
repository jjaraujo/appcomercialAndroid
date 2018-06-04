package br.com.jmdesenvolvimento.appcomercial.view.dialogFragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.com.jmdesenvolvimento.appcomercial.R;
import br.com.jmdesenvolvimento.appcomercial.controller.funcionais.Funcoes;
import br.com.jmdesenvolvimento.appcomercial.controller.funcionais.VariaveisControle;
import br.com.jmdesenvolvimento.appcomercial.model.Tabela;
import br.com.jmdesenvolvimento.appcomercial.model.dao.ProdutoDAO;
import br.com.jmdesenvolvimento.appcomercial.model.dao.SQLiteDatabaseDao;
import br.com.jmdesenvolvimento.appcomercial.model.entidades.Entidade;
import br.com.jmdesenvolvimento.appcomercial.model.entidades.cadastral.pessoas.Cliente;
import br.com.jmdesenvolvimento.appcomercial.model.entidades.estoque.Produto;
import br.com.jmdesenvolvimento.appcomercial.model.entidades.vendas.Venda;
import br.com.jmdesenvolvimento.appcomercial.model.tabelas.TabelaProdutosVenda;
import br.com.jmdesenvolvimento.appcomercial.view.adapters.ArrayAdapterTabelas;

@SuppressLint("ValidFragment")
public class DialogEscolherEntidade extends DialogFragment {

    private int numStyle;
    private int theme;
    private SearchView searchViewBusca;
    private int fragmentSelecionado;
    private ListView lista;
    private Entidade entidade;
    private SearchView searchView;
    private View view;

    public DialogEscolherEntidade(int nulStyle, int theme, int fragmentSelecionado) {
        this.numStyle = nulStyle;
        this.theme = theme;
        this.fragmentSelecionado = fragmentSelecionado;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int style;
        int theme;

        switch (numStyle) {
            case 1:
                style = DialogFragment.STYLE_NO_TITLE;
                break;
            case 2:
                style = DialogFragment.STYLE_NO_FRAME;
                break;
            case 3:
                style = DialogFragment.STYLE_NO_INPUT;
                break;
            default:
                style = DialogFragment.STYLE_NORMAL;
                break;
        }

        switch (this.theme) {
            case 1:
                theme = android.R.style.Theme_Holo_Light;
                break;
            case 2:
                theme = android.R.style.Theme_Holo_Dialog;
                break;
            default:
                theme = android.R.style.Theme_Holo_Light_DarkActionBar;
                break;
        }
        setStyle(style, theme);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_fragment_escolher_entidade, container, false);
        carregaList(view, null);

        searchView = view.findViewById(R.id.searchViewBuscaEntidade);

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                switch (fragmentSelecionado) {
                    case 0:
                        addCliente(parent, position);
                        break;
                    case 1:
                        VariaveisControle.dialogEscolherEntidade = DialogEscolherEntidade.this;
                        openDialogFragment(parent, position);
                        // addProduto(parent, position);
                        break;
                    default:

                        break;
                }
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                carregaList(view, newText);
                return false;
            }
        });

        return view;
    }

    private void addCliente(AdapterView<?> parent, int position) {
        Cliente cliente = (Cliente) parent.getItemAtPosition(position);
        Venda venda = new Venda();
        venda.setCliente(cliente);
        VariaveisControle.VENDA_SELECIONADA = venda;
        venda.setDataRegistro(Funcoes.getDataHojeDDMMAAAA());
        SQLiteDatabaseDao dao = new SQLiteDatabaseDao(getContext());
        dao.insere(venda);
        dao.close();
        VariaveisControle.fragmentVendasAbertas.carregaLista();
        VariaveisControle.fragmentProdutos.carregaLista();
        Funcoes.alteraViewVendaSelecionada();
        dismiss();
    }

    public void addProduto(AdapterView<?> parent, int position) {
        Produto produto = (Produto) parent.getItemAtPosition(position);
        Venda venda = VariaveisControle.VENDA_SELECIONADA;

        if (venda == null) {
            venda = new Venda();
        }

        TabelaProdutosVenda tpv = new TabelaProdutosVenda();
        tpv.setDataCadastro(Funcoes.getDataHojeDDMMAAAA());
        tpv.setVenda_id(venda.getId());
        tpv.setProduto(produto);
        tpv.setQtd(VariaveisControle.qtdSelecionadaProdutoVenda);

        if (venda.getTabelaProdutosVenda() == null) {
            List<TabelaProdutosVenda> listTPV = new ArrayList<>();
            venda.setTabelaProdutosVenda(listTPV);
        }
        venda.getTabelaProdutosVenda().add(tpv);
        venda.setDataRegistro(Funcoes.getDataHojeDDMMAAAA());
        SQLiteDatabaseDao dao = new SQLiteDatabaseDao(getContext());
        dao.insere(tpv);
        ProdutoDAO produtoDao = new ProdutoDAO(getContext());
        produtoDao.subtraiEstoque(tpv);
        dao.close();
        VariaveisControle.fragmentProdutos.carregaLista();
        Funcoes.alteraViewVendaSelecionada();
      //  openDialogFragment();

    }

    private void carregaList(View view, String query) {

        String where = null;
        SQLiteDatabaseDao dao = new SQLiteDatabaseDao(getContext());
        List<Tabela> listTabela;

        switch (fragmentSelecionado) {
            case 0:
                Cliente cliente = new Cliente();
                entidade = cliente;
                if (query != null && Funcoes.somenteNumero(query)) {
                    listTabela = dao.buscaClientesPorNomeCpf("cpfCNPJ", query);
                } else if(query != null) {
                    listTabela = dao.buscaClientesPorNomeCpf("nome_pessoa", query);
                } else{ // caso query == null, irá buscar todos os resultados
                    listTabela = (List<Tabela>) dao.buscaTodos(cliente, query,false );
                }
                break;

            case 1:
                Produto produto = new Produto();
                entidade = produto;
                if (query != null) {
                    where = " nome_produto like " + "'%" + Funcoes.removeCaracteresEspeciais(query).replace(" ", "%") + "%'"
                            + " AND qtd > 0";
                    if (Funcoes.somenteNumero(query)) {
                        where = produto.getIdNome() + " = " + query;
                    }
                } else { // caso query == null, irá buscar somente os produtos com qtd > 0
                    where = " qtd > 0";
                }
                listTabela = (List<Tabela>) dao.buscaTodos(produto, where,false);
                break;

            default:
                listTabela = (List<Tabela>) dao.buscaTodos(entidade, where,false);
                break;
        }

        dao.close();
        ArrayAdapterTabelas adapter = new ArrayAdapterTabelas(getContext(), listTabela);
        lista = (ListView) view.findViewById(R.id.list_escolha_clientes);
        lista.setAdapter(adapter);
        lista.setAdapter(adapter);
    }

    public void openDialogFragment(AdapterView<?> parent, int position) {
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        if (fragmentSelecionado == 1 && VariaveisControle.VENDA_SELECIONADA == null) {
            new AlertDialog.Builder(getContext()).
                    setMessage("Nenhuma venda selecionada!").show();
            //  alert.setMessage("Não");
        } else {
            DialogQuantidadeProduto dialog = new DialogQuantidadeProduto(1, 2, parent, position);
            dialog.show(ft, "dialogQuantidadeProduto");
        }
    }

    /* @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
       super.onCreateDialog(savedInstanceState);
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity())
                .setTitle("Teste")
          //      .setIcon()
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getActivity(),"Ok",Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                        Toast.makeText(getActivity(),"Cancelar",Toast.LENGTH_SHORT).show();
                    }
                })
                ;
        return  null;
    } */


}
