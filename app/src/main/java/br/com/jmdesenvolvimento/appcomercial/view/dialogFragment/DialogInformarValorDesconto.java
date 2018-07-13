package br.com.jmdesenvolvimento.appcomercial.view.dialogFragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import br.com.jmdesenvolvimento.appcomercial.R;;
import br.com.jmdesenvolvimento.appcomercial.controller.funcionaisAndroid.FuncoesViewAndroid;
import app.jm.funcional.controller.funcoesGerais.FuncoesGerais;
import app.jm.funcional.controller.funcoesGerais.FuncoesMatematicas;
import br.com.jmdesenvolvimento.appcomercial.controller.funcionaisAndroid.VariaveisControleAndroid;
import app.jm.funcional.controller.VariaveisControle;
import app.jm.funcional.model.tabelasIntermediarias.TabelaPagamento;


/**Dialog onde o usuário irá informar tipo de pagamento, parcelas etc.*/
@SuppressLint("ValidFragment")
public class DialogInformarValorDesconto extends DialogFragment {
    private  TabelaPagamento tabelaPagamento;
    private View view;
    private EditText textValor;
    private Button buttonAdicionar;
    private Button somar;
    private Button subtrair;
    private TextView textViewDesconto;
    private RadioButton radioButtonDescontPercentual;
    private RadioButton radioButtonDescontDinheiro;
    private double valorTotal;

    public DialogInformarValorDesconto(TextView textViewDesconto){
        this.textViewDesconto = textViewDesconto;
        this.valorTotal = FuncoesMatematicas.calculaValorTotalVendaDouble(VariaveisControle.vendaSelecionada);
    }


    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int theme = android.R.style.Theme_DeviceDefault_Dialog;
        setStyle(DialogFragment.STYLE_NO_TITLE, theme);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.dialog_fragment_valor_desconto, container, false);
        somar = view.findViewById(R.id.buttonDescontoSomar);
        subtrair = view.findViewById(R.id.buttonDescontoSubtrair);
        textValor = view.findViewById(R.id.editTexDesconto);
        buttonAdicionar = view.findViewById(R.id.buttonAdicionarDesconto);
        radioButtonDescontPercentual = view.findViewById(R.id.radioButtonDescontPercentual);
        radioButtonDescontDinheiro = view.findViewById(R.id.radioButtonDescontoDinheiro);

        buttonAdicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double valorDesconto = getValorDesconto() ;
                textViewDesconto.setText(("R$"+valorDesconto).replace(".",","));
                dismiss();
                VariaveisControle.vendaSelecionada.setDesconto(valorDesconto);
                VariaveisControleAndroid.activityPagamento.carregaTextValores();
            }
        });

        somar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qtdAtual = FuncoesGerais.corrigeValoresCamposInt(textValor.getText().toString());
                textValor.setText((qtdAtual + 5)+"");
            }
        });

        subtrair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qtdAtual = FuncoesGerais.corrigeValoresCamposInt(textValor.getText().toString());
                textValor.setText((qtdAtual - 5)+"");

                if(textValor.getText().toString().equals("0")){
                    textValor.setText("5");
                }
            }
        });
        return view;
    }

    private double getValorDesconto(){
        if(radioButtonDescontDinheiro.isChecked()){
            return Double.parseDouble(textValor.getText().toString());
        } else if(radioButtonDescontPercentual.isChecked()){
            double percentual = Double.parseDouble(textValor.getText().toString()) / 100;
            return valorTotal * percentual;
        } else{
            FuncoesViewAndroid.addAlertDialogAlerta(getContext(),"Selecione o tipo de desconto");
            return 0;
        }
    }

}
