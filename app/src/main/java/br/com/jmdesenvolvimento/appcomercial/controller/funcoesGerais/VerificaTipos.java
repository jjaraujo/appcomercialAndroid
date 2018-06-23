package br.com.jmdesenvolvimento.appcomercial.controller.funcoesGerais;

import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.List;

import br.com.jmdesenvolvimento.appcomercial.model.Tabela;

public final class VerificaTipos {

    public static  boolean isString(Object object, Field field){
        try{
            String string = (String) object;
            if(string == null)
                string.contains("");
            return true;
        }catch (ClassCastException e){
           return false;
        }catch (NullPointerException e){
            return field.toString().toLowerCase().contains("string");
        }
    }

    public static  boolean isTabela(Field field, Tabela tabela){
        try{
            Tabela t = (Tabela) field.get(tabela);
                if(t == null){
                    Type type = field.getType();
                    String nome = type.toString().replace("class ","");
                    Object o = Class.forName(nome).newInstance();
                    Tabela t1 = (Tabela) o;
                   t1.getId();
                }
            return true;
        }catch (ClassCastException e){
            return false;
        }catch (IllegalAccessException e) {
            return false;
        }catch (NullPointerException e) {
            return false;
        } catch (InstantiationException e) {
            return false;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    public static boolean isCalendar(Object object, Field field){
        try{
            Calendar calendar = (Calendar) object;
            return true;
        }catch (ClassCastException e){
            return false;
        } catch (NullPointerException e){
            return field.toString().toLowerCase().contains("calendar");
        }
    }

    public static boolean isList(Object object, Field field) {
        try {
            List<Tabela> calendar = (List<Tabela>) object;
            calendar.size();
            return true;
        } catch (ClassCastException e) {
            return false;
        } catch (NullPointerException e) {
            return field.toString().toLowerCase().contains("list");
        }
    }


    public static boolean isInt(Object object, Field field){
        try{
            int integer = (int) object;
            return true;
        }catch (ClassCastException e){
            return false;
        } catch (NullPointerException e){
            return field.toString().toLowerCase().contains("int");
        }
    }

    public static boolean isLong(Object object, Field field){
        try{
            long integer = (long) object;
            return true;
        }catch (ClassCastException e){
            return false;
        } catch (NullPointerException e){
            return field.toString().toLowerCase().contains("long");
        }
    }

    public static boolean isBoolean(Object object, Field field){
        try{
            boolean boo = (boolean) object;
            return true;
        }catch (ClassCastException e){
            return false;
        } catch (NullPointerException e){
           return field.toString().toLowerCase().contains("boolean");
        }
    }

    public static boolean isDouble(Object object, Field field){
        try{
            double dou = (double) object;
            return true;
        }catch (ClassCastException e){
            return false;
        } catch (NullPointerException e){
            return field.toString().toLowerCase().contains("double");
        }
    }

    public static boolean isText(Object object, Field field){
        return isCalendar(object, field) || isString(object, field);
    }

    public static boolean isInteger(Object object, Field field){
        return isBoolean(object, field) || isInt(object, field) || isLong(object, field);
    }

    public static boolean isReal(Object object, Field field){
        return isDouble(object, field);
    }
}
