package br.com.jmdesenvolvimento.appcomercial.view.activitys;

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
import android.support.v7.app.ActionBarDrawerToggle;
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
import java.util.List;

import br.com.jmdesenvolvimento.appcomercial.R;
import br.com.jmdesenvolvimento.appcomercial.controller.funcionaisAndroid.FuncoesConfiguracao;
import br.com.jmdesenvolvimento.appcomercial.controller.funcionaisAndroid.FuncoesViewAndroid;
import com.jmdesenvolvimento.appcomercial.controller.funcoesGerais.VariaveisControleG;
import br.com.jmdesenvolvimento.appcomercial.model.dao.SQLiteDatabaseDao;
import com.jmdesenvolvimento.appcomercial.model.entidades.vendas.Venda;
import br.com.jmdesenvolvimento.appcomercial.view.activitys.configuracoes.ConfiguracoesActivity;
import br.com.jmdesenvolvimento.appcomercial.view.activitys.entidades.PagamentoActivity;
import br.com.jmdesenvolvimento.appcomercial.view.activitys.entidades.contas.ContaReceberActivity;
import br.com.jmdesenvolvimento.appcomercial.view.activitys.entidades.pessoas.PessoasActivity;
import br.com.jmdesenvolvimento.appcomercial.view.activitys.entidades.estoque.EstoqueActivity;
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
        FuncoesConfiguracao.iniciaConfiguracoes(this);

        setTitle("Vendas abertas");

      //  VariaveisControleAndroid.textViewVendaSelectionada = vendaSelectionada;
     //   VariaveisControleAndroid.buttonValorTotal = valorTotal;

        lista = (GridView) findViewById(R.id.vendas_abertas_lista);

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Venda venda = (Venda) parent.getItemAtPosition(position);
                VariaveisControleG.vendaSelecionada = venda;
         //       FuncoesViewAndroid.alteraViewVendaSelecionada();
                // VariaveisControleAndroid.fragmentProdutos.carregaLista();
                if(venda.getId() == 0){
                    SQLiteDatabaseDao dao = new SQLiteDatabaseDao(VendasAbertasActivity.this);
                    dao.insert(venda);
                    dao.close();
                }

                Intent intent = new Intent(VendasAbertasActivity.this, ProdutosVendaActivity.class);
                startActivity(intent);
            }
        });
        iniciaComponentesMenuLateral(toolbar);

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
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.menuVendasEncerradas) {

        } else if (id == R.id.menuPessoas) {
            Intent intent = new Intent(VendasAbertasActivity.this, PessoasActivity.class);
            startActivity(intent);
        } else if (id == R.id.menuContas) {
            Intent intent = new Intent(VendasAbertasActivity.this, ContaReceberActivity.class);
            startActivity(intent);
        } else if (id == R.id.menuEstoque) {
            Intent intent = new Intent(VendasAbertasActivity.this, EstoqueActivity.class);
            startActivity(intent);
        } else  if (id == R.id.menuConfiguracoes) {
            Intent intent = new Intent(VendasAbertasActivity.this, ConfiguracoesActivity.class);
            startActivity(intent);
            return true;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void iniciaComponentesMenuLateral(Toolbar toolbar) {

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }


    public void openDialogFragment(View view) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (fragmentSelecionado == 1 && VariaveisControleG.vendaSelecionada == null) {
            FuncoesViewAndroid.addAlertDialogAlerta(this,"Nenhuma venda selecionada!");
            //  alert.setMessage("Não");
        } else {
            DialogEscolherEntidade ecf = new DialogEscolherEntidade(1, 2, fragmentSelecionado);
            ecf.show(ft, "dialogEscolherCliente");
        }
    }

    private void cliqueButonFinalizar(){
        buttonFinalizarVenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Venda venda = VariaveisControleG.vendaSelecionada;
                String textMensagem = "";
                if (venda == null) {
                    FuncoesViewAndroid.addSnackBarToast(v,VendasAbertasActivity.this, "Selecione uma venda!");
                } else {
                    if (venda.getCliente().getId() == 0) {
                        textMensagem = "Deseja fechar a " + VariaveisControleG.configuracoesSimples.getNomeTipoVenda()
                                + " " + venda.getNumeroMesaComanda() + "?";
                    } else {
                        textMensagem = "Deseja fechar a venda " + venda.getId() + "?";
                    }
                    new AlertDialog.Builder(VendasAbertasActivity.this)
                            .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(VendasAbertasActivity.this, PagamentoActivity.class);
                                    startActivity(intent);
                                }
                            })
                            .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .setTitle(textMensagem)
                            .setIcon(R.drawable.icone_pergunta)
                            .show();
                }
            }
        });
    }

    public void carregaLista(){
        SQLiteDatabaseDao dao = new SQLiteDatabaseDao(this);
        Venda venda = new Venda();
        String orderby = "numeroMesaComanda," + venda.getIdNome();
        List<Venda> vendasAbertas = (List<Venda>) dao.selectAll(venda,"dataFechamento IS NULL", false,null,null,orderby,null);
        dao.close();
        vendasAbertas = VariaveisControleG.configuracoesSimples.getNumeroDeMesasComandas() == 0 ? vendasAbertas : organizaQtdVendasAbertas(vendasAbertas);
        ArrayAdapterVendasAbertas adapterTabelas = new ArrayAdapterVendasAbertas(this,vendasAbertas);
        lista.setAdapter(adapterTabelas);
    }

    public List<Venda> organizaQtdVendasAbertas(List<Venda> listVendas){
        List<Venda> vendasAbertasOrganizadas = new ArrayList<>();
        int tamanho = VariaveisControleG.configuracoesSimples.getNumeroDeMesasComandas();

        for(int i = 0; i < tamanho ; i++){
            vendasAbertasOrganizadas.add(new Venda(i+1));
        }
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
