package br.com.jmdesenvolvimento.appcomercial.controller.funcionais;

import android.app.DatePickerDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Set;

import br.com.jmdesenvolvimento.appcomercial.R;
import br.com.jmdesenvolvimento.appcomercial.model.Tabela;
import br.com.jmdesenvolvimento.appcomercial.model.dao.SQLiteDatabaseDao;
import br.com.jmdesenvolvimento.appcomercial.model.entidades.vendas.Venda;
import br.com.jmdesenvolvimento.appcomercial.view.activitys.entidades.estoque.CadastroProdutoActivity;

public class Funcoes {

    public static String corrigeValoresCampos(String s) {
        if (s == null) {
            return "null";
        } else {
            return s;
        }
    }

    public static String converteNuloToVazio(String s) {
        if (s == null) {
            return "";
        }
        return s;
    }

    public static int corrigeValoresCamposInt(String i) {
        i = Funcoes.removePontosTracos(i);
        if (i == null || i.equals("")) {
            return 0;
        } else {
            return Integer.parseInt(i);
        }
    }

    public static long corrigeValoresCamposLong(String i) {
        i = Funcoes.removePontosTracos(i);
        if (i == null || i.equals("")) {
            return 0;
        } else {
            return Long.parseLong(i);
        }
    }

    public static double corrigeValoresCamposDouble(String i) {
        if (i == null || i.equals("")) {
            return 0;
        } else {
            return Double.parseDouble(i);
        }
    }

