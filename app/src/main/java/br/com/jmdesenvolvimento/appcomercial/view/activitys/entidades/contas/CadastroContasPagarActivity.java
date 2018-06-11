package br.com.jmdesenvolvimento.appcomercial.view.activitys.entidades.contas;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import br.com.jmdesenvolvimento.appcomercial.R;

public class CadastroContasPagarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_contas_pagar);
        setTitle("Cadastro de Conta a Pagar");
    }
}
