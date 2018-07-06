package br.com.jmdesenvolvimento.appcomercial.view.dialogFragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.com.jmdesenvolvimento.appcomercial.R;
import br.com.jmdesenvolvimento.appcomercial.controller.funcionaisAndroid.FuncoesVendas;
import br.com.jmdesenvolvimento.appcomercial.controller.funcionaisAndroid.FuncoesViewAndroid;
import com.jmdesenvolvimento.appcomercial.controller.funcoesGerais.FuncoesGerais;
import br.com.jmdesenvolvimento.appcomercial.controller.funcionaisAndroid.VariaveisControleAndroid;
import com.jmdesenvolvimento.appcomercial.controller.VariaveisControleG;
import com.jmdesenvolvimento.appcomercial.model.Tabela;
import br.com.jmdesenvolvimento.appcomercial.model.dao.ProdutoDAO;
import br.com.jmdesenvolvimento.appcomercial.model.dao.SQLiteDatabaseDao;
import com.jmdesenvolvimento.appcomercial.model.entidades.cadastral.pessoas.Cliente;
import com.jmdesenvolvimento.appcomercial.model.entidades.estoque.Produto;
import com.jmdesenvolvimento.appcomercial.model.entidades.vendas.Venda;
import com.jmdesenvolvimento.appcomercial.model.tabelasIntermediarias.TabelaProdutosVenda;
import br.com.jmdesenvolvimento.appcomercial.view.activitys.LeitorCodigoBarrasActivity;
import br.com.jmdesenvolvimento.appcomercial.view.adapters.arraysAdapter.tabelas.ArrayAdapterClientes;
import br.com.jmdesenvolvimento.appcomercial.view.adapters.arraysAdapter.tabelas.ArrayAdapterProdutos;

@SuppressLint("ValidFragment")
public class DialogEscolherEntidade extends DialogFragment {

    private int numStyle;
    private int theme;
    //   private int fragmentSelecionado;
    private int tipoEntidade;
    public static final int TIPO_CLIENTE = 0;
    public final static  int TIPO_PRODUTO = 1;
    private ListView lista;
    private Tabela entidade;
    private SearchView searchView;
    private View view;
    private ImageView imageViewCamera;
    private ImageView imageViewContador;

    List<Tabela> listTabela;

    public DialogEscolherEntidade(int nulStyle, int theme, int tipoEntidade) {
        this.numStyle = nulStyle;
        this.theme = theme;
        this.tipoEntidade = tipoEntidade;
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
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_fragment_escolher_entidade, container, false);
        carregaList(null);

        searchView = view.findViewById(R.id.searchViewBuscaEntidade);
        imageViewContador = view.findViewById(R.id.imageViewContador);
        imageViewCamera = view.findViewById(R.id.imageViewCamera);
        imageViewCamera.setVisibility(View.INVISIBLE);
        imageViewContador.setVisibility(View.INVISIBLE);

        imageViewCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LeitorCodigoBarrasActivity.class);
                startActivity(intent);
            }
        });

        imageViewContador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogFragmentComanda();
                onDestroy();
            }
        });

        switch (tipoEntidade) {
            case TIPO_CLIENTE:
                if(VariaveisControleG.configuracoesSimples.isVendaMesaComanda()){
                    imageViewContador.setVisibility(View.VISIBLE);
                }
               searchView.setQueryHint("Buscar clientes...");
                break;
            case TIPO_PRODUTO:
                imageViewCamera.setVisibility(View.VISIBLE);
                searchView.setQueryHint("Buscar produtos...");
                break;
        }

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                switch (tipoEntidade) {
                    case 0:
                            addCliente(parent, position);
                        break;
                    case 1:
                        VariaveisControleAndroid.dialogEscolherEntidade = DialogEscolherEntidade.this;
                        openDialogFragment(parent, position);
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
                carregaList(newText);
                return false;
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        String codigoLido = VariaveisControleAndroid.codigoDeBarrasLido;
        if(codigoLido!= null){
            if(!codigoLido.equals("")){
                carregaList(codigoLido);
                VariaveisControleAndroid.codigoDeBarrasLido = "";
            }
        }
    }

    private void addCliente(AdapterView<?> parent, int position) {
        Cliente cliente = (Cliente) parent.getItemAtPosition(position);
        Venda venda = new Venda();
        venda.setCliente(cliente);
        FuncoesVendas.criaVenda(venda,getContext());
        dismiss();
    }


    public void addProdutoNaVenda(AdapterView<?> parent, int position) {
        Produto produto = (Produto) parent.getItemAtPosition(position);
        Venda venda = VariaveisControleG.vendaSelecionada;

        if (venda == null) {
            venda = new Venda();
        }

        TabelaProdutosVenda tpv = new TabelaProdutosVenda();
        tpv.setDataCadastro(Calendar.getInstance());
        tpv.setVenda(venda.getId());
        tpv.setProduto(produto);
        tpv.setQtd(VariaveisControleAndroid.qtdSelecionadaProdutoVenda);

        if (venda.getTabelaProdutosVenda() == null) {
            List<TabelaProdutosVenda> listTPV = new ArrayList<>();
            venda.setTabelaProdutosVenda(listTPV);
        }
        venda.getTabelaProdutosVenda().add(tpv);
        venda.setDataRegistro(Calendar.getInstance());
        SQLiteDatabaseDao dao = new SQLiteDatabaseDao(getContext());
        dao.insert(tpv);
        ProdutoDAO produtoDao = new ProdutoDAO(getContext());
        produtoDao.subtraiEstoque(tpv);
        dao.close();
        VariaveisControleAndroid.produtosVendaActivity.carregaLista();
        FuncoesViewAndroid.alteraViewVendaSelecionada();
      //  openDialogFragment();
    }

    private void carregaList(String query) {

        String where = null;
        SQLiteDatabaseDao dao = new SQLiteDatabaseDao(getContext());
        BaseAdapter arrayAdapter = null;

        switch (tipoEntidade) {
            case TIPO_CLIENTE:
                Cliente cliente = new Cliente();
                entidade = cliente;
                if (query != null && FuncoesGerais.stringIsSomenteNumero(query)) {
                    listTabela = dao.buscaPessoaPorNomeCpf(cliente,"cpfCNPJ", query);
                } else if(query != null) {
                    listTabela = dao.buscaPessoaPorNomeCpf(cliente,"nome_pessoa", query);
                } else{ // caso query == null, irá buscar todos os resultados
                    listTabela = (List<Tabela>) dao.selectAll(cliente, query,false,null,null,cliente.getIdNome(),"100" );
                }
                arrayAdapter = new ArrayAdapterClientes(getContext(),listTabela);
                break;

            case TIPO_PRODUTO:
                Produto produto = new Produto();
                entidade = produto;
                if (query != null) {
                    where = " nome_produto like " + "'%" + FuncoesGerais.removeCaracteresEspeciais(query).replace(" ", "%") + "%'";

                    if (FuncoesGerais.stringIsSomenteNumero(query)) {
                        where = produto.getIdNome() + " = " + query
                        + " OR CodigoBarras = " + query;
                    }
                    // verifica a configuração de venda sem estoque do usuário
                    if(!VariaveisControleG.configuracoesSimples.isVendaSemEstoque()){
                        where += " AND qtd > 0";
                    }
                }
                else { // caso query == null, irá buscar somente os produtos com qtd > 0
                    // verifica a configuração de venda sem estoque do usuário
                    if(!VariaveisControleG.configuracoesSimples.isVendaSemEstoque()){
                        where = " qtd > 0";
                    }
                }
                listTabela = (List<Tabela>) dao.selectAll(produto, where,false,null,null,produto.getIdNome(),"100");
                arrayAdapter = new ArrayAdapterProdutos(getContext(),listTabela);
                break;
        }

        dao.close();
        if(listTabela != null) {
            lista = (ListView) view.findViewById(R.id.list_escolha_entidades);
            lista.setAdapter(arrayAdapter);
            lista.setAdapter(arrayAdapter);
        }
    }


    public void openDialogFragment(AdapterView<?> parent, int position) {
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        if (tipoEntidade == TIPO_CLIENTE && VariaveisControleG.vendaSelecionada == null) {
            new AlertDialog.Builder(getContext()).
                    setMessage("Nenhuma venda selecionada!").show();
            //  alert.setMessage("Não");
        } else {
            DialogQuantidadeProduto dialog = new DialogQuantidadeProduto(parent, position,(Produto) listTabela.get(position));
            dialog.show(ft, "dialogQuantidadeProduto");
        }
    }

    public void openDialogFragmentComanda() {
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();

            DialogVendaPorComanda dialog = new DialogVendaPorComanda();
            dialog.show(ft, "dialogComanda");
    }
}
