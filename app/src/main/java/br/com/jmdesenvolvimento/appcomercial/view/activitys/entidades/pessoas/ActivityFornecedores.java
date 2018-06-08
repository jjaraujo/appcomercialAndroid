package br.com.jmdesenvolvimento.appcomercial.view.activitys.entidades.pessoas;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.List;

import br.com.jmdesenvolvimento.appcomercial.R;
import br.com.jmdesenvolvimento.appcomercial.controller.funcionais.Funcoes;
import br.com.jmdesenvolvimento.appcomercial.model.Tabela;
import br.com.jmdesenvolvimento.appcomercial.model.dao.SQLiteDatabaseDao;
import br.com.jmdesenvolvimento.appcomercial.model.entidades.cadastral.pessoas.Cliente;
import br.com.jmdesenvolvimento.appcomercial.model.entidades.cadastral.pessoas.Fornecedor;
import br.com.jmdesenvolvimento.appcomercial.view.adapters.ArrayAdapterTabelas;

public class ActivityFornecedores extends AppCompatActivity {

    private ListView listaFornecedores;
    private FloatingActionButton buttonAdd;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fornecedores);
        setTitle("Fornecedores");

        listaFornecedores = findViewById(R.id.list_fornecedores);
        searchView = findViewById(R.id.searchViewFornecedores);
        buttonAdd = (FloatingActionButton) findViewById(R.id.fornecedoresButtonAdd);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityFornecedores.this, CadastroPessoasActivity.class);
                intent.putExtra("tipoPessoa","fornecedor");
                startActivity(intent);
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.i("Query",query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                carregaLista(newText);
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregaLista(null);
    }

    private void carregaLista(String query){
        SQLiteDatabaseDao dao = new SQLiteDatabaseDao(this);
        Fornecedor fornecedor = new Fornecedor();
        List<Tabela> clientes;
//        if(query != null){
//            if (Funcoes.somenteNumero(query)) {
//             //   clientes = dao.buscaClientesPorNomeCpf("cpfCNPJ",query);
//            } else {
//                clientes = dao.buscaClientesPorNomeCpf("nome_pessoa",query);
//            }
//        } else{
            clientes = (List<Tabela>) dao.selectAll(fornecedor,null,false);
      //  }
        dao.close();
        ArrayAdapterTabelas adapter = new ArrayAdapterTabelas(this,clientes);
        listaFornecedores.setAdapter(adapter);
    }
}
