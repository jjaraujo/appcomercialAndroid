package br.com.jmdesenvolvimento.appcomercial.controller.services;


import java.util.List;

import app.jm.funcional.model.Tabela;
import br.com.jmdesenvolvimento.appcomercial.controller.dto.ClienteSync;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import app.jm.funcional.model.entidades.cadastral.pessoas.Usuario;
import app.jm.funcional.model.entidades.cadastral.pessoas.Cliente;
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

    @GET("login/{login}/{senha}")
    public Call<String> login( @Path("login") String login, @Path("senha") String senha);

    @GET("select/{nomeTabela}/{id}")
    public Call<String> getTabela(@Path("nomeTabela") String nomeTabela, @Path("id") long id);

    @POST("insert/insertTabela")
    public  Call<String> insertTabela(@Body String s);

    @POST("insert/cadastraNovaEmpresa/{token}")
    public  Call<String> cadastraNovaEmpresa(@Body String empresa, @Path("token") String token);

    @POST("insert/cadastraProduto")
    public  Call<String> cadastraFuncionario(@Body String s);

    @POST("insert/cadastraProduto")
    public  Call<String> cadastraProduto(@Body String s);


}
