package br.com.jmdesenvolvimento.appcomercial.view.activitys.iniciais;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import app.jm.funcional.model.entidades.cadastral.pessoas.EmpresaCliente;

import app.jm.funcional.model.entidades.cadastral.pessoas.Usuario;
import br.com.jmdesenvolvimento.appcomercial.R;;
import br.com.jmdesenvolvimento.appcomercial.controller.IniciaAplicacaoTask;
import br.com.jmdesenvolvimento.appcomercial.model.dao.SQLiteDatabaseDao;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SQLiteDatabaseDao dao = new SQLiteDatabaseDao(this);
        EmpresaCliente ec = dao.selectEmpresaCliente();
        if( ec != null) {
            boolean login = false;
            if(getIntent().getSerializableExtra("login") != null){
                login = true;
            }
            new IniciaAplicacaoTask(this,login).execute();
        } else {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }

    private void backup(){

    }

}
