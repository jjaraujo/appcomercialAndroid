package br.com.jmdesenvolvimento.appcomercial.controller.services;

import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class RetrofitInicializador {

    public final Retrofit retrofit;

    public RetrofitInicializador() {
        retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/aplicacao/rest/")
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
    }

    public ClienteService getAlunoService() {
        return retrofit.create(ClienteService.class);
    }
}
