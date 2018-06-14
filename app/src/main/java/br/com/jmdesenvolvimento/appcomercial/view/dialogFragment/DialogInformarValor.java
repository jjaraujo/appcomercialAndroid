package br.com.jmdesenvolvimento.appcomercial.view.dialogFragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import br.com.jmdesenvolvimento.appcomercial.R;
import br.com.jmdesenvolvimento.appcomercial.controller.funcionais.Funcoes;
import br.com.jmdesenvolvimento.appcomercial.controller.funcionais.VariaveisControle;
import br.com.jmdesenvolvimento.appcomercial.model.entidades.vendas.TipoPagamentos;
import br.com.jmdesenvolvimento.appcomercial.model.tabelas.TabelaPagamento;

@SuppressLint("ValidFragment")
public class DialogInformarValor extends DialogFragment {
    private final TabelaPagamento tabelaPagamento;
    private View view;
    private EditText textValor;
    private Button buttonAdicionar;
    private Button somar;
    private Button subtrair;
    private TextView valorRestante;
    private TextView valorDesconto;

    public DialogInformarValor(TextView valorRestante, TextView valorDesconto, TabelaPagamento tabelaPagamento){
        this.valorRestante = valorRestante;
        this.valorDesconto = valorDesconto;
        this.tabelaPagamento = tabelaPagamento;
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

        view = inflater.inflate(R.layout.dialog_fragment_qtd_produto_venda, container, false);
        buttonAdicionar = view.findViewById(R.id.buttonAdicionarQtdProduto);
        somar = view.findViewById(R.id.buttonQtdProdutoSomar);
        subtrair = view.findViewById(R.id.buttonQtdProdutoSubtrair);
        textValor = view.findViewById(R.id.editTextQtdProdutoVenda);
        buttonAdicionar = view.findViewById(R.id.buttonAdicionarQtdProduto);

        buttonAdicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(valorDesconto == null) { // se valorDesconto == null, o dialogo é referente ao valor de um pagamento
                 String valorStirng =   valorRestante.getText().toString().replace("R$","").replace(" ","").replace(",",".");
                 double valor = Double.parseDouble(valorStirng) - Double.parseDouble(textValor.getText().toString());
                 valorRestante.setText("R$ " + (valor+"").replace(".",","));
                 tabelaPagamento.setValor(valor);
                } else{
                    String valorStirng =   valorRestante.getText().toString().replace("R$","").replace(" ","").replace(",",".");
                    String valorDescontoString = textValor.getText().toString();
                    double valor = Double.parseDouble(valorStirng) - Double.parseDouble(valorDescontoString.replace(",","."));
                    valorRestante.setText("R$ " + (valor+"").replace(".",","));
                    valorDesconto.setText("R$ " + valorDescontoString);
                    VariaveisControle.vendaSelecionada.setDesconto(valor);
                }
                dismiss();
            }
        });

        somar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qtdAtual = Funcoes.corrigeValoresCamposInt(textValor.getText().toString());
                textValor.setText((qtdAtual + 1)+"");
            }
        });

        subtrair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qtdAtual = Funcoes.corrigeValoresCamposInt(textValor.getText().toString());
                textValor.setText((qtdAtual - 1)+"");

                if(textValor.getText().toString().equals("0")){
                    textValor.setText("1");
                }
            }
        });
        return view;
    }
}
