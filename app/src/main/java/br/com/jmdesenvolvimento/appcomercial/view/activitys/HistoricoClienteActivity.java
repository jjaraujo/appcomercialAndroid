package br.com.jmdesenvolvimento.appcomercial.view.activitys;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.List;

import br.com.jmdesenvolvimento.appcomercial.R;
import br.com.jmdesenvolvimento.appcomercial.model.dao.SQLiteDatabaseDao;
import com.jmdesenvolvimento.appcomercial.model.entidades.cadastral.pessoas.Cliente;
import com.jmdesenvolvimento.appcomercial.model.entidades.vendas.Venda;
import br.com.jmdesenvolvimento.appcomercial.view.adapters.arraysAdapter.tabelas.ArrayAdapterHitoricoClientes;

public class HistoricoClienteActivity extends AppCompatActivity {

    private Cliente cliente;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historico_cliente);
        listView = findViewById(R.id.listHistoricoClientes);
        cliente = (Cliente) getIntent().getSerializableExtra("clienteForHistorico");
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void carregaList(){
        SQLiteDatabaseDao dao = new SQLiteDatabaseDao(this);
        List<Venda> listVendas = (List<Venda>) dao.selectAll(new Venda(),"cliente = " + cliente.getId(), false);
        ArrayAdapterHitoricoClientes adapter = new ArrayAdapterHitoricoClientes(this, listVendas);
        listView.setAdapter(adapter);
    }
}
