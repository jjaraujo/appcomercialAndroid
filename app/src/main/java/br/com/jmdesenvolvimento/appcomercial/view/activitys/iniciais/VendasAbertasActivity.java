package br.com.jmdesenvolvimento.appcomercial.view.activitys.iniciais;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.support.design.widget.TabLayout;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.com.jmdesenvolvimento.appcomercial.R;;
import br.com.jmdesenvolvimento.appcomercial.controller.funcionaisAndroid.FuncoesViewAndroid;
import app.jm.funcional.controller.VariaveisControle;
import br.com.jmdesenvolvimento.appcomercial.model.dao.SQLiteDatabaseDao;
import app.jm.funcional.model.entidades.vendas.Venda;

import br.com.jmdesenvolvimento.appcomercial.view.activitys.ProdutosVendaActivity;
import br.com.jmdesenvolvimento.appcomercial.view.activitys.entidades.PagamentoDaVendaActivity;
import br.com.jmdesenvolvimento.appcomercial.view.adapters.adaptersFragments.AdapterFragmentActivityPrincipal;
import br.com.jmdesenvolvimento.appcomercial.view.adapters.arraysAdapter.tabelas.ArrayAdapterVendasAbertas;
import br.com.jmdesenvolvimento.appcomercial.view.dialogFragment.DialogEscolherEntidade;

public class VendasAbertasActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private AdapterFragmentActivityPrincipal adapterFragmentActivityPrincipal;
    private int fragmentSelecionado;
    private TextView vendaSelectionada;
    private Button valorTotal;
    private Button buttonFinalizarVenda;
    private GridView lista;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendas_abertas);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        setTitle("Vendas abertas");

      //  VariaveisControleAndroid.textViewVendaSelectionada = vendaSelectionada;
     //   VariaveisControleAndroid.buttonValorTotal = valorTotal;

        lista = (GridView) findViewById(R.id.vendas_abertas_lista);

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Venda venda = (Venda) parent.getItemAtPosition(position);
                VariaveisControle.vendaSelecionada = venda;
         //       FuncoesViewAndroid.alteraViewVendaSelecionada();
                // VariaveisControleAndroid.fragmentProdutos.carregaLista();
                if(venda.getDataRegistro() == null){
                    SQLiteDatabaseDao dao = new SQLiteDatabaseDao(VendasAbertasActivity.this);
                    venda.setDataRegistro(Calendar.getInstance());
                    dao.insert(venda);
                    dao.close();
                }

                Intent intent = new Intent(VendasAbertasActivity.this, ProdutosVendaActivity.class);
                startActivity(intent);
            }
        });
        MenuLateral.iniciaComponentesMenuLateral(toolbar,this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.INVISIBLE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogFragment(view);
            }
        });

     //   buttonFinalizarVenda = findViewById(R.id.buttonFinalizarVenda);
     //   cliqueButonFinalizar();
    }

    @Override
    protected void onResume() {
        carregaLista();
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        MenuLateral.onNavigationItemSelected(item, this);
        return true;
    }

    public void openDialogFragment(View view) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (fragmentSelecionado == 1 && VariaveisControle.vendaSelecionada == null) {
            FuncoesViewAndroid.addAlertDialogAlerta(this,"Atenção", "Nenhuma venda selecionada!");
            //  alert.setMessage("Não");
        } else {
            DialogEscolherEntidade ecf = new DialogEscolherEntidade(1, 2, fragmentSelecionado);
            ecf.show(ft, "dialogEscolherCliente");
        }
    }



    public void carregaLista(){
        SQLiteDatabaseDao dao = new SQLiteDatabaseDao(this);
        Venda venda = new Venda();
        String orderby = "numeroMesaComanda," + venda.getIdNome();
        List<Venda> vendasAbertas = (List<Venda>) dao.selectAll(venda,"dataFechamento IS NULL", false,null,null,orderby,null);
        dao.close();
        vendasAbertas = organizaQtdVendasAbertas(vendasAbertas);
        ArrayAdapterVendasAbertas adapterTabelas = new ArrayAdapterVendasAbertas(this,vendasAbertas);
        lista.setAdapter(adapterTabelas);
    }

    public List<Venda> organizaQtdVendasAbertas(List<Venda> listVendas){
        List<Venda> vendasAbertasOrganizadas = new ArrayList<>();
        int tamanho = VariaveisControle.configuracoesSimples.getNumeroDeMesasComandas();

        for(int i = 0; i < tamanho ; i++){
            vendasAbertasOrganizadas.add(new Venda(i+1));
        }
        // verifica se há vendas abertas e substitui por elas na lista
        for(int i = 0; i < listVendas.size(); i++) {
            Venda v = listVendas.get(i);
            vendasAbertasOrganizadas.remove(v.getNumeroMesaComanda() -1);
            Log.i("numero comanda",v.getNumeroMesaComanda()+"");
            vendasAbertasOrganizadas.add(v.getNumeroMesaComanda()-1,v);
        }

       return vendasAbertasOrganizadas;
    }

    private void addVendaEspacoVazio(List<Venda> listVendas, Venda venda1, Venda venda2) {
        int contador = venda1.getNumeroMesaComanda()+1;
        listVendas.add(venda1);
        while (contador < venda2.getNumeroMesaComanda()) {
            listVendas.add(new Venda(contador));
            contador ++;
        }
        listVendas.add(venda2);
    }
}
