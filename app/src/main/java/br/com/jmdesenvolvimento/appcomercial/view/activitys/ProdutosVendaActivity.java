package br.com.jmdesenvolvimento.appcomercial.view.activitys;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import br.com.jmdesenvolvimento.appcomercial.R;
import br.com.jmdesenvolvimento.appcomercial.controller.funcionaisAndroid.FuncoesViewAndroid;
import br.com.jmdesenvolvimento.appcomercial.controller.funcionaisAndroid.VariaveisControleAndroid;
import com.jmdesenvolvimento.appcomercial.controller.funcoesGerais.FuncoesGerais;
import com.jmdesenvolvimento.appcomercial.controller.VariaveisControleG;
import br.com.jmdesenvolvimento.appcomercial.model.dao.ProdutoDAO;
import br.com.jmdesenvolvimento.appcomercial.model.dao.SQLiteDatabaseDao;
import com.jmdesenvolvimento.appcomercial.model.entidades.vendas.Venda;
import com.jmdesenvolvimento.appcomercial.model.tabelasIntermediarias.TabelaProdutosVenda;
import br.com.jmdesenvolvimento.appcomercial.view.adapters.arraysAdapter.tabelas.ArrayAdapterTabelaProdutosVendas;
import br.com.jmdesenvolvimento.appcomercial.view.dialogFragment.DialogEscolherEntidade;
import br.com.jmdesenvolvimento.appcomercial.view.dialogFragment.DialogQuantidadeProduto;

public class ProdutosVendaActivity extends AppCompatActivity {

    private ListView lista;
    private TabelaProdutosVenda tabelaProdutosVendaClicado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produtos_venda);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        int numeroMesaComanda =  VariaveisControleG.vendaSelecionada.getNumeroMesaComanda();
        setTitle("Produtos da " + VariaveisControleG.configuracoesSimples.getNomeTipoVenda() + " " + FuncoesGerais.addZeros(numeroMesaComanda,2));

        lista = (ListView)findViewById(R.id.produtosDaVenda);
        VariaveisControleAndroid.produtosVendaActivity = this;

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tabelaProdutosVendaClicado = (TabelaProdutosVenda) lista.getItemAtPosition(position);
                VariaveisControleAndroid.qtdSelecionadaProdutoVenda = tabelaProdutosVendaClicado.getQtd();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                DialogQuantidadeProduto dialog = new DialogQuantidadeProduto(tabelaProdutosVendaClicado.getProduto());
                dialog.setCancelable(true);
                dialog.show(ft,"alterarQtdProduto");
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                DialogEscolherEntidade ecf = new DialogEscolherEntidade(1, 2, DialogEscolherEntidade.TIPO_PRODUTO);
                ecf.show(ft, "dialogEscolherCliente");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregaLista();
    }

    public void carregaLista() {
        SQLiteDatabaseDao dao = new SQLiteDatabaseDao(this);
        Venda venda = VariaveisControleG.vendaSelecionada;
        String where = null;
        if (venda != null) {
            where = " venda = " + venda.getId();
            List<TabelaProdutosVenda> tpv = (List<TabelaProdutosVenda>) dao.selectAll(new TabelaProdutosVenda(), where, false);
            ArrayAdapterTabelaProdutosVendas adapter = new ArrayAdapterTabelaProdutosVendas(this, tpv);
            venda.setTabelaProdutosVenda(tpv);
            lista.setAdapter(adapter);
        } else{
            ArrayAdapterTabelaProdutosVendas adapter = new ArrayAdapterTabelaProdutosVendas(this, new ArrayList<TabelaProdutosVenda>());
            lista.setAdapter(adapter);
        }
    }

    public void alteraQtdProdutoVenda(int qtdAnterior){

        tabelaProdutosVendaClicado.setQtd(VariaveisControleAndroid.qtdSelecionadaProdutoVenda);
        SQLiteDatabaseDao dao = new SQLiteDatabaseDao(this);
        dao.updateQtdProdutoVenda(tabelaProdutosVendaClicado);
        ProdutoDAO produtoDAO = new ProdutoDAO(this);
        produtoDAO.addEstoque(tabelaProdutosVendaClicado.getProduto().getId(),qtdAnterior);
        produtoDAO.subtraiEstoque(tabelaProdutosVendaClicado);
        dao.close();
        produtoDAO.close();

        carregaLista();
    }

}
