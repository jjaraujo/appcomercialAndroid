package br.com.jmdesenvolvimento.appcomercial.controller.services;


import java.util.List;

import br.com.jmdesenvolvimento.appcomercial.controller.dto.ClienteSync;
import com.jmdesenvolvimento.appcomercial.model.entidades.cadastral.pessoas.Cliente;
import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface Service {

    @GET("tabelas/listClientes")
    public Call<List<Cliente>> list();

    @POST("tabelas/insertTabela")
    public  Call<String> insertTabela(@Body String s);

    @POST("tabelas/cadastraNovaEmpresa")
    public  Call<String> cadastraNovaEmpresa(@Body String s);
}
