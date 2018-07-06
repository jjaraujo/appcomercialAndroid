package br.com.jmdesenvolvimento.appcomercial.view.dialogFragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import br.com.jmdesenvolvimento.appcomercial.R;
import br.com.jmdesenvolvimento.appcomercial.controller.funcionaisAndroid.FuncoesViewAndroid;
import com.jmdesenvolvimento.appcomercial.controller.funcoesGerais.FuncoesGerais;
import br.com.jmdesenvolvimento.appcomercial.controller.funcionaisAndroid.VariaveisControleAndroid;
import com.jmdesenvolvimento.appcomercial.controller.VariaveisControleG;
import com.jmdesenvolvimento.appcomercial.model.tabelasIntermediarias.TabelaParcelasPagamento;
import com.jmdesenvolvimento.appcomercial.model.tabelasIntermediarias.TabelaPagamento;
import br.com.jmdesenvolvimento.appcomercial.view.adapters.arraysAdapter.tabelas.ArrayAdapterDatasParcelas;


/**Dialog onde o usuário irá informar tipo de pagamento, parcelas etc.*/
@SuppressLint("ValidFragment")
public class DialogInformarValorPagamento extends android.support.v4.app.DialogFragment {
    private final TabelaPagamento tabelaPagamento;
    private TextView valorRestante;
    private TextView textViewOk;
    private TextView textViewCancelar;
    private TextInputEditText editTextValorRecebido;
    private TextInputEditText editTextParcelas;
    private TextInputEditText editTextDataPrimeiroPagamento;
    private TextInputEditText editTextIntervalo;
    private List<TabelaParcelasPagamento> listParcelasPagamentos = new ArrayList<>();;
    private ListView listViewParcelas;
    private int parcelas;
    private int intervalo = 0;
    private Calendar dataPrimeiraParcela;

    public DialogInformarValorPagamento(TextView valorRestante, TabelaPagamento tabelaPagamento){
        this.valorRestante = valorRestante;
        this.tabelaPagamento = tabelaPagamento;
    }


    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int theme;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_informar_valor_pagamento_parcelas, container, false);
        editTextIntervalo = view.findViewById(R.id.editTextInformarPagamentoIntervalo);
        editTextParcelas = view.findViewById(R.id.editTextInformarPagamentoParcelas);
        editTextDataPrimeiroPagamento = view.findViewById(R.id.editTextInformarPagamentoPrimeiroPagamento);
        FuncoesViewAndroid.addCalendar((AppCompatActivity) getActivity(),editTextDataPrimeiroPagamento);
        editTextValorRecebido = view.findViewById(R.id.editTextInformarPagamentoValorRecebido);
        textViewCancelar = view.findViewById(R.id.textViewCancelar);
        textViewOk = view.findViewById(R.id.textViewOk);
        listViewParcelas = view.findViewById(R.id.listViewParcelas);

        ouveCliqueElementos();

        return view;
    }

    private void ouveCliqueElementos(){

        textViewOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(valorRestante != null) {
                    double valorRecebido = Double.parseDouble(editTextValorRecebido.getText().toString());
                    VariaveisControleG.valorRestante =  VariaveisControleG.valorRestante - valorRecebido;
                    valorRestante.setText("R$ " + (VariaveisControleG.valorRestante+"").replace(".",","));
                    tabelaPagamento.setValor(valorRecebido);
                    tabelaPagamento.seTabelaParcelasPagamento(listParcelasPagamentos);
                    VariaveisControleG.vendaSelecionada.getTabelaPagamentos().add(tabelaPagamento);
                    VariaveisControleAndroid.activityPagamento.carregaListPagamentosEscolhidos();
                }
                dismiss();
            }
        });

        editTextValorRecebido.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().equals("") && s != null){
                    if(Double.parseDouble(s+"") > VariaveisControleG.valorRestante){
                        FuncoesViewAndroid.addAlertDialogAlerta(getContext(),"Valor informado maior que o restante!");
                        editTextValorRecebido.setText(VariaveisControleG.valorRestante +"");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().equals("") && s != null) {
                    tabelaPagamento.setValor(Double.parseDouble(s.toString()));
                } else{
                    tabelaPagamento.setValor(0);
                }
            }
        });

        editTextParcelas.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(tabelaPagamento.getValor() == 0){
                    FuncoesViewAndroid.addAlertDialogAlerta(getContext(),"Informe o valor do pagamento!");
                    return;
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().equals("") && s != null){
                    parcelas = Integer.parseInt(s.toString());
                } else{
                    parcelas = 0;
                }
                addListParcelas();
                carregaListParcelas();
            }
        });

        editTextIntervalo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(tabelaPagamento.getValor() == 0){
                    FuncoesViewAndroid.addAlertDialogAlerta(getContext(),"Informe o valor do pagamento!");
                    return;
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().equals("") && s != null){
                    intervalo = Integer.parseInt(s.toString());
                    addListParcelas();
                    carregaListParcelas();
                } else{
                    intervalo = 0;
                    addListParcelas();
                    carregaListParcelas();
                }

            }
        });

        editTextDataPrimeiroPagamento.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(tabelaPagamento.getValor() == 0){
                    FuncoesViewAndroid.addAlertDialogAlerta(getContext(),"Informe o valor do pagamento!");
                } else {
                    if(s.length() == 10) {
                        dataPrimeiraParcela = FuncoesGerais.stringToCalendar(s.toString(), FuncoesGerais.ddMMyyyy);
                        addListParcelas();
                        carregaListParcelas();
                    }
                }
            }
        });

        textViewCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listParcelasPagamentos.clear();
                dismiss();
            }
        });
    }

    private void addListParcelas(){
        listParcelasPagamentos.clear();
        double valorParcela = tabelaPagamento.getValor() / parcelas;
        Calendar calendar = Calendar.getInstance();

        if(dataPrimeiraParcela != null){
            calendar = dataPrimeiraParcela;
        }

        for(int i = 1; i <= parcelas; i++){
            TabelaParcelasPagamento parcelasPagamento = new TabelaParcelasPagamento();
            parcelasPagamento.setValor(valorParcela);
            parcelasPagamento.setNumeroParcela(i);
            Calendar data = (Calendar) calendar.clone();
            if(intervalo == 0){
                parcelasPagamento.setData(data);
                calendar.add(Calendar.DAY_OF_MONTH,30);
            } else{
                parcelasPagamento.setData(data);
                calendar.add(Calendar.DAY_OF_MONTH,intervalo);
            }
            listParcelasPagamentos.add(parcelasPagamento);
        }
    }

    private void carregaListParcelas(){
        ArrayAdapterDatasParcelas adapterTabelas = new ArrayAdapterDatasParcelas(getContext(),listParcelasPagamentos);
        listViewParcelas.setAdapter(adapterTabelas);
    }

}
