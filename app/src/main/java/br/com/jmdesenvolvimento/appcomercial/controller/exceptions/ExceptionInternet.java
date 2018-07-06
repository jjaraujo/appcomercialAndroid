package br.com.jmdesenvolvimento.appcomercial.controller.exceptions;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import br.com.jmdesenvolvimento.appcomercial.controller.funcionaisAndroid.FuncoesViewAndroid;

public class ExceptionInternet extends Exception {

    public void addMensagem(AppCompatActivity compatActivity, boolean encerrarActivity){
        FuncoesViewAndroid.addAlertDialogErro(compatActivity,"Falha na conexão",
                "Parece que você não está conectado à Internet. Verifique sua conexão e tente novamente",
                encerrarActivity).show();
    }

}
