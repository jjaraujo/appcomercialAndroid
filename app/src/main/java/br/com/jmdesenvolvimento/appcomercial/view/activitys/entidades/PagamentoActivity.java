package br.com.jmdesenvolvimento.appcomercial.view.activitys.entidades;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import br.com.jmdesenvolvimento.appcomercial.R;
import com.jmdesenvolvimento.appcomercial.controller.funcoesGerais.FuncoesMatematicas;
import br.com.jmdesenvolvimento.appcomercial.controller.funcionaisAndroid.FuncoesVendas;
import br.com.jmdesenvolvimento.appcomercial.controller.funcionaisAndroid.VariaveisControleAndroid;
import com.jmdesenvolvimento.appcomercial.controller.VariaveisControleG;
import br.com.jmdesenvolvimento.appcomercial.model.dao.SQLiteDatabaseDao;
import com.jmdesenvolvimento.appcomercial.model.entidades.vendas.TipoPagamento;
import com.jmdesenvolvimento.appcomercial.model.entidades.vendas.Venda;
import com.jmdesenvolvimento.appcomercial.model.tabelasIntermediarias.TabelaPagamento;
import br.com.jmdesenvolvimento.appcomercial.view.adapters.arraysAdapter.ArrayAdapterPagamentoEscolhido;
import br.com.jmdesenvolvimento.appcomercial.view.adapters.arraysAdapter.tabelas.ArrayAdapterTiposPagamentos;
import br.com.jmdesenvolvimento.appcomercial.view.dialogFragment.DialogInformarValorDesconto;
import br.com.jmdesenvolvimento.appcomercial.view.dialogFragment.DialogInformarValorPagamento;

public class PagamentoActivity extends AppCompatActivity {
    private TextView textViewValorTotalPagamento;
    private TextView textViewRestantePagamento;
    private TextView textViewDesconto;
    private RelativeLayout relativeLayoutDesconto;
    private ListView listViewPagamentosEscolhidos;
    private double valotTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagamento);
        setTitle("Pagamento");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        GridView gridView = findViewById(R.id.gridViewPagamento);
        listViewPagamentosEscolhidos = findViewById(R.id.listViewPagamento);
        textViewRestantePagamento = findViewById(R.id.textViewValorRestantePagamento);
        textViewValorTotalPagamento = findViewById(R.id.textViewValorTotalPagamento);
        textViewDesconto = findViewById(R.id.textViewTextoDescontoPagamento);
        relativeLayoutDesconto = findViewById(R.id.relativeLayoutDescontoPagamento);

        VariaveisControleAndroid.activityPagamento = this;
        VariaveisControleG.valorRestante = FuncoesMatematicas.calculaValorTotalVendaDouble(VariaveisControleG.vendaSelecionada);

        valotTotal = FuncoesMatematicas.calculaValorTotalVendaDouble(VariaveisControleG.vendaSelecionada);
        final List list = getList();

        ArrayAdapterTiposPagamentos adapterTabelas = new ArrayAdapterTiposPagamentos(this, list);
        gridView.setAdapter(adapterTabelas);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Venda vendaSelecionada = VariaveisControleG.vendaSelecionada;
                TipoPagamento tipoPagamento = (TipoPagamento) list.get(position);
                TabelaPagamento tabelaPagamento = new TabelaPagamento(vendaSelecionada.getId());
                tabelaPagamento.setTipoPagamentos(tipoPagamento);

                DialogInformarValorPagamento informarValor = new DialogInformarValorPagamento(textViewRestantePagamento, tabelaPagamento);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                informarValor.show(ft, "");

            }
        });

        relativeLayoutDesconto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInformarValorDesconto informarValor = new DialogInformarValorDesconto(textViewDesconto);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                informarValor.show(ft, "dialogInformarDesconto");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregaTextValores();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_salvar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { //Botão adicional na ToolBar
        switch (item.getItemId()) {
            case android.R.id.home:  //ID do seu botão (gerado automaticamente pelo android, usando como está, deve funcionar
                finish();  //Método para matar a activity e não deixa-lá indexada na pilhagem
                break;
            case R.id.menu_formularios_salvar:
                    FuncoesVendas.finalizaVenda(this);
                break;
            default:
                break;
        }
        return true;
    }

    public void carregaTextValores() {
        double valorRestante = VariaveisControleG.valorRestante - VariaveisControleG.vendaSelecionada.getDesconto();

        textViewRestantePagamento.setText("R$" + FuncoesMatematicas.formataValoresDouble(valorRestante));
        textViewValorTotalPagamento.setText("R$" + FuncoesMatematicas.formataValoresDouble(valotTotal));
    }

    public void carregaListPagamentosEscolhidos() {
        List listPagamentos = VariaveisControleG.vendaSelecionada.getTabelaPagamentos();
        ArrayAdapterPagamentoEscolhido adapter = new ArrayAdapterPagamentoEscolhido(this, listPagamentos);
        listViewPagamentosEscolhidos.setAdapter(adapter);
    }

    public void excluiPagamentoDaVenda(Object o) {
        VariaveisControleG.vendaSelecionada.removePagamento((TabelaPagamento) o);
        carregaListPagamentosEscolhidos();
        carregaTextValores();
    }

    private List<TipoPagamento> getList(){
        SQLiteDatabaseDao dao = new SQLiteDatabaseDao(this);
        String where = VariaveisControleG.vendaSelecionada.getCliente() == null
                || VariaveisControleG.vendaSelecionada.getCliente().getId() == 0 ? "aceitaParcela = 0" : null;
        List<TipoPagamento> list = (List<TipoPagamento>) dao.selectAll(new TipoPagamento(), where, false);
        dao.close();
        return list;
    }

}
