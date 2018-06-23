package br.com.jmdesenvolvimento.appcomercial.controller.funcoesGerais;


import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Set;

import br.com.jmdesenvolvimento.appcomercial.model.Tabela;

/**Aqui ficam as funções que servem para Android e Java*/
public final class FuncoesGerais {

    private static boolean usandoDatePickerDialog;
    public static final String yyyyMMdd_HHMMSS = "yyyy-MM-dd hh:mm:ss";
    public static final String ddMMyyyy = "dd/MM/yyyy";
    public static final String yyyyMMdd = "yyyy-MM-dd";

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
        i = FuncoesGerais.removePontosTracos(i);
        if (i == null || i.equals("")) {
            return 0;
        } else {
            return Integer.parseInt(i);
        }
    }

    public static int corrigeValoresCamposInt(Object i) {
        if (i == null) {
            return 0;
        } else {
            return (int) i;
        }
    }

    public static long corrigeValoresCamposLong(String i) {
        i = FuncoesGerais.removePontosTracos(i);
        if (i == null || i.equals("")) {
            return 0;
        } else {
            return Long.parseLong(i);
        }
    }

    public static long corrigeValoresCamposLong(Object o) {
        if (o == null) {
            return 0;
        } else {
            return (long) o;
        }
    }

    public static Calendar corrigeValoresCalendar(Object o, String formato) {
        if (o == null) {
            return Calendar.getInstance();
        } else {
            return stringToCalendar(o+"", formato);
        }
    }

    public static double corrigeValoresCamposDouble(String i) {
        if (i == null || i.equals("")) {
            return 0;
        } else {
            return Double.parseDouble(i);
        }
    }

    public static double corrigeValoresCamposDouble(Object i) {
        if (i == null) {
            return 0.0;
        } else {
            return (double) i;
        }
    }

    public static boolean stringIsSomenteNumero(String query) {
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

    public static boolean isTabela(Object o) {
        try{
            Tabela t = (Tabela) o;
            return true;
        } catch (ClassCastException e){
            return false;
        }
    }

//    public static String prefixoChaveEstrangeira() {
//        return "_idFK";
//    }

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
        HashMap<String, Object> mapAntigo = tabelaAntiga.getMapAtributos(false);
        HashMap<String, Object> mapAlterado = tabelaAlterada.getMapAtributos(false);
        HashMap<String, Object> mapNovo = tabelaNova.getMapAtributos(false);

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

    public static String addZeros(int numero,int tamanhoTotal){
        String formatado = "" + numero ;
        while( tamanhoTotal-1 > 0){
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


    public static String formataTextoData(int dia, int mes, int ano){
        mes = mes + 1;
        String mesString = mes + "";
        String diaString = dia + "";
        if(mes < 10){
            mesString = "0" + mesString;
        }
        if(dia < 10){
            diaString = "0" + diaString;
        }
       return diaString+"/"+mesString+"/"+ano;
    }

   
    /**Usar um dos formados declarados nesta classe
     * Retorna null se valor == null ou valor == ""*/
    public static Calendar stringToCalendar(String valor,String formato){
        if(valor == null || valor.equals("")){
            return null;
        }
        try {
            SimpleDateFormat fd = new SimpleDateFormat(formato);
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(fd.parse(valor));
            return calendar;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String calendarToString(Calendar calendar, String formato, boolean paraSql){
        if(calendar == null || calendar.getTimeInMillis() == 0 ){
            return null;
        }
            SimpleDateFormat fd = new SimpleDateFormat(formato);
        String data= "'"+ fd.format(calendar.getTime()) + "'";
        return paraSql == false ? data.replace("'","") : data;
    }

    /**Pega field de uma tabela pelo nome*/
    public static Object getFieldDeTabela(String nomeAtributo, Tabela tabela) {
        try {

            Object[] o = verificaSeEhFieldTabelaMain(nomeAtributo, tabela);
            boolean boo = (boolean) o[0];
            if(boo == true){
                return o[1];
            }

            Class classe = tabela.getClass();
            Field f = classe.getDeclaredField(nomeAtributo);
            f.setAccessible(true);
            return f.get(tabela);

        } catch (NoSuchFieldException e) {
            Log.e("NoSuchFieldException", "Não foi possível encontrar o field " + nomeAtributo + " da tabela " + tabela.getNomeTabela(false));
            e.printStackTrace();
            return  null;
        } catch (IllegalAccessException e) {
            Log.e("IllegalAccessException", "Não foi possível encontrar o objeto " + nomeAtributo + " da tabela " + tabela.getNomeTabela(false));
            e.printStackTrace();
            return null;
        }
    }

    public static Object[] verificaSeEhFieldTabelaMain(String string, Tabela tabela) {

        if(FuncoesGerais.isTabela(tabela.getMapAtributos(true).get(string)))
            return new Object[]{true,0};
        if(string.contains(tabela.prefixoDataExclusao()))
            return new Object[]{true,""};
        if(string.contains(getPrefixoPK()))
            return new Object[]{true,0};

        return new Object[]{false,null};
    }

    public static String getPrefixoPK(){
        return "_pk";
    }

    public static Tabela getNovaInstanciaTabela(Field field){
       try {
           Type type = field.getType();
           String nomeType = type.toString().replace("class ", "");
           Tabela tabela = (Tabela) Class.forName(nomeType).newInstance();

           return tabela;

       }catch (ClassNotFoundException e){
           e.printStackTrace();
       } catch (IllegalAccessException e) {
           e.printStackTrace();
       } catch (InstantiationException e) {
           e.printStackTrace();
       }
       return null;
    }

    public static Tabela getFieldTypeTabela(Tabela objectTabela, Field field){
        try {
            Tabela tabela = getNovaInstanciaTabela(field);
            Field tabelaField = objectTabela.getClass().getDeclaredField(tabela.getNomeTabela(true).trim());
            tabelaField.setAccessible(true);

            return (Tabela) tabelaField.get(objectTabela);

        }catch (NoSuchFieldException e) {
            Log.i("NoSuchFieldException", "Erro ao buscar atributo " + field.getName() + " na TABELA " + objectTabela.getNomeTabela(false));
            e.printStackTrace();
        } catch (NullPointerException e) {
            Log.i("NullPointerException", "Erro ao buscar atributo " + field.getName() + " - TABELA " + objectTabela.getNomeTabela(false));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Calendar getCalendarNulo(){
        Calendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(0);
        return calendar;
    }

    public static boolean calendarIsNulo(Calendar calendar){
        return calendar.getTimeInMillis() == 0;
    }


    public static boolean classIsFinal(Object o){
        return Modifier.isFinal(o.getClass().getModifiers());
    }
}
