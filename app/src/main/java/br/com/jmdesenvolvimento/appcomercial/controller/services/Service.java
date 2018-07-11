package br.com.jmdesenvolvimento.appcomercial.controller.services;


import java.util.List;

import br.com.jmdesenvolvimento.appcomercial.controller.dto.ClienteSync;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.jmdesenvolvimento.appcomercial.model.entidades.cadastral.pessoas.AUsuario;
import com.jmdesenvolvimento.appcomercial.model.entidades.cadastral.pessoas.Cliente;
import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface Service {

    @GET("login/{tipo}/{login}/{senha}")
    public Call<AUsuario> login(@Path("tipo") int tipo, @Path("login") String login, @Path("senha") String senha);

    @POST("tabelas/insertTabela")
    public  Call<String> insertTabela(@Body String s);

    @POST("tabelas/cadastraNovaEmpresa/{token}")
    public  Call<String> cadastraNovaEmpresa(@Body String empresa, @Path("token") String token);

    @POST("tabelas/cadastraFuncionario")
    public  Call<String> cadastraFuncionario(@Body String s);


}
