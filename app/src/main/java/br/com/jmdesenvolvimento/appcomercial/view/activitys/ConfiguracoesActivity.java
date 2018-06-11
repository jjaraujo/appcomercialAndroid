package br.com.jmdesenvolvimento.appcomercial.view.activitys;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import br.com.jmdesenvolvimento.appcomercial.R;
import br.com.jmdesenvolvimento.appcomercial.view.adapters.ArrayAdapterListaConfiguracoes;

public class ConfiguracoesActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracoes);
        setTitle("Configurações");
        ListView listView = findViewById(R.id.list_configuracoes);
        ArrayAdapterListaConfiguracoes adapter = new ArrayAdapterListaConfiguracoes(this,listView);
        listView.setAdapter(adapter);
        listView.setClickable(true);
    }
}
