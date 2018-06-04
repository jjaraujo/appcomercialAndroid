/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.jmdesenvolvimento.appcomercial.controller.json;

import android.content.Context;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import br.com.jmdesenvolvimento.appcomercial.model.entidades.Entidade;
import br.com.jmdesenvolvimento.appcomercial.model.entidades.cadastral.pessoas.Pessoa;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;



/**
 *
 * @author HP
 */
public class LeituraJson {
    Context context;

    public LeituraJson(Context context) {
        this.context = context;
    }

    public List<Entidade> lerJsonList(String s, Entidade entidade){
        Gson gson = new Gson();
        try{
        Type collectionType = new TypeToken<ArrayList<Entidade>>(){}.getType();
        List<Entidade> listEntidade = gson.fromJson(s, collectionType);
        return  listEntidade;
        } catch(JsonSyntaxException e){
            Toast.makeText(context,"Erro em ler JSON.LeituraJson().lerJsonListPessoas. Erro:" + e.getMessage(),Toast.LENGTH_LONG);
            return null;
        }
    }    
	
	public List<Entidade> lerJson(String s){
        Gson gson = new Gson();
        System.out.println("vai ler json");
        try{
        Type type = new TypeToken<List<Entidade>>(){}.getType();
        List<Entidade> entidade = gson.fromJson(s, type);
        return  entidade;
        } catch(JsonSyntaxException e){
            Toast.makeText(context,"Erro em ler JSON.LeituraJson().lerJsonPessoa Erro:" + e.getMessage(),Toast.LENGTH_LONG);
            return null;
        }
    }    
	
	public String tranformaParaJson(Entidade entidade){
        Gson gson = new Gson();
        try{
        return  gson.toJson(entidade);
        } catch(JsonSyntaxException e){
            Toast.makeText(context,"Erro em ler JSON.LeituraJson().enviarJsonPessoa Erro:" + e.getMessage(),Toast.LENGTH_LONG);
            return null;
        }
    }
	
	public String transformaListParaJson(List<Pessoa> list){
        Gson gson = new Gson();
        try{
        return  gson.toJson(list);
        } catch(JsonSyntaxException e){
            Toast.makeText(context,"Erro em ler JSON.LeituraJson().enviarJsonListPessoas Erro:" + e.getMessage(),Toast.LENGTH_LONG);
            return null;
        }
    } 
}
