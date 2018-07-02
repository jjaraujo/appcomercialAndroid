package br.com.jmdesenvolvimento.appcomercial.view.dialogFragment;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import br.com.jmdesenvolvimento.appcomercial.R;
import com.jmdesenvolvimento.appcomercial.controller.funcoesGerais.FuncoesGerais;
import com.jmdesenvolvimento.appcomercial.model.tabelasIntermediarias.TabelaParcelasPagamento;
import com.jmdesenvolvimento.appcomercial.model.tabelasIntermediarias.TabelaPagamento;


/**Dialog onde o usuário irá informar tipo de pagamento, parcelas etc.*/
@SuppressLint("ValidFragment")
public class DialogAlterarValorParcela extends DialogFragment {
    private  TabelaPagamento tabelaPagamento;
    private View view;
    private EditText textValor;
    private Button buttonAdicionar;
    private Button somar;
    private Button subtrair;
    private TextView textViewParcela;
    private TabelaParcelasPagamento parcelasPagamentos;

    public DialogAlterarValorParcela(TextView textViewParcela, TabelaParcelasPagamento parcelasPagamentos){
        this.textViewParcela = textViewParcela;
        this.parcelasPagamentos = parcelasPagamentos;
    }

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                double valorParcela = Double.parseDouble(textValor.getText().toString());
                parcelasPagamentos.setValor(valorParcela);
                textViewParcela.setText(("R$"+valorParcela).replace(".",","));

                dismiss();
            }
        });

        somar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qtdAtual = FuncoesGerais.corrigeValoresCamposInt(textValor.getText().toString());
                textValor.setText((qtdAtual + 10)+"");
            }
        });

        subtrair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qtdAtual = FuncoesGerais.corrigeValoresCamposInt(textValor.getText().toString());
                textValor.setText((qtdAtual - 10)+"");

                if(textValor.getText().toString().equals("0")){
                    textValor.setText("1");
                }
            }
        });
        return view;
    }
}
