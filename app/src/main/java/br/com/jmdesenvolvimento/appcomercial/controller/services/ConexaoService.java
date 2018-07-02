package br.com.jmdesenvolvimento.appcomercial.controller.services;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

import retrofit2.Response;

public class ConexaoService extends AsyncTask<Void, Void, Void> {

    private String json;
    private Response response;

    public ConexaoService(String json){
        this.json = json;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... voids) {

        try {
            response = new RetrofitInicializador().getService().cadastraNovaEmpresa(json).execute();
            Log.i("Conexao bem sucedida",response.isSuccessful()+"");
            Log.i("Resposta", response.body()+"");

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
