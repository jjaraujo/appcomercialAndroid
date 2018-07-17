package br.com.jmdesenvolvimento.appcomercial.view.activitys.iniciais;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

import app.jm.funcional.controller.VariaveisControle;
import app.jm.funcional.controller.funcoesGerais.FuncoesGerais;
import app.jm.funcional.controller.funcoesGerais.FuncoesMatematicas;
import app.jm.funcional.model.entidades.vendas.Caixa;
import app.jm.funcional.model.entidades.vendas.Venda;
import br.com.jmdesenvolvimento.appcomercial.R;
import br.com.jmdesenvolvimento.appcomercial.controller.services.conexoes.ConexaoServiceRecuperaTabela;
import br.com.jmdesenvolvimento.appcomercial.model.dao.SQLiteDatabaseDao;
import br.com.jmdesenvolvimento.appcomercial.view.activitys.ProdutosVendaActivity;;

public class CaixaActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private TextView textViewAvista;
    private TextView textViewAPrazo;
    private TextView textViewTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caixa);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Caixa");
        MenuLateral.iniciaComponentesMenuLateral(toolbar,this);
        FloatingActionButton fab = findViewById(R.id.fab);

        textViewAvista = findViewById(R.id.textViewCaixaValorAVista);
        textViewAPrazo = findViewById(R.id.textViewCaixaValorAPrazo);
        textViewTotal = findViewById(R.id.textViewCaixaValorTotal);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                criaVendaCaixa();
            }
        });

    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        MenuLateral.onNavigationItemSelected(item, this);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

        Object loginI = getIntent().getSerializableExtra("login");
        boolean login =loginI == null ? false : (boolean) loginI;
        if(login) {
            getIntent().putExtra("login",false);
            new ConexaoServiceRecuperaTabela(this).execute();
        }

        carregaCaixa();

    }

    private void carregaCaixa(){
        SQLiteDatabaseDao dao = new SQLiteDatabaseDao(this);
        Calendar calendarAgora = Calendar.getInstance();
        Caixa caixa = FuncoesMatematicas.calculaCaixa(calendarAgora, null, dao);
        List caixas = dao.selectAll(new Caixa(),null,true);
        textViewAPrazo.setText("R$ " + FuncoesMatematicas.formataValoresDouble(caixa.getValorTotalAPrazo()));
        textViewAvista.setText("R$ " + FuncoesMatematicas.formataValoresDouble(caixa.getValorTotalAVista()));
        textViewTotal.setText("R$ " + FuncoesMatematicas.formataValoresDouble(caixa.getValorTotal()));
    }


    private void criaVendaCaixa(){
        Venda venda = new Venda();
        venda.setTipoVenda(Venda.TIPO_CAIXA);
        venda.getMapAtributos(true);
        VariaveisControle.vendaSelecionada = venda;
        Intent intent = new Intent(CaixaActivity.this, ProdutosVendaActivity.class);
        startActivity(intent);

    }

}
