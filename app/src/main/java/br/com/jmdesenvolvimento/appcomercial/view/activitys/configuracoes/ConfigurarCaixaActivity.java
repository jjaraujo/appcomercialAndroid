package br.com.jmdesenvolvimento.appcomercial.view.activitys.configuracoes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.sql.Time;
import java.time.Instant;
import java.time.LocalTime;
import java.util.List;
import java.util.Timer;

import app.jm.funcional.model.entidades.vendas.HorariosCaixa;
import br.com.jmdesenvolvimento.appcomercial.R;
import br.com.jmdesenvolvimento.appcomercial.model.dao.SQLiteDatabaseDao;
import br.com.jmdesenvolvimento.appcomercial.view.adapters.arraysAdapter.tabelas.ArrayAdapterHorariosCaixa;

public class ConfigurarCaixaActivity extends AppCompatActivity {
    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configurar_caixa);

        listView = findViewById(R.id.listViewHorariosCaixa);
    }

    @Override
    protected void onResume() {
        super.onResume();

        SQLiteDatabaseDao dao = new SQLiteDatabaseDao(this);
        List<HorariosCaixa> horariosCaixas = (List<HorariosCaixa>) dao.selectAll(new HorariosCaixa(),null,false);
        ArrayAdapterHorariosCaixa adapter = new ArrayAdapterHorariosCaixa(this,horariosCaixas);
        listView.setAdapter(adapter);


    }
}
