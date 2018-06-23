package br.com.jmdesenvolvimento.appcomercial.model.entidades.vendas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.com.jmdesenvolvimento.appcomercial.R;
import br.com.jmdesenvolvimento.appcomercial.controller.funcoesGerais.FuncoesGerais;
import br.com.jmdesenvolvimento.appcomercial.model.Tabela;
import br.com.jmdesenvolvimento.appcomercial.model.entidades.Entidade;

public class TipoPagamento extends Entidade{
    private String nome;
    private boolean aceito;
    private int tipoIcone;
    private boolean aceitaParcela;
    public final static int CARTAO = 1;
    public final static int CHEQUE = 2;
    public final static int DINHEIRO = 3;
    public final static int BOLETO = 4;
    public final static int FIADO = 5;

    public TipoPagamento(String nome, int tipoIcone, boolean aceitaParcela){
        this.nome = nome;
        this.tipoIcone = tipoIcone;
        aceito = true;
        this.aceitaParcela = aceitaParcela;
    }

    public TipoPagamento(){
        switch (tipoIcone){
            case CARTAO:
                tipoIcone = R.drawable.icone_cartao;
            case BOLETO:
                tipoIcone = R.drawable.icone_boleto;
                aceitaParcela = true;
            case CHEQUE:
                tipoIcone = R.drawable.icone_cheque;
                aceitaParcela = true;
            case DINHEIRO:
                tipoIcone = R.drawable.icone_dinheiro;
            case FIADO:
                tipoIcone = R.drawable.icone_fiado;
                aceitaParcela = true;
        }
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

    public Integer getIdIcone() {
        return tipoIcone;
    }

    public boolean isAceitaParcela() {
        return aceitaParcela;
    }

    public void setAceitaParcela(boolean aceitaParcela) {
        this.aceitaParcela = aceitaParcela;
    }

    @Override
    public void setMapAtributos(HashMap<String, Object> map) {
        id = (int) map.get(getIdNome());
        nome = (String) map.get("nome");
        aceito = (boolean) map.get("aceito");
        tipoIcone = (int) map.get("tipoIcone");
        aceitaParcela = (boolean) map.get("aceitaParcela");
    }

    @Override
    public boolean getPrecisaRegistroInicial() {
        return true;
    }

    @Override
    public List<Tabela> getListValoresIniciais() {
        //"R.drawable.icone_cartao"
        List<Tabela> list = new ArrayList<>();
        TipoPagamento cartaoCredito = new TipoPagamento("Cartão de Crédito",CARTAO,false);
        list.add(cartaoCredito);
        TipoPagamento cartaoDebito = new TipoPagamento("Cartão de Débito",CARTAO,false);
        list.add(cartaoDebito);
        TipoPagamento dinheiro = new TipoPagamento("Dinheiro",DINHEIRO,false);
        list.add(dinheiro);
        TipoPagamento cheque = new TipoPagamento("Cheque",CHEQUE,true);
        list.add(cheque);
        TipoPagamento boleto = new TipoPagamento("Boleto",BOLETO,true);
        list.add(boleto);
        TipoPagamento alimentacao = new TipoPagamento("Alimentação/Refeição",CARTAO,false);
        list.add(alimentacao);
        TipoPagamento fiado = new TipoPagamento("Fiado",FIADO,true);
        list.add(fiado);
        return list;
    }
}
