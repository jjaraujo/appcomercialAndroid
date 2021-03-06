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

import java.util.List;

import br.com.jmdesenvolvimento.appcomercial.R;
import com.jmdesenvolvimento.appcomercial.controller.funcoesGerais.FuncoesGerais;
import com.jmdesenvolvimento.appcomercial.model.Tabela;
import br.com.jmdesenvolvimento.appcomercial.model.dao.SQLiteDatabaseDao;
import com.jmdesenvolvimento.appcomercial.model.entidades.estoque.Produto;
import br.com.jmdesenvolvimento.appcomercial.view.adapters.arraysAdapter.tabelas.ArrayAdapterProdutos;

public class EstoqueActivity extends AppCompatActivity {

    private ListView listProdutos;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estoque);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Estoque");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        listProdutos = findViewById(R.id.list_produtos_estoque);
        searchView = findViewById(R.id.searchViewProdutos);

        FloatingActionButton botaoAdd = (FloatingActionButton) findViewById(R.id.floatingAddProduto);
        botaoAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EstoqueActivity.this, CadastroProdutoActivity.class);
                intent.putExtra("tipoAbertura","cadastrar");
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
                intent.putExtra("tipoAbertura","visualizar");
                Produto produto = (Produto) listProdutos.getItemAtPosition(position);
                intent.putExtra("produtoVisualizar", produto);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregaList(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_filtro, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home: // botao voltar
                finish();
                break;
            case R.id.menu_filtro:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void carregaList(String query){
        SQLiteDatabaseDao dao = new SQLiteDatabaseDao(this);
        String where = null;
        Produto p = new Produto();
        if(query != null){
            where = " nome_produto like " + "'%" + FuncoesGerais.removeCaracteresEspeciais(query).replace(" ","%") + "%'" ;
            if(FuncoesGerais.stringIsSomenteNumero(query)){
                where = p.getIdNome() + " = " + query;
            }
        }
        List<Tabela> produtos = (List<Tabela>) dao.selectAll(p, where, false,null,null,p.getIdNome(),"100");
        dao.close();
        ArrayAdapterProdutos adapter = new ArrayAdapterProdutos(this,produtos);
        listProdutos.setAdapter(adapter);
    }
}
