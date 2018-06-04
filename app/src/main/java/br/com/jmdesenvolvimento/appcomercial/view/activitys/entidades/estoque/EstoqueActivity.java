package br.com.jmdesenvolvimento.appcomercial.view.activitys.entidades.estoque;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.List;

import br.com.jmdesenvolvimento.appcomercial.R;
import br.com.jmdesenvolvimento.appcomercial.controller.funcionais.Funcoes;
import br.com.jmdesenvolvimento.appcomercial.controller.funcionais.VariaveisControle;
import br.com.jmdesenvolvimento.appcomercial.model.Tabela;
import br.com.jmdesenvolvimento.appcomercial.model.dao.SQLiteDatabaseDao;
import br.com.jmdesenvolvimento.appcomercial.model.entidades.estoque.Produto;
import br.com.jmdesenvolvimento.appcomercial.view.adapters.ArrayAdapterTabelas;

public class EstoqueActivity extends AppCompatActivity {

    private ListView listProdutos;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estoque);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Controle de Estoque");

        listProdutos = findViewById(R.id.list_produtos_estoque);
        searchView = findViewById(R.id.searchViewProdutos);

        FloatingActionButton botaoAdd = (FloatingActionButton) findViewById(R.id.floatingAddProduto);
        botaoAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EstoqueActivity.this, CadastroProdutoActivity.class);
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
                carregaList(newText);
                return false;
            }
        });

        listProdutos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(EstoqueActivity.this, CadastroProdutoActivity.class);
                intent.putExtra("TipoAbertura","VISUALIZAR");
                Produto produto = (Produto) listProdutos.getItemAtPosition(position);
                intent.putExtra("VISUALIZAR", produto);
                VariaveisControle.produtoEdicao = true;
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregaList(null);
        VariaveisControle.produtoEdicao = false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_filtro, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menu_formularios_salvar){

        }
        return super.onOptionsItemSelected(item);
    }

    private void carregaList(String query){
        SQLiteDatabaseDao dao = new SQLiteDatabaseDao(this);
        String where = null;
        Produto p = new Produto();
        if(query != null){
            where = " nome_produto like " + "'%" +Funcoes.removeCaracteresEspeciais(query).replace(" ","%") + "%'" ;
            if(Funcoes.somenteNumero(query)){
                where = p.getIdNome() + " = " + query;
            }
        }
        List<Tabela> produtos = (List<Tabela>) dao.buscaTodos(p, where, false);
        dao.close();
        ArrayAdapterTabelas adapter = new ArrayAdapterTabelas(this,produtos);
        listProdutos.setAdapter(adapter);
    }
}
