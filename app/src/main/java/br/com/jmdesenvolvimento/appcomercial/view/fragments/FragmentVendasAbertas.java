package br.com.jmdesenvolvimento.appcomercial.view.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.List;

import br.com.jmdesenvolvimento.appcomercial.R;
import br.com.jmdesenvolvimento.appcomercial.controller.funcionais.Funcoes;
import br.com.jmdesenvolvimento.appcomercial.controller.funcionais.VariaveisControle;
import br.com.jmdesenvolvimento.appcomercial.model.Tabela;
import br.com.jmdesenvolvimento.appcomercial.model.dao.SQLiteDatabaseDao;
import br.com.jmdesenvolvimento.appcomercial.model.entidades.vendas.Venda;
import br.com.jmdesenvolvimento.appcomercial.view.adapters.ArrayAdapterTabelas;

public class FragmentVendasAbertas extends Fragment{
    private GridView lista;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vendas_abertas, container, false);
        VariaveisControle.fragmentVendasAbertas = this;
        lista = (GridView) view.findViewById(R.id.vendas_abertas_lista);

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Venda venda = (Venda) parent.getItemAtPosition(position);
                VariaveisControle.vendaSelecionada = venda;
                Funcoes.alteraViewVendaSelecionada();
                VariaveisControle.fragmentProdutos.carregaLista();
                Funcoes.alteraValorVendaSelecionada();
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        Log.i("FragVendasAbertas","OnResume");
        carregaLista();
        super.onResume();
    }


    @Override
    public void onPause() {
        Log.i("FragVendasAbertas","OnPause");
        super.onPause();
    }

    public void carregaLista(){
        SQLiteDatabaseDao dao = new SQLiteDatabaseDao(getContext());
        Venda venda = new Venda();
        String orderby = "numeroMesaComanda," + venda.getIdNome();
        List<Tabela> vendas = (List<Tabela>) dao.selectAll(venda,null, false,null,null,orderby,null);
        dao.close();
        if(!vendas.isEmpty()){
            ArrayAdapterTabelas adapterTabelas = new ArrayAdapterTabelas(getContext(),vendas,ArrayAdapterTabelas.TIPO_VENDAS_ABERTAS);
            lista.setAdapter(adapterTabelas);
        }
    }
}
