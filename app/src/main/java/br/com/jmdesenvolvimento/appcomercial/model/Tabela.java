package br.com.jmdesenvolvimento.appcomercial.model;

import android.util.Log;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

import br.com.jmdesenvolvimento.appcomercial.controller.funcionais.Funcoes;
import br.com.jmdesenvolvimento.appcomercial.model.entidades.Entidade;

public abstract class Tabela implements Serializable {

    public abstract void setMapAtributos(HashMap<String, Object> map);

    private HashMap<String, Object> map;

    protected int id;

    protected String dataExclusao;

    public  boolean getPrecisaRegistroInicial(){
        return false;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDataExclusao() {
        return dataExclusao;
    }

    public void setDataExclusao(String dataExclusao) {
        this.dataExclusao = dataExclusao;
    }

    public String getNomeTabela(boolean minusculo) {
        String nome = this.getClass().getSimpleName();
        if (minusculo) {
            return nome.substring(0, 1).toLowerCase().concat(nome.substring(1));
        }
        return nome;
    }

    public HashMap<String, Object> getMapAtributos() {
        // retornará o map da memória se ele já tiver sido criado uma vez
        if (map != null) {
            return map;
        }

        map = new HashMap<>();
        Field[] fields = getClass().getDeclaredFields();

        //caso o id da tabela tenha valor > 0, será adicionado no map o valor atual
        if (getId() == null) {
            map.put(getIdNome(), 0);
        } else
            map.put(getIdNome(), getId());

        // caso a dataExclusao nao tenha nenhum valor, será criado um valor vazio
        if (getDataExclusao() == null) {
            map.put(getDataExclusaoNome(), "");
        } else {
            map.put(getDataExclusaoNome(), getDataExclusao());
        }

        for (Field field : fields) {
            field.setAccessible(true);

            if (field.getName().trim().contains("$change") || field.getName().trim().contains("serialVersionUID")) {
                continue;
            }

            Type type = field.getType();
            String nomeTipoDoField = type.toString().replace("class ", "");
            try {
                // caso o field seja uma entidade, será adicionado o _id na chave no map para ficar igual ao banco
                if (Funcoes.fieldExtendsEntidade(field)) {
                    try {
                        Entidade entidade = (Entidade) Class.forName(nomeTipoDoField).newInstance();
                        Field entidadeField = this.getClass().getDeclaredField(entidade.getNomeTabela(true).trim());
                        entidadeField.setAccessible(true);

                        if (entidadeField.get(this) == null) {
                            map.put(field.getName() + Funcoes.prefixoChaveEstrangeira(), entidade);
                        } else {
                            map.put(field.getName() + Funcoes.prefixoChaveEstrangeira(), entidadeField.get(this));
                        }
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    } catch (NoSuchFieldException e) {
                        Log.i("NoSuchFieldException", "Erro ao buscar atributo " + field.getName() + " - tabela " + this.getNomeTabela(false));
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        Log.i("NullPointerException", "Erro ao buscar atributo " + field.getName() + " - tabela " + this.getNomeTabela(false));
                    }
                } else {
                    if (nomeTipoDoField.toLowerCase().contains("boolean")) {

                        map.put(field.getName(), Funcoes.booleanToint(field.getBoolean(this)));
                    } else if (field.get(this) == null) {
                        if (nomeTipoDoField.toLowerCase().contains("string")) {
                            map.put(field.getName(), "");
                        } else if (nomeTipoDoField.toLowerCase().contains("list")) {
                            map.put(field.getName(), new ArrayList<>());
                        } else {
                            map.put(field.getName(), 0);
                        }
                    } else {
                        map.put(field.getName(), field.get(this));
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    public String getIdNome() {
        return "id_" + getNomeTabela(true) + "_pk";
    }

    public String getDataExclusaoNome() {
        return "dataExclusao_" + getNomeTabela(true);
    }


    public boolean isEntidade() {
        return this.getClass().getSuperclass().getSimpleName().toLowerCase().contains("entidade");
    }

    public ArrayList<String> nomesAtibutosList() {
        Field[] fields = this.getClass().getDeclaredFields();
        ArrayList<String> list = new ArrayList<String>();
        list.add(getIdNome());
        list.add(getDataExclusaoNome());

        for (int i = 0; i < fields.length; i++) {

            String nomeField = fields[i].getName();
            Type type = fields[i].getType();
            String nomeTipo = type.toString().replace("class ", "").toLowerCase();

            if (nomeField.trim().equals("$change") || nomeField.trim().equals("serialVersionUID") || nomeTipo.contains("list")) {
                continue;
            }
            Field f = fields[i];
            if (Funcoes.fieldExtendsEntidade(f)) {
                nomeField += Funcoes.prefixoChaveEstrangeira();
            }
            list.add(nomeField);
        }
        return list;
    }

    public String[] nomesAtibutos() {
        ArrayList<String> list = nomesAtibutosList();
        String array[] = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            array[i] = list.get(i);
        }
        return array;
    }

    public String nomesAtibutosInLinha() {
        ArrayList<String> list = nomesAtibutosList();
        String array = list.get(0);
        for (int i = 1; i < list.size(); i++) {
            array += "," + list.get(i);
        }
        return array;
    }
}
