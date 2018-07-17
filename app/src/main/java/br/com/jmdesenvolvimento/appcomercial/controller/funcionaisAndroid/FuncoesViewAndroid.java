package br.com.jmdesenvolvimento.appcomercial.controller.funcionaisAndroid;

import android.app.Activity;
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

import br.com.jmdesenvolvimento.appcomercial.R;;
import app.jm.funcional.controller.funcoesGerais.FuncoesGerais;
import app.jm.funcional.controller.VariaveisControle;
import app.jm.funcional.model.Tabela;
import br.com.jmdesenvolvimento.appcomercial.model.dao.SQLiteDatabaseDao;
import br.com.jmdesenvolvimento.appcomercial.view.activitys.TrataOnBackPressed;
import br.com.jmdesenvolvimento.appcomercial.view.activitys.iniciais.CaixaActivity;
import br.com.jmdesenvolvimento.appcomercial.view.activitys.iniciais.VendasAbertasActivity;

import app.jm.funcional.model.entidades.vendas.Venda;

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

    public static void addAlertDialogPerguntaSair(final TrataOnBackPressed context, String mensagem, boolean apagaVenda) {
        new AlertDialog.Builder(context).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                VariaveisControle.vendaSelecionada.setDesconto(0);
                context.onBackPressedSuper();
                dialog.dismiss();
                if(apagaVenda){
                    VariaveisControle.vendaSelecionada = null;
                }

            }
        }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setTitle("Deseja sair?")
                .setMessage(mensagem)
                .setIcon(R.drawable.icone_atencao).show();
    }

    public static void alteraViewVendaSelecionada() {
        Venda venda = VariaveisControle.vendaSelecionada;
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


    public static void addAlertDialogAlerta(Context context, String titulo, String mensagem){
        new AlertDialog.Builder(context).setTitle(titulo)
                .setMessage(mensagem)
                .setIcon(R.drawable.icone_atencao)
                .setCancelable(true)
                .setPositiveButton("Ok",null)
                .show();
    }

    public static void addAlertDialogSucesso(Context context, String titulo, String mensagem){
        new AlertDialog.Builder(context).setTitle(titulo)
                .setMessage(mensagem)
                .setIcon(R.drawable.icone_salvar)
                .setCancelable(true)
                .setPositiveButton("Ok",null)
                .show();
    }


    /**Adiciona um alertDialog com icone de erro
     * @param activity - Informe a activity que onde será exibida o alert
     * @param titulo - Informe o título do dialogo
     * @param mensagem - Informe o titulo da mensagem
     * @param encerrarActivity - Informar true caso queira encerrar a @activity */
    public static AlertDialog addAlertDialogErro(AppCompatActivity activity, String titulo, String mensagem, boolean encerrarActivity){
       return new AlertDialog.Builder(activity)
                .setTitle(titulo)
                .setMessage(mensagem)
                .setIcon(R.drawable.icone_erro)
                .setCancelable(true)
                .setPositiveButton("Fechar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(encerrarActivity){
                            activity.finish();
                        }
                    }
                }).create();

    }

    public static AlertDialog addAlertaSemInternet(AppCompatActivity appCompatActivity,boolean encerrarActivity){
       return addAlertDialogErro(appCompatActivity,"Falha na conexão",
                "Parece que você não está conectado à Internet. Verifique sua conexão e tente novamente",
                encerrarActivity);
    }

    public static AlertDialog addAlertaErroServidor(AppCompatActivity appCompatActivity,boolean encerrarActivity){
        return addAlertDialogErro(appCompatActivity, "Falha na conexão",
                "Nosso sistema não está respondendo. Tente novamente em instantes e caso o problema persista, entre em contato com o suporte!", encerrarActivity);

    }

    public static void addSnackBarToast(View v, Context context, String mensagem){
        try {
            Snackbar.make(v, mensagem, Snackbar.LENGTH_LONG).show();
        }catch (Exception e){
            Toast.makeText(context,mensagem,Toast.LENGTH_SHORT).show();
        }
    }

    public static Class  getActivityInicial(){
        String caminho = "br.com.jmdesenvolvimento.appcomercial.view.activitys.iniciais.";
        String nome = VariaveisControle.configuracoesSimples.getActivityInicial();
        try {
            return Class.forName(caminho+nome).getClass();
        } catch (ClassNotFoundException e) {
            //      e.printStackTrace();
            return CaixaActivity.class;
        }
    }
}
