package br.com.jmdesenvolvimento.appcomercial.controller.funcionaisAndroid;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Calendar;

import br.com.jmdesenvolvimento.appcomercial.R;
import com.jmdesenvolvimento.appcomercial.controller.funcoesGerais.FuncoesGerais;
import com.jmdesenvolvimento.appcomercial.controller.funcoesGerais.VariaveisControleG;
import com.jmdesenvolvimento.appcomercial.model.Tabela;
import br.com.jmdesenvolvimento.appcomercial.model.dao.SQLiteDatabaseDao;
import com.jmdesenvolvimento.appcomercial.model.entidades.vendas.Venda;

public final class FuncoesViewAndroid {

    private static boolean usandoDatePickerDialog;
    public static final String yyyyMMdd_HHMMSS = "yyyy-MM-dd hh:mm:ss";
    public static final String ddMMyyyy = "dd/MM/yyyy";

    /**inicioMensagemPosDelete como exemplo: Produto excluído*/
    public static void addAlertDialogExcluir(final Context context, final Tabela objetoTabelaParaDeletar,
                                             final String inicioMensagemPosDelete, String mensagemTitulo, final Method metodoExecPosDelete, final Object objectLocalMetodo, final Object argsMetodo[]) {
        new AlertDialog.Builder(context).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SQLiteDatabaseDao dao = new SQLiteDatabaseDao(context);
                dao.deleteLogico(objetoTabelaParaDeletar);
                dao.close();
                Toast.makeText(context,  inicioMensagemPosDelete + " com sucesso!", Toast.LENGTH_SHORT).show();
                try {
                    metodoExecPosDelete.invoke(objectLocalMetodo,argsMetodo);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
            }
        }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setTitle(mensagemTitulo)
                .setIcon(R.drawable.icone_atencao).show();
    }

    public static void alteraViewVendaSelecionada() {
        Venda venda = VariaveisControleG.vendaSelecionada;
        if (venda.getCliente() != null) {
            if (venda.getCliente().getId() > 0) {
                String[] palavrasNome = venda.getCliente().getPessoa().getNome().split(" ");
                String nome = palavrasNome[0] + " " + palavrasNome[palavrasNome.length -1];
       //         VariaveisControleAndroid.textViewVendaSelectionada.setText("Venda: "+venda.getId()+"\nCliente: " + nome);
            } else {
            //    VariaveisControleAndroid.textViewVendaSelectionada.setText(  "Comanda selecionada: " + venda.getNumeroMesaComanda());
            }
        } else {
           // VariaveisControleAndroid.textViewVendaSelectionada.setText(      "Comanda selecionada: " + venda.getNumeroMesaComanda());
        }
    }


    public static void addCalendar(final AppCompatActivity appCompatActivity, final TextView textView){
        textView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                getCalendar(appCompatActivity,textView);
                usandoDatePickerDialog = true;
                return false;
            }
        });
    }

    private static void getCalendar(AppCompatActivity appCompatActivity,final TextView textView){
        if(!usandoDatePickerDialog) {
            Calendar calendar = Calendar.getInstance();
            DatePickerDialog datePickerDialog = new DatePickerDialog(appCompatActivity, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    textView.setText(FuncoesGerais.formataTextoData(dayOfMonth, month, year));
                    usandoDatePickerDialog = false;
                }

            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

           datePickerDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
               @Override
               public void onDismiss(DialogInterface dialog) {
                   usandoDatePickerDialog = false;
               }
           });
            datePickerDialog.show();
        }
    }


    public static void addAlertDialogAlerta(Context context, String mensagem){
        new AlertDialog.Builder(context).setTitle(mensagem)
                .setIcon(R.drawable.icone_atencao)
                .setCancelable(true)
                .setPositiveButton("Ok",null)
                .show();
    }

    public static void addAlertDialogErro(Context context, String titulo, String mensagem){
        new AlertDialog.Builder(context)
                .setTitle(titulo)
                .setMessage(mensagem)
                .setIcon(R.drawable.icone_erro)
                .setCancelable(true)
                .setPositiveButton("Fechar",null)
                .show();
    }

    public static void addSnackBarToast(View v, Context context, String mensagem){
        try {
            Snackbar.make(v, mensagem, Snackbar.LENGTH_LONG).show();
        }catch (Exception e){
            Toast.makeText(context,mensagem,Toast.LENGTH_SHORT).show();
        }
    }
}
