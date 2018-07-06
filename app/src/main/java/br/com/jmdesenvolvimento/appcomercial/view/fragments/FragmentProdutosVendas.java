package br.com.jmdesenvolvimento.appcomercial.view.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import br.com.jmdesenvolvimento.appcomercial.R;
import br.com.jmdesenvolvimento.appcomercial.controller.funcionaisAndroid.FuncoesViewAndroid;
import com.jmdesenvolvimento.appcomercial.controller.funcoesGerais.FuncoesGerais;
import br.com.jmdesenvolvimento.appcomercial.controller.funcionaisAndroid.VariaveisControleAndroid;
import com.jmdesenvolvimento.appcomercial.controller.VariaveisControleG;
import br.com.jmdesenvolvimento.appcomercial.model.dao.ProdutoDAO;
import br.com.jmdesenvolvimento.appcomercial.model.dao.SQLiteDatabaseDao;
import com.jmdesenvolvimento.appcomercial.model.entidades.vendas.Venda;
import com.jmdesenvolvimento.appcomercial.model.tabelasIntermediarias.TabelaProdutosVenda;
import br.com.jmdesenvolvimento.appcomercial.view.adapters.arraysAdapter.tabelas.ArrayAdapterTabelaProdutosVendas;
import br.com.jmdesenvolvimento.appcomercial.view.dialogFragment.DialogQuantidadeProduto;

public class FragmentProdutosVendas extends Fragment {
    private ListView lista;
    private TabelaProdutosVenda tabelaProdutosVendaClicado;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_produtos, container, false);
        lista = (ListView) view.findViewById(R.id.produtosDaVenda);
      //  VariaveisControleAndroid.fragmentProdutos = this;

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tabelaProdutosVendaClicado = (TabelaProdutosVenda) lista.getItemAtPosition(position);
                VariaveisControleAndroid.qtdSelecionadaProdutoVenda = tabelaProdutosVendaClicado.getQtd();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                DialogQuantidadeProduto dialog = new DialogQuantidadeProduto(tabelaProdutosVendaClicado.getProduto());
                dialog.setCancelable(true);
                dialog.show(ft,"alterarQtdProduto");
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        Log.i("FragProdutos", "OnResume");
        carregaLista();
        super.onResume();
    }


    @Override
    public void onPause() {
        Log.i("FragProdutos", "OnPause");
        super.onPause();
    }

    public void carregaLista() {
        SQLiteDatabaseDao dao = new SQLiteDatabaseDao(getContext());
        Venda venda = VariaveisControleG.vendaSelecionada;
        String where = null;
        if (venda != null) {
            where = " venda = " + venda.getId();
            List<TabelaProdutosVenda> tpv = (List<TabelaProdutosVenda>) dao.selectAll(new TabelaProdutosVenda(), where, false);
            ArrayAdapterTabelaProdutosVendas adapter = new ArrayAdapterTabelaProdutosVendas(getContext(), tpv);
            venda.setTabelaProdutosVenda(tpv);
            lista.setAdapter(adapter);
        } else{
            ArrayAdapterTabelaProdutosVendas adapter = new ArrayAdapterTabelaProdutosVendas(getContext(), new ArrayList<TabelaProdutosVenda>());
            lista.setAdapter(adapter);
        }
    }

    public void alteraQtdProdutoVenda(int qtdAnterior){

        tabelaProdutosVendaClicado.setQtd(VariaveisControleAndroid.qtdSelecionadaProdutoVenda);
        SQLiteDatabaseDao dao = new SQLiteDatabaseDao(getContext());
        dao.updateQtdProdutoVenda(tabelaProdutosVendaClicado);
        ProdutoDAO produtoDAO = new ProdutoDAO(getContext());
        produtoDAO.addEstoque(tabelaProdutosVendaClicado.getProduto().getId(),qtdAnterior);
        produtoDAO.subtraiEstoque(tabelaProdutosVendaClicado);
        dao.close();
        produtoDAO.close();
        
        carregaLista();
    }
}
