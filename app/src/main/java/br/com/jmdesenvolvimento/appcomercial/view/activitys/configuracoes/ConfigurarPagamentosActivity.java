package br.com.jmdesenvolvimento.appcomercial.view.activitys.configuracoes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.List;

import br.com.jmdesenvolvimento.appcomercial.R;
import com.jmdesenvolvimento.appcomercial.model.Tabela;
import br.com.jmdesenvolvimento.appcomercial.model.dao.SQLiteDatabaseDao;
import com.jmdesenvolvimento.appcomercial.model.entidades.vendas.TipoPagamento;
import br.com.jmdesenvolvimento.appcomercial.view.adapters.arraysAdapter.tabelas.ArrayAdapterListaConfiguracoes;

public class ConfigurarPagamentosActivity extends AppCompatActivity {

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configurar_pagamentos);
        setTitle("Tipos de pagamentos");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        listView = findViewById(R.id.list_configuracoes_pagamento);
        SQLiteDatabaseDao dao = new SQLiteDatabaseDao(this);
        List<Tabela> tipoPagamentosList  = (List<Tabela>) dao.selectAll(new TipoPagamento(), null,false);
        dao.close();
        ArrayAdapterListaConfiguracoes adapterListaConfiguracoes = new ArrayAdapterListaConfiguracoes(this,null,tipoPagamentosList,ArrayAdapterListaConfiguracoes.TIPO_CONFIGURACAO_PAGAMENTO);
        listView.setAdapter(adapterListaConfiguracoes);
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
