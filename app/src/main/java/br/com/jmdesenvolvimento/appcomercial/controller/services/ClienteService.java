package br.com.jmdesenvolvimento.appcomercial.controller.services;


import java.util.List;

import br.com.jmdesenvolvimento.appcomercial.controller.dto.ClienteSync;
import br.com.jmdesenvolvimento.appcomercial.model.entidades.cadastral.pessoas.Cliente;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ClienteService {

    @GET("pessoas/listClientes")
    public Call<List<Cliente>> list();

}