    public static String getDataHojeDDMMAAAA() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        return sdf.format(date);
    }

    public static boolean somenteNumero(String query) {
        char[] ca = query.toCharArray();
        boolean d = false;
        for (char c : ca) {
            if (Character.isDigit(c) && !Character.isAlphabetic(c)) {
                d = true;
                break;
            } else {
                break;
            }
        }
        return d;
    }

    public static String removeCaracteresEspeciais(String string) {
        return string
                .replaceAll("[ãâàáä]", "a")
                .replaceAll("[êèéë&]", "e")
                .replaceAll("[îìíï]", "i")
                .replaceAll("[õôòóö]", "o")
                .replaceAll("[ûúùü]", "u")
                .replaceAll("[ÃÂÀÁÄ]", "A")
                .replaceAll("[ÊÈÉË]", "E")
                .replaceAll("[ÎÌÍÏ]", "I")
                .replaceAll("[ÕÔÒÓÖ]", "O")
                .replaceAll("[ÛÙÚÜ]", "U")
                .replace('ç', 'c')
                .replace('Ç', 'C')
                .replace('ñ', 'n')
                .replace('Ñ', 'N')
                ;
    }

    public static String removePontosTracos(String s) {
        return s.replace(".", "")
                .replace("-", "")
                .replace("_", "")
                .replace(",", "");
    }

    public static boolean fieldExtendsEntidade(Field f) {
        if (f.getType().getSuperclass() == null || f == null) {
            return false;
        }
        return f.getType().getSuperclass().toString().toLowerCase().trim().contains("entidade");
    }

    public static boolean objectExtendsEntidade(Object o) {
        if (o.getClass().getSuperclass() == null || o == null) {
            return false;
        }
        return o.getClass().getSuperclass().toString().toLowerCase().trim().contains("entidade");
    }

    public static String prefixoChaveEstrangeira() {
        return "_idFK";
    }


    public static String removeNullZeroFormularios(String s) {
        if(s == null){
            return " ";
        }
        String snovo = s.replace("null", "").replace("0.0", "").replace("0", "");
        if (snovo.equals("")) {
            return " ";
        } else {
            return s;
        }
    }

    public static Tabela getTabelaModificada(Tabela tabelaAntiga, Tabela tabelaAlterada, Tabela tabelaNova) {
        HashMap<String, Object> mapAntigo = tabelaAntiga.getMapAtributos();
        HashMap<String, Object> mapAlterado = tabelaAlterada.getMapAtributos();
        HashMap<String, Object> mapNovo = tabelaNova.getMapAtributos();

        Set<String> set = mapAntigo.keySet();
        for (String s : set) {
            if (!(mapAntigo.get(s) + "").equals(mapAlterado.get(s) + "")) {
                mapNovo.put(s, mapAlterado.get(s));
            }
        }
        mapNovo.put(tabelaAntiga.getIdNome(), tabelaAntiga.getId());
        tabelaNova.setMapAtributos(mapNovo);
        return tabelaNova;
    }
    /**Informe o inicio da mensaem como exemplo: Produto excluído*/
    public static void addAlertDialogExcluir(final Context context, final Tabela tabela, final String inicioMensagem, String mensagemTitulo) {
        new AlertDialog.Builder(context).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SQLiteDatabaseDao dao = new SQLiteDatabaseDao(context);
                dao.deleteLogico(tabela);
                dao.close();
                Toast.makeText(context,  inicioMensagem + " com sucesso!", Toast.LENGTH_SHORT).show();
                VariaveisControle.fragmentProdutos.carregaLista();
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

    public static void addAlertDialogAlerta(Context context, String mensagem){
        new AlertDialog.Builder(context).setTitle(mensagem)
                .setIcon(R.drawable.icone_atencao)
                .setCancelable(true)
                .setPositiveButton("Ok",null)
        .show();
    }

    public static void alteraViewVendaSelecionada() {
        Venda venda = VariaveisControle.vendaSelecionada;
        if (venda.getCliente() != null) {
            if (venda.getCliente().getId() > 0) {
                String[] palavrasNome = venda.getCliente().getPessoa().getNome().split(" ");
                String nome = palavrasNome[0] + " " + palavrasNome[palavrasNome.length -1];
                VariaveisControle.textViewVendaSelectionada.setText(
                        "Venda: "+venda.getId()+"\nCliente: " + nome);
            } else {
                VariaveisControle.textViewVendaSelectionada.setText(
                        "Comanda selecionada: " + venda.getNumeroMesaComanda());
            }
        } else { // a primeira vez que a venda é adiciona, o cliente é nulo. Após ser buscada no banco, haverá um cliente com id 0
            VariaveisControle.textViewVendaSelectionada.setText(
                    "Comanda selecionada: " + venda.getNumeroMesaComanda());
        }
        alteraValorVendaSelecionada();
    }

    public static void alteraValorVendaSelecionada() {
        Venda venda = VariaveisControle.vendaSelecionada;
        VariaveisControle.buttonValorTotal.setText("FINALIZAR \n\nR$ " + FuncoesMatematicas.calculaValorTotalVenda(venda));
    }

    public static String addZeros(int numero,int tamanhoTotal){
        String formatado = "" + numero ;
        while( tamanhoTotal > 0){
            formatado = "0" + formatado;
            tamanhoTotal --;
        }
        return formatado;
    }

    public static boolean intToBoolean(int i) {
        if (i == 1) {
            return true;
        }
        return false;
    }

    public static int booleanToint(boolean b) {
        if (b == true) {
            return 1;
        }
        return 0;
    }

    public static void addCalendarTeste(AppCompatActivity appCompatActivity, final TextView textView){
       Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(appCompatActivity, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String mes = month + "";
                String dia = dayOfMonth + "";
                if(month < 10){
                    mes = "0" + mes;
                }
                if(dayOfMonth < 10){
                    dia = "0" + dia;
                }
                textView.setText(dia+"/"+mes+"/"+year);
            }
        },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    public static void addCliqueToOpenCalendar(final AppCompatActivity appCompatActivity, final EditText editText){
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Funcoes.addCalendarTeste(appCompatActivity,editText);
            }
        });
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Funcoes.addCalendarTeste(appCompatActivity,editText);
            }
        });
    }

}
