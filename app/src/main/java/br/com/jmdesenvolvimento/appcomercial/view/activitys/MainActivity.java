package br.com.jmdesenvolvimento.appcomercial.view.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import br.com.jmdesenvolvimento.appcomercial.R;
import br.com.jmdesenvolvimento.appcomercial.controller.funcionais.VariaveisControle;
import br.com.jmdesenvolvimento.appcomercial.view.activitys.entidades.contas.ContasPagarActivity;
import br.com.jmdesenvolvimento.appcomercial.view.activitys.entidades.pessoas.ActivityClientes;
import br.com.jmdesenvolvimento.appcomercial.view.activitys.entidades.estoque.EstoqueActivity;
import br.com.jmdesenvolvimento.appcomercial.view.activitys.entidades.pessoas.ActivityFornecedores;
import br.com.jmdesenvolvimento.appcomercial.view.adapters.AdapterFragmentActivityPrincipal;
import br.com.jmdesenvolvimento.appcomercial.view.dialogFragment.DialogEscolherEntidade;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private AdapterFragmentActivityPrincipal adapterFragmentActivityPrincipal;
    private int fragmentSelecionado;
    private TextView vendaSelectionada;
    private Button valorTotal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("Vendas abertas");

        vendaSelectionada = findViewById(R.id.textViewVendaEscolhina);
        valorTotal = findViewById(R.id.buttonFinalizarVenda);
        VariaveisControle.vendaSelectionada = vendaSelectionada;
        VariaveisControle.valorTotal = valorTotal;

        // inicia os fragments de venda e produto
        tabLayout = findViewById(R.id.tabLayoutActivityPrincipal);
        viewPager = findViewById(R.id.viwePagerActivityPrincipal);
        adapterFragmentActivityPrincipal = new AdapterFragmentActivityPrincipal(getSupportFragmentManager());
        viewPager.setAdapter(adapterFragmentActivityPrincipal);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                fragmentSelecionado = tab.getPosition();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        iniciaComponentesMenuLateral(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogFragment(view);
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            Toast.makeText(this, "onBackPressed close", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "onBackPressed statrt", Toast.LENGTH_LONG).show();
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Toast.makeText(this, "onOptionsItemSelected -" + id, Toast.LENGTH_SHORT).show();
        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_formularios_salvar) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int id = item.getItemId();
        Toast.makeText(this, "onContextItemSelected -" + id, Toast.LENGTH_SHORT).show();
        return super.onContextItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Toast.makeText(this, "onNavigationItemSelected -" + id, Toast.LENGTH_SHORT).show();
        if (id == R.id.menuVendasEncerradas) {

        } else if (id == R.id.menuClientes) {
            Intent intent = new Intent(MainActivity.this, ActivityClientes.class);

            startActivity(intent);
        } else if (id == R.id.menuVendedores) {

        } else if (id == R.id.menuFornecedores) {
            Intent intent = new Intent(MainActivity.this, ActivityFornecedores.class);
            startActivity(intent);
        } else if (id == R.id.menuContaAPagar) {
            Intent intent = new Intent(MainActivity.this, ContasPagarActivity.class);
            startActivity(intent);
        } else if (id == R.id.menuContaAReceber) {

        } else if (id == R.id.menuEstoque) {
            Intent intent = new Intent(MainActivity.this, EstoqueActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void iniciaComponentesMenuLateral(Toolbar toolbar) {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }


    public void openDialogFragment(View view) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (fragmentSelecionado == 1 && VariaveisControle.VENDA_SELECIONADA == null) {
            new AlertDialog.Builder(this).
                    setMessage("Nenhuma venda selecionada!").show();
            //  alert.setMessage("Não");
        } else {
            DialogEscolherEntidade ecf = new DialogEscolherEntidade(1, 2, fragmentSelecionado);
            ecf.show(ft, "dialogEscolherCliente");
        }
    }

    public void turnOffDialogFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        DialogEscolherEntidade ecf = (DialogEscolherEntidade) getSupportFragmentManager().findFragmentByTag("dialogEscolherCliente");

        if (ecf != null) {
            ecf.dismiss();
            ft.remove(ecf);
        }
    }
}
