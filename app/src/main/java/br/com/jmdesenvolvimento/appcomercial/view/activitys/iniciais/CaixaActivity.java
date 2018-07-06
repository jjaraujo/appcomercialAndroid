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

import br.com.jmdesenvolvimento.appcomercial.R;

public class CaixaActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

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
    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        MenuLateral.onNavigationItemSelected(item, this);
        return true;
    }

}
