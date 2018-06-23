package br.com.jmdesenvolvimento.appcomercial.model;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import br.com.jmdesenvolvimento.appcomercial.controller.funcoesGerais.FuncoesGerais;
import br.com.jmdesenvolvimento.appcomercial.controller.funcoesGerais.FuncoesMatematicas;
import br.com.jmdesenvolvimento.appcomercial.controller.funcoesGerais.VerificaTipos;
import br.com.jmdesenvolvimento.appcomercial.model.entidades.Entidade;

public abstract class Tabela implements Serializable {

    public abstract void setMapAtributos(HashMap<String, Object> map);

    /**
     * Informar instancias da tabela que contém valores a serem inseridos no banco
     */
    public abstract List<Tabela> getListValoresIniciais();

    private HashMap<String, Object> map;

    protected int id;

    protected Calendar dataExclusao;

    public boolean getPrecisaRegistroInicial() {
        return false;
    }

    public int getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Calendar getDataExclusao() {
        return dataExclusao;
    }

    public void setDataExclusao(Calendar dataExclusao) {
        this.dataExclusao = dataExclusao;
    }

    public String getNomeTabela(boolean minusculo) {
        String nome = this.getClass().getSimpleName();
        if (minusculo) {
            return nome.substring(0, 1).toLowerCase().concat(nome.substring(1));
        }
        return nome;
    }

    public HashMap<String, Object> getMapAtributos(boolean carregaMapNovamente) {

        // retornará o map da memória se ele já tiver sido criado uma vez
        if (map != null && carregaMapNovamente == false) {
            return map;
        }

        map = new HashMap<>();
        Field[] fields = getClass().getDeclaredFields();

        //caso o id da tabela tenha valor > 0, será adicionado no map o valor atual
        int id = getId() == 0 ? 0 : getId();
        map.put(getIdNome(), id);

        // caso a dataExclusao nao tenha nenhum valor, será criado um valor vazio
        Calendar dataExclusao = getDataExclusao() == null ? FuncoesGerais.getCalendarNulo() : getDataExclusao();
        map.put(getDataExclusaoNome(), dataExclusao);

        for (Field field : fields) {
            field.setAccessible(true);
//
            //verifica se a variável é estática
            int modifiers = field.getModifiers();
            if (Modifier.isStatic(modifiers)) {
                continue;
            }

            try {
                Object objectField = null;
                try {
                    objectField = !FuncoesGerais.classIsFinal(field.getType()) ? field.getType().newInstance() : null;
                } catch (InstantiationException e) {
                    // variaveis que nao podem ser instanciadas
                        objectField = field.getType().getName().toLowerCase().contains("boolean") ? field.get(this) : null;
                   if(objectField == null)
                        e.printStackTrace();
                }

                // caso o field seja uma entidade, será adicionado o  na chave no map para ficar igual ao banco
                if (VerificaTipos.isTabela(field, this)) {

                    Tabela object = FuncoesGerais.getFieldTypeTabela(this, field) == null ?
                            FuncoesGerais.getNovaInstanciaTabela(field) :
                            FuncoesGerais.getFieldTypeTabela(this, field);
                    object.getMapAtributos(false);
                    map.put(field.getName(),object);// + FuncoesGerais.prefixoChaveEstrangeira(), object);

                } else {
                    if (field.get(this) == null) { // neste IF, valores que podem estar nulos
                        if (VerificaTipos.isBoolean(objectField, field)) {
                            map.put(field.getName(), FuncoesGerais.booleanToint(field.getBoolean(this)));
                        } else if (VerificaTipos.isString(objectField, field)) {
                            map.put(field.getName(), "");
                        } else if (VerificaTipos.isList(objectField, field)) {
                            map.put(field.getName(), new ArrayList<>());
                        } else if (VerificaTipos.isCalendar(objectField, field)) {
                            map.put(field.getName(), FuncoesGerais.getCalendarNulo());
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
        return "id_" + getNomeTabela(true) + FuncoesGerais.getPrefixoPK();
    }

    public String getDataExclusaoNome() {
        return "dataExclusao_" + getNomeTabela(false);
    }

    public String prefixoDataExclusao() {
        return "dataExclusao_";
    }


    public boolean isEntidade() {
        try {
            Entidade e = (Entidade) this;
            return true;
        } catch (ClassCastException e) {
            return false;
        }
    }

    public String nomesAtibutosInLinha() {
        Set<String> set = getMapAtributos(false).keySet();
        String array = "";
        for (String s : set) {
            array += ", " + s;
        }
        return array;
    }

    public String[] getNomesAtributos() {
        Object[] o = getMapAtributos(false).keySet().toArray();
        String[] strings = new String[o.length];
        for (int i = 0; i < o.length; i++) {
            strings[i] = o[i] + "";
        }
        return strings;
    }


}
