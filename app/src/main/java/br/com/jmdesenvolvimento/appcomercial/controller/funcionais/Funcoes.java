package br.com.jmdesenvolvimento.appcomercial.controller.funcionais;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import br.com.jmdesenvolvimento.appcomercial.R;
import br.com.jmdesenvolvimento.appcomercial.model.Tabela;
import br.com.jmdesenvolvimento.appcomercial.model.dao.SQLiteDatabaseDao;
import br.com.jmdesenvolvimento.appcomercial.model.entidades.Entidade;
import br.com.jmdesenvolvimento.appcomercial.model.entidades.estoque.Produto;
import br.com.jmdesenvolvimento.appcomercial.model.entidades.vendas.Venda;
import br.com.jmdesenvolvimento.appcomercial.model.tabelas.TabelaProdutosVenda;

public class Funcoes {

    public static String corrigeValoresCampos(String s) {
        if (s == null) {
            return "null";
        } else {
            return s;
        }
    }

    public static int corrigeValoresCamposInt(String i) {
        if (i == null || i.equals("")) {
            return 0;
        } else {
            return Integer.parseInt(i);
        }
    }

    public static long corrigeValoresCamposLong(String i) {
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
            } else{
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
        String snovo = s.replace("null", "").replace("0.0", "").replace("0", "");
        if (snovo.equals("")) {
            return " ";
        } else {
            return s;
        }
    }

    public static Object isNull(Tabela tabela, String metodo, Tabela tabelaNova) {
        try {
            if (tabela == null) {
                return tabelaNova;
            } else {

                Tabela t = (Tabela) Class.forName(tabela.getClass().getName()).newInstance();
                Field f = t.getClass().getDeclaredField(metodo);
                f.setAccessible(true);
                Object s = f.get(t);
                return s;
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
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
        mapNovo.put(tabelaAntiga.getIdNome(),tabelaAntiga.getId());
        tabelaNova.setMapAtributos(mapNovo);
        return tabelaNova;
    }

    public static void alertDialogExcluirEntidade(final Context context, final Tabela tabela, String mensagemTitulo){
        new AlertDialog.Builder(context).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SQLiteDatabaseDao dao = new SQLiteDatabaseDao(context);
                dao.deleteLogico(tabela);
                dao.close();
                Toast.makeText(context,tabela.getNomeTabela(false) + " excluída com sucesso!",Toast.LENGTH_SHORT).show();
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

    public static void alteraViewVendaSelecionada(){
        Venda venda = VariaveisControle.VENDA_SELECIONADA;
        VariaveisControle.vendaSelectionada.setText(
                "Venda selecionada: " +
                        venda.getCliente().getPessoa().toString());
        VariaveisControle.valorTotal.setText("R$ " + FuncoesMatematicas.calculaValorTotalVenda(venda));

    }

    public static void alteraValorVendaSelecionada(){
        Venda venda = VariaveisControle.VENDA_SELECIONADA;
        VariaveisControle.valorTotal.setText("R$ " + FuncoesMatematicas.calculaValorTotalVenda(venda));
    }

    public static boolean intToBoolean(int i){
        if(i == 1){
            return true;
        }
        return false;
    }

    public static int booleanToint(boolean b){
        if(b == true){
            return 1;
        }
        return 0;
    }


}
