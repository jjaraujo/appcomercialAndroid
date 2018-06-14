package br.com.jmdesenvolvimento.appcomercial.view.activitys.entidades;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import br.com.jmdesenvolvimento.appcomercial.R;
import br.com.jmdesenvolvimento.appcomercial.model.dao.SQLiteDatabaseDao;
import br.com.jmdesenvolvimento.appcomercial.model.entidades.vendas.TipoPagamentos;
import br.com.jmdesenvolvimento.appcomercial.model.tabelas.TabelaPagamento;
import br.com.jmdesenvolvimento.appcomercial.view.adapters.ArrayAdapterTabelas;
import br.com.jmdesenvolvimento.appcomercial.view.dialogFragment.DialogInformarValor;

public class PagamentoActivity extends AppCompatActivity {
    TextView textViewValorTotalPagamento;
    TextView textViewRestantePagamento;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagamento);
        setTitle("Pagamento");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        GridView gridView = findViewById(R.id.gridViewPagamento);
        ListView listView = findViewById(R.id.listViewPagamento);
        textViewRestantePagamento = findViewById(R.id.textViewValorRestantePagamento);
        textViewValorTotalPagamento = findViewById(R.id.textViewValorTotalPagamento);
        SQLiteDatabaseDao dao = new SQLiteDatabaseDao(this);
        final List list = dao.selectAll(new TipoPagamentos(),null,false);
        ArrayAdapterTabelas adapterTabelas = new ArrayAdapterTabelas(this,list,ArrayAdapterTabelas.TIPO_PAGAMENTO);
        gridView.setAdapter(adapterTabelas);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TipoPagamentos tipoPagamentos = (TipoPagamentos) list.get(position);
                TabelaPagamento pagamento = new TabelaPagamento();
             //   DialogInformarValor informarValor = new DialogInformarValor(textViewValorTotalPagamento,null, tipoPagamentos);
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) { //Botão adicional na ToolBar
        switch (item.getItemId()) {
            case android.R.id.home:  //ID do seu botão (gerado automaticamente pelo android, usando como está, deve funcionar
                finish();  //Método para matar a activity e não deixa-lá indexada na pilhagem
                break;
            default:break;
        }
        return true;
    }
}
