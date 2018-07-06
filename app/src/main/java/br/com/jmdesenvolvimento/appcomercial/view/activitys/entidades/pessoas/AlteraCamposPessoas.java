package br.com.jmdesenvolvimento.appcomercial.view.activitys.entidades.pessoas;

import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import java.lang.reflect.Field;

import com.jmdesenvolvimento.appcomercial.controller.funcoesGerais.FuncoesGerais;
import com.jmdesenvolvimento.appcomercial.model.entidades.cadastral.pessoas.Pessoa;

public abstract class AlteraCamposPessoas {

    public static void camposNotAcessible(AppCompatActivity pessoa) {
        Log.i("camposNotAcessible", "entrou");
        Field[] fields = pessoa.getClass().getDeclaredFields();
        for (Field f : fields) {
            if (ehFieldEditavel(f)) {
                f.setAccessible(true);
                try {
                    View v = (View) f.get(pessoa);
                    v.setEnabled(false);
                } catch (IllegalAccessException e) {

                    e.printStackTrace();
                } catch (NullPointerException e) {
                    Log.i("Erro", "Algo deu errado com o field " + f.getName());
                    e.printStackTrace();
                }
            }
        }
        Log.i("camposNotAcessible", "saiu");
    }

    public static void camposAcessible(AppCompatActivity pessoa, Menu menu) {
        Field[] fields = pessoa.getClass().getDeclaredFields();
        for (Field f : fields) {
            if (ehFieldEditavel(f)) {
                f.setAccessible(true);
                View v;
                try {
                    if (f.getType().getSimpleName().equals("TextInputEditText")) {
                        v = (TextInputEditText) f.get(pessoa);
                        TextInputEditText editText = (TextInputEditText) f.get(pessoa);
                        if (editText.getText().toString().equals(" "))
                            editText.setText("");
                    } else {
                        v = (View) f.get(pessoa);
                    }
                    v.setEnabled(true);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
  //      visualizacao = false;
        pessoa.onCreateOptionsMenu(menu);
    }

    public static boolean ehFieldEditavel(Field f) {
        return f.getType().getSimpleName().equals("TextInputEditText")
                || f.getType().getSimpleName().equals("AutoCompleteTextView")
                || f.getType().getSimpleName().contains("Spinner");
    }
}
