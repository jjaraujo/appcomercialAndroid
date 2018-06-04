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

import java.util.List;

import br.com.jmdesenvolvimento.appcomercial.R;
import br.com.jmdesenvolvimento.appcomercial.controller.funcionais.Funcoes;
import br.com.jmdesenvolvimento.appcomercial.controller.funcionais.VariaveisControle;
import br.com.jmdesenvolvimento.appcomercial.model.dao.SQLiteDatabaseDao;
import br.com.jmdesenvolvimento.appcomercial.model.entidades.vendas.Venda;
import br.com.jmdesenvolvimento.appcomercial.model.tabelas.TabelaProdutosVenda;
import br.com.jmdesenvolvimento.appcomercial.view.adapters.ArrayAdapterTabelas;
import br.com.jmdesenvolvimento.appcomercial.view.dialogFragment.DialogQuantidadeProduto;

public class FragmentProdutos extends Fragment {
    private ListView lista;
    private TabelaProdutosVenda tabelaProdutosVendaClicado;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_produtos, container, false);
        lista = (ListView) view.findViewById(R.id.produtosDaVenda);
        VariaveisControle.fragmentProdutos = this;

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tabelaProdutosVendaClicado = (TabelaProdutosVenda) lista.getItemAtPosition(position);
                VariaveisControle.qtdSelecionadaProdutoVenda = tabelaProdutosVendaClicado.getQtd();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                DialogQuantidadeProduto dqp = new DialogQuantidadeProduto(1,2);
                dqp.show(ft,"alterarQtdProduto");
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
        Venda venda = VariaveisControle.VENDA_SELECIONADA;
        String where = null;
        if (venda != null) {
            where = " venda_id = " + venda.getId();
            List<TabelaProdutosVenda> tpv = (List<TabelaProdutosVenda>) dao.buscaTodos(new TabelaProdutosVenda(), where, false);
            ArrayAdapterTabelas adapter = new ArrayAdapterTabelas(getContext(), tpv);
            venda.setTabelaProdutosVenda(tpv);
            lista.setAdapter(adapter);
        }
    }

    public void alteraQtdProdutoVenda(){

        tabelaProdutosVendaClicado.setQtd(VariaveisControle.qtdSelecionadaProdutoVenda);
        SQLiteDatabaseDao dao = new SQLiteDatabaseDao(getContext());
        dao.updateQtdProdutoVenda(tabelaProdutosVendaClicado);
        carregaLista();
    }
}
