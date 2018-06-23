package br.com.jmdesenvolvimento.appcomercial.view.activitys;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import br.com.jmdesenvolvimento.appcomercial.controller.funcionaisAndroid.FuncoesConfiguracao;
import br.com.jmdesenvolvimento.appcomercial.controller.funcionaisAndroid.FuncoesViewAndroid;
import br.com.jmdesenvolvimento.appcomercial.controller.funcoesGerais.FuncoesGerais;
import br.com.jmdesenvolvimento.appcomercial.controller.funcoesGerais.FuncoesConfiguracaoG;
import br.com.jmdesenvolvimento.appcomercial.controller.funcionaisAndroid.VariaveisControleAndroid;
import br.com.jmdesenvolvimento.appcomercial.controller.funcoesGerais.VariaveisControleG;
import br.com.jmdesenvolvimento.appcomercial.model.entidades.vendas.Venda;
import br.com.jmdesenvolvimento.appcomercial.view.activitys.configuracoes.ConfiguracoesActivity;
import br.com.jmdesenvolvimento.appcomercial.view.activitys.entidades.PagamentoActivity;
import br.com.jmdesenvolvimento.appcomercial.view.activitys.entidades.contas.ContaReceberActivity;
import br.com.jmdesenvolvimento.appcomercial.view.activitys.entidades.pessoas.PessoasActivity;
import br.com.jmdesenvolvimento.appcomercial.view.activitys.entidades.contas.CadastroContasPagarActivity;
import br.com.jmdesenvolvimento.appcomercial.view.activitys.entidades.estoque.EstoqueActivity;
import br.com.jmdesenvolvimento.appcomercial.view.adapters.adaptersFragments.AdapterFragmentActivityPrincipal;
import br.com.jmdesenvolvimento.appcomercial.view.dialogFragment.DialogEscolherEntidade;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private AdapterFragmentActivityPrincipal adapterFragmentActivityPrincipal;
    private int fragmentSelecionado;
    private TextView vendaSelectionada;
    private Button valorTotal;
    private Button buttonFinalizarVenda;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FuncoesConfiguracao.iniciaConfiguracoes(this);

        setTitle("Vendas abertas");

        vendaSelectionada = findViewById(R.id.textViewVendaEscolhina);
        valorTotal = findViewById(R.id.buttonFinalizarVenda);
        VariaveisControleAndroid.textViewVendaSelectionada = vendaSelectionada;
        VariaveisControleAndroid.buttonValorTotal = valorTotal;

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
        tabLayout.getTabAt(0).setIcon(R.drawable.icone_loja_azul);
        tabLayout.getTabAt(1).setIcon(R.drawable.icone_carrinho_cinza);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tabLayout.getTabAt(1).setIcon(R.drawable.icone_carrinho_cinza);
                tabLayout.getTabAt(0).setIcon(R.drawable.icone_loja_cinza);
                switch (tab.getPosition()){
                    case 0:
                        tabLayout.getTabAt(0).setIcon(R.drawable.icone_loja_azul);
                        break;
                    case 1:
                        tabLayout.getTabAt(1).setIcon(R.drawable.icone_carrinho_azul);
                        break;
                }
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

        buttonFinalizarVenda = findViewById(R.id.buttonFinalizarVenda);
        cliqueButonFinalizar();
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
        // Inflate o menu; adiciona itens para o action bar caso existam
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_configuracoes) {
            Intent intent = new Intent(MainActivity.this, ConfiguracoesActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    // cliques do menu superior esquerdo
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int id = item.getItemId();

        return super.onContextItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Toast.makeText(this, "onNavigationItemSelected -" + id, Toast.LENGTH_SHORT).show();
        if (id == R.id.menuVendasEncerradas) {

        } else if (id == R.id.menuPessoas) {
            Intent intent = new Intent(MainActivity.this, PessoasActivity.class);
            startActivity(intent);
        } else if (id == R.id.menuContas) {
            Intent intent = new Intent(MainActivity.this, ContaReceberActivity.class);
            startActivity(intent);
        } else if (id == R.id.menuEstoque) {
            Intent intent = new Intent(MainActivity.this, EstoqueActivity.class);
            startActivity(intent);
        } else  if (id == R.id.menuConfiguracoes) {
            Intent intent = new Intent(MainActivity.this, ConfiguracoesActivity.class);
            startActivity(intent);
            return true;
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
                    Snackbar.make(v, "Selecione uma venda!", Snackbar.LENGTH_LONG).show();
                } else {
                    if (venda.getCliente().getId() == 0) {
                        textMensagem = "Deseja fechar a " + VariaveisControleG.configuracoesSimples.getNomeTipoVenda()
                                + " " + venda.getNumeroMesaComanda() + "?";
                    } else {
                        textMensagem = "Deseja fechar a venda " + venda.getId() + "?";
                    }
                    new AlertDialog.Builder(MainActivity.this)
                            .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(MainActivity.this, PagamentoActivity.class);
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
}
