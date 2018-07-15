package br.com.jmdesenvolvimento.appcomercial.view.activitys.iniciais;

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

import app.jm.funcional.controller.funcoesGerais.FuncoesGerais;
import app.jm.funcional.controller.funcoesGerais.FuncoesMatematicas;
import app.jm.funcional.model.entidades.vendas.Caixa;
import br.com.jmdesenvolvimento.appcomercial.R;
import br.com.jmdesenvolvimento.appcomercial.model.dao.SQLiteDatabaseDao;;

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
        fab.setVisibility(View.INVISIBLE);

        textViewAvista = findViewById(R.id.textViewCaixaValorAVista);
        textViewAPrazo = findViewById(R.id.textViewCaixaValorAPrazo);
        textViewTotal = findViewById(R.id.textViewCaixaValorTotal);
    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        MenuLateral.onNavigationItemSelected(item, this);
        return true;
    }


    @Override
    protected void onResume() {
        super.onResume();
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


}
