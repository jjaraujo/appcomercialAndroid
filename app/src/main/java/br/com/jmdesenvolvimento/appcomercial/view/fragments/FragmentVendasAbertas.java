package br.com.jmdesenvolvimento.appcomercial.view.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

import br.com.jmdesenvolvimento.appcomercial.R;
import br.com.jmdesenvolvimento.appcomercial.controller.funcionais.Funcoes;
import br.com.jmdesenvolvimento.appcomercial.controller.funcionais.FuncoesMatematicas;
import br.com.jmdesenvolvimento.appcomercial.controller.funcionais.VariaveisControle;
import br.com.jmdesenvolvimento.appcomercial.model.Tabela;
import br.com.jmdesenvolvimento.appcomercial.model.dao.SQLiteDatabaseDao;
import br.com.jmdesenvolvimento.appcomercial.model.entidades.vendas.Venda;

public class FragmentVendasAbertas extends Fragment{
    private  ListView lista;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vendas_abertas, container, false);
        VariaveisControle.fragmentVendasAbertas = this;
        lista = (ListView) view.findViewById(R.id.clientes_lista);

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Venda venda = (Venda) parent.getItemAtPosition(position);
                VariaveisControle.VENDA_SELECIONADA = venda;
                VariaveisControle.vendaSelectionada.setText(
                        "Venda selecionada: " +
                        venda.getCliente().getPessoa().toString());
                VariaveisControle.fragmentProdutos.carregaLista();
                VariaveisControle.valorTotal.setText("R$ " + FuncoesMatematicas.calculaValorTotalVenda(venda));
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
        List<Tabela> vendas = (List<Tabela>) dao.buscaTodos(new Venda(),null, false);
        dao.close();
        if(!vendas.isEmpty()){
            ArrayAdapter<Tabela> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, vendas);
            lista.setAdapter(adapter);
        }
    }
}
