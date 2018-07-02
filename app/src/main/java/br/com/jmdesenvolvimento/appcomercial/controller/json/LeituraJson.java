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

import com.jmdesenvolvimento.appcomercial.controller.funcoesGerais.FuncoesGerais;
import com.jmdesenvolvimento.appcomercial.controller.funcoesGerais.VerificaTipos;
import com.jmdesenvolvimento.appcomercial.model.Tabela;
import com.jmdesenvolvimento.appcomercial.model.entidades.Entidade;
import com.jmdesenvolvimento.appcomercial.model.entidades.cadastral.pessoas.Pessoa;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;



/**
 *
 * @author HP
 */
public final class LeituraJson {


    public static List<Entidade> lerJsonList( Context context,String s, Entidade entidade){
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
	
	public static List<Entidade> lerJson( Context context, String s){
        Gson gson = new Gson();
        System.out.println("vai ler json");
        try{
        Type type = new TypeToken<List<Tabela>>(){}.getType();
        List<Entidade> entidade = gson.fromJson(s, type);
        return  entidade;
        } catch(JsonSyntaxException e){
            Toast.makeText(context,"Erro em ler JSON.LeituraJson().lerJsonPessoa Erro:" + e.getMessage(),Toast.LENGTH_LONG);
            return null;
        }
    }    
	
	public static String tranformaParaJson( Context context, Tabela entidade){
        Gson gson = new Gson();
        entidade.anulaMapAtributo();
        try{
            anulaMap(entidade);
            String json = gson.toJson(entidade);
        return  "\"" + entidade.getNomeTabela(true) + "\":" +
                json ;
        } catch(JsonSyntaxException e){
            Toast.makeText(context,"Erro em ler JSON.LeituraJson().enviarJsonPessoa Erro:" + e.getMessage(),Toast.LENGTH_LONG);

        }
        return null;
    }
	
	public static String transformaListParaJson(Context context, List<Pessoa> list){
        Gson gson = new Gson();
        try{
        return  gson.toJson(list);
        } catch(JsonSyntaxException e){
            Toast.makeText(context,"Erro em ler JSON.LeituraJson().enviarJsonListPessoas Erro:" + e.getMessage(),Toast.LENGTH_LONG);
            return null;
        }
    }

    private static void anulaMap(Tabela entidade) {
        try {
            for (Field f : entidade.getClass().getDeclaredFields()) {
                f.setAccessible(true);
                if (VerificaTipos.isTabela(f, entidade)) {
                    Tabela t = (Tabela) f.get(entidade);
                   if(t != null) {
                       t.anulaMapAtributo();
                       anulaMap(t);
                   } else{
                       Tabela tabela = FuncoesGerais.getNovaInstanciaTabela(f);
                       f.set(entidade,tabela);
                   }
                }
            }
            for (Field f : entidade.getClass().getFields()) {
                f.setAccessible(true);
                if (VerificaTipos.isTabela(f, entidade)) {
                    Tabela t = (Tabela) f.get(entidade);
                    if(t != null) {
                        t.anulaMapAtributo();
                        anulaMap(t);
                    } else{
                        Tabela tabela = FuncoesGerais.getNovaInstanciaTabela(f);
                        f.set(entidade,tabela);
                    }
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
