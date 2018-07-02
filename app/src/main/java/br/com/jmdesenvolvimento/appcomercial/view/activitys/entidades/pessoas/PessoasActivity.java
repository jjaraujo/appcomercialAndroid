package br.com.jmdesenvolvimento.appcomercial.view.activitys.entidades.pessoas;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import br.com.jmdesenvolvimento.appcomercial.R;
import br.com.jmdesenvolvimento.appcomercial.controller.funcionaisAndroid.VariaveisControleAndroid;
import br.com.jmdesenvolvimento.appcomercial.view.adapters.adaptersFragments.AdapterFragmentActivityPessoas;

public class PessoasActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private AdapterFragmentActivityPessoas adapterPessoas;
    private int fragmentSelecionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pessoas);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Pessoas");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        // inicia os fragments de pessoas
        viewPager = findViewById(R.id.viewPagerPessoas);
        tabLayout = findViewById(R.id.tabLayoutPessoas);
        adapterPessoas = new AdapterFragmentActivityPessoas(getSupportFragmentManager());
        viewPager.setAdapter(adapterPessoas);
        tabLayout.setupWithViewPager(viewPager);

        addIconesFragments();
        tabLayout.getTabAt(0).setIcon(R.drawable.icone_usuario_azul); // deve ficar depois do addIconesFragments()

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                addIconesFragments();
                fragmentSelecionado = tab.getPosition();
                switch (fragmentSelecionado){
                    case 0:
                        tabLayout.getTabAt(0).setIcon(R.drawable.icone_usuario_azul);
                        break;
                    case 1:
                        tabLayout.getTabAt(1).setIcon(R.drawable.icone_fornecedores_azul);
                        break;
                    case 2:
                        tabLayout.getTabAt(2).setIcon(R.drawable.icone_vendendor_azul);
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

        final FloatingActionButton buttonAddPessoa = (FloatingActionButton) findViewById(R.id.buttonAddPessoas);
        VariaveisControleAndroid.buttonAddPessoaForSnackbar = buttonAddPessoa;
        buttonAddPessoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
               switch (fragmentSelecionado){
                   case 0:
                       intent = new Intent(PessoasActivity.this, CadastroClientesActivity.class);
                       intent.putExtra("tipoAbertura","cadastrar");
                       startActivity(intent);
                       break;
                   case 1:
                       intent = new Intent(PessoasActivity.this, CadastroFornecedoresActivity.class);
                       intent.putExtra("tipoAbertura","cadastrar");
                       startActivity(intent);
                       break;
                   case 2:
                       intent = new Intent(PessoasActivity.this, CadastroVendedoresActivity.class);
                       intent.putExtra("tipoAbertura","cadastrar");
                       startActivity(intent);
                       break;
               }
            }
        });
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

    private void addIconesFragments(){

        tabLayout.getTabAt(0).setIcon(R.drawable.icone_usuario_cinza);
        tabLayout.getTabAt(1).setIcon(R.drawable.icone_fornecedores_cinza);
        tabLayout.getTabAt(2).setIcon(R.drawable.icone_vendendor_cinza);
    }



}
