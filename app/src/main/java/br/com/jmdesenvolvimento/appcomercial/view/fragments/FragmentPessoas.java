package br.com.jmdesenvolvimento.appcomercial.view.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.List;

import br.com.jmdesenvolvimento.appcomercial.R;
import br.com.jmdesenvolvimento.appcomercial.controller.funcoesGerais.FuncoesGerais;
import br.com.jmdesenvolvimento.appcomercial.model.Tabela;
import br.com.jmdesenvolvimento.appcomercial.model.dao.SQLiteDatabaseDao;
import br.com.jmdesenvolvimento.appcomercial.model.entidades.cadastral.pessoas.Cliente;
import br.com.jmdesenvolvimento.appcomercial.model.entidades.cadastral.pessoas.Fornecedor;
import br.com.jmdesenvolvimento.appcomercial.view.activitys.entidades.pessoas.CadastroPessoasActivity;
import br.com.jmdesenvolvimento.appcomercial.view.adapters.arraysAdapter.tabelas.ArrayAdapterClientes;
import br.com.jmdesenvolvimento.appcomercial.view.adapters.arraysAdapter.tabelas.ArrayAdapterFornecedores;

@SuppressLint("ValidFragment")
public class FragmentPessoas extends Fragment {


    private OnFragmentInteractionListener mListener;
    private ListView listView;
    private SearchView searchView;
    private int posicaoFragment;
    private String tipoPessoa;

    public FragmentPessoas() {
        // Required empty public constructor
    }


    public FragmentPessoas(int posicaoFragment, String tipoPessoa){
        this.posicaoFragment = posicaoFragment;
        this.tipoPessoa = tipoPessoa;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pessoa, container, false);
        listView = view.findViewById(R.id.listViewPessoas);
        searchView = view.findViewById(R.id.searchViewPessoas);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.i("Query",query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                switch (posicaoFragment){
                    case 0:
                        carregaListaClientes(newText);
                        break;
                    case 1:
                        carregaListaFornecedores(newText);
                        break;
                    case 2:
                        break;
                }
                return false;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), CadastroPessoasActivity.class);
                intent.putExtra("tipoAbertura","visualizar");
                Tabela pessoa = (Tabela) listView.getItemAtPosition(position);
                intent.putExtra("pessoaVisualizar", pessoa);
                intent.putExtra("tipoPessoa", tipoPessoa);
                startActivity(intent);
            }
        });

        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onResume() {
        switch (posicaoFragment){
            case 0:
                carregaListaClientes(null);
                break;
            case 1:
                carregaListaFornecedores(null);
                break;
            case 2:
                break;
        }
        super.onResume();
    }


    public void carregaListaClientes(String query){
        SQLiteDatabaseDao dao = new SQLiteDatabaseDao(getContext());
        Cliente cliente = new Cliente();
        List<Tabela> clientes;
        if(query != null){
            if (FuncoesGerais.stringIsSomenteNumero(query)) {
                clientes = dao.buscaPessoaPorNomeCpf(cliente,"cpfCNPJ",query);
            } else {
                clientes = dao.buscaPessoaPorNomeCpf(cliente,"nome_pessoa",query);
            }
        } else{
            clientes = (List<Tabela>) dao.selectAll(cliente,null,false,null,null,cliente.getIdNome(),"100");
        }
        dao.close();
        ArrayAdapterClientes adapter = new ArrayAdapterClientes(getContext(),clientes);
        listView.setAdapter(adapter);
    }

    public void carregaListaFornecedores(String query){
        SQLiteDatabaseDao dao = new SQLiteDatabaseDao(getContext());
        Fornecedor fornecedor = new Fornecedor();
        List<Tabela> fornecedores;
        if(query != null){
            if (FuncoesGerais.stringIsSomenteNumero(query)) {
                fornecedores = dao.buscaPessoaPorNomeCpf(fornecedor,"cpfCNPJ",query);
            } else {
                fornecedores = dao.buscaPessoaPorNomeCpf(fornecedor,"nome_pessoa",query);
            }
        } else{
        fornecedores = (List<Tabela>) dao.selectAll(fornecedor,null,false,null,null,fornecedor.getIdNome(),"100");
          }
        dao.close();
        ArrayAdapterFornecedores adapter = new ArrayAdapterFornecedores(getContext(),fornecedores);
        listView.setAdapter(adapter);
    }


}
