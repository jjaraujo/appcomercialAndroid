package br.com.jmdesenvolvimento.appcomercial.view.activitys.entidades.contas;

import android.support.v4.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.lang.reflect.Method;
import java.util.List;

import br.com.jmdesenvolvimento.appcomercial.R;
import br.com.jmdesenvolvimento.appcomercial.controller.funcionaisAndroid.FuncoesViewAndroid;
import br.com.jmdesenvolvimento.appcomercial.controller.funcionaisAndroid.VariaveisControleAndroid;
import br.com.jmdesenvolvimento.appcomercial.controller.funcoesGerais.FuncoesGerais;
import br.com.jmdesenvolvimento.appcomercial.model.dao.SQLiteDatabaseDao;
import br.com.jmdesenvolvimento.appcomercial.model.entidades.contas.ContaReceber;
import br.com.jmdesenvolvimento.appcomercial.model.filtrosOrdenacao.FiltroContasReceber;
import br.com.jmdesenvolvimento.appcomercial.view.adapters.arraysAdapter.tabelas.ArrayAdapterContasReceber;
import br.com.jmdesenvolvimento.appcomercial.view.dialogFragment.DialogFiltrosContaReceber;

public class ContaReceberActivity extends AppCompatActivity {

    private ListView listView;
    private FiltroContasReceber fcr;
    private List<?> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Contas a Receber");
        setContentView(R.layout.activity_conta_receber);
        listView = findViewById(R.id.listViewContasReceber);
        registerForContextMenu(listView);
        VariaveisControleAndroid.contaReceberActivity = this;
    }

    @Override
    protected void onResume() {
        super.onResume();
        ProgressDialog progressDialog = ProgressDialog.show(this, "Titulo", "Aguarde...", false, true);
        if(progressDialog.isShowing()){
            carregaLista(null);
        }
        progressDialog.dismiss();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_filtro, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.menu_contas_pagar,menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_filtro:
                DialogFiltrosContaReceber dialogFiltrosContaReceber = new DialogFiltrosContaReceber();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                dialogFiltrosContaReceber.show(ft,"dialogFiltroContasReceber]");
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo adapterContextMenuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        ContaReceber contaReceber = (ContaReceber) list.get(adapterContextMenuInfo.position);
        switch (item.getItemId()){
            case R.id.menu_conta_receber_excluir:
              chamaDialogExluirConta(contaReceber);
              break;
            case R.id.menu_conta_receber_informar_pagamento:
        }
        return super.onContextItemSelected(item);
    }

    private void chamaDialogExluirConta(ContaReceber contaReceber) {
        try {
            Method method = ContaReceberActivity.class.getDeclaredMethod("carregaLista", FiltroContasReceber.class);
            FuncoesViewAndroid.addAlertDialogExcluir(ContaReceberActivity.this, contaReceber,
                    "Conta excluida ", "Confirma exclusão da conta?",
                    method,this,new Object[]{fcr} );
        }catch (NoSuchMethodException e){

        }
    }

    public void carregaLista(FiltroContasReceber fcr){

        this.fcr = fcr;

        SQLiteDatabaseDao dao = new SQLiteDatabaseDao(this);
        list = dao.selectAll(new ContaReceber(),getFiltro(),false,null,"50");
        ArrayAdapterContasReceber adapter = new ArrayAdapterContasReceber(this,list);
        listView.setAdapter(adapter);
    }

    private String getFiltro(){
        if(fcr == null){
            return null;
        }
        String where = "" ;
        where +=  fcr.getCliente() == null || fcr.getCliente().getId() == 0  ? "" : " cliente = " + fcr.getCliente().getId();
        where += where.equals("") || fcr.getDataFim() == null ? "" : " AND ";
        where += fcr.getDataFim() == null ? "" : " dataVencimento BETWEEN "
                + FuncoesGerais.calendarToString(fcr.getDataInicio(),FuncoesGerais.yyyyMMdd_HHMMSS,true) + " AND "
        + FuncoesGerais.calendarToString(fcr.getDataFim(),FuncoesGerais.yyyyMMdd_HHMMSS,true);
        return where;
    }
}
