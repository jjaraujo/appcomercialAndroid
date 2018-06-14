package br.com.jmdesenvolvimento.appcomercial.model.entidades.vendas;

import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.com.jmdesenvolvimento.appcomercial.R;
import br.com.jmdesenvolvimento.appcomercial.controller.funcionais.Funcoes;
import br.com.jmdesenvolvimento.appcomercial.model.Tabela;
import br.com.jmdesenvolvimento.appcomercial.model.entidades.Entidade;

public class TipoPagamentos extends Entidade{
    private String nome;
    private boolean aceito;
    private int tipoIcone;
    public final static int CARTAO = 1;
    public final static int CHEQUE = 2;
    public final static int DINHEIRO = 3;
    public final static int BOLETO = 4;
    public final static int FIADO = 5;



    public TipoPagamentos(String nome, int tipoIcone){
        this.nome = nome;
        this.tipoIcone = tipoIcone;
        aceito = true;
    }

    public TipoPagamentos(){

    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public boolean isAceito() {
        return aceito;
    }

    public void setAceito(boolean aceito) {
        this.aceito = aceito;
    }

    public int getIdIcone() {
        switch (tipoIcone){
            case CARTAO:
                return R.drawable.icone_cartao;
            case BOLETO:
                return  R.drawable.icone_boleto;
            case CHEQUE:
                return R.drawable.icone_cheque;
            case DINHEIRO:
                return R.drawable.icone_dinheiro;
            case FIADO:
                return R.drawable.icone_fiado;
        }
        return 0;
    }


    @Override
    public void setMapAtributos(HashMap<String, Object> map) {
        id = (int) map.get(getIdNome());
        nome = (String) map.get("nome");
        aceito =  Funcoes.intToBoolean((Integer) map.get("aceito"));
        tipoIcone = (int) map.get("tipoIcone");
    }

    @Override
    public boolean getPrecisaRegistroInicial() {
        return true;
    }

    @Override
    public List<Tabela> getListValoresIniciais() {
        //"R.drawable.icone_cartao"
        List<Tabela> list = new ArrayList<>();
        TipoPagamentos cartaoCredito = new TipoPagamentos("Cartão de Crédito",CARTAO);
        list.add(cartaoCredito);
        TipoPagamentos cartaoDebito = new TipoPagamentos("Cartão de Débito",CARTAO);
        list.add(cartaoDebito);
        TipoPagamentos dinheiro = new TipoPagamentos("Dinheiro",DINHEIRO);
        list.add(dinheiro);
        TipoPagamentos cheque = new TipoPagamentos("Cheque",CHEQUE);
        list.add(cheque);
        TipoPagamentos boleto = new TipoPagamentos("Boleto",BOLETO);
        list.add(boleto);
        TipoPagamentos alimentacao = new TipoPagamentos("Alimentação/Refeição",CARTAO);
        list.add(alimentacao);
        TipoPagamentos fiado = new TipoPagamentos("Fiado",FIADO);
        list.add(fiado);
        return list;
    }
}
