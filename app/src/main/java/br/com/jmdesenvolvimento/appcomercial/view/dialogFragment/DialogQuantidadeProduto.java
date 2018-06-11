package br.com.jmdesenvolvimento.appcomercial.view.dialogFragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;

import java.util.zip.Inflater;

import br.com.jmdesenvolvimento.appcomercial.R;
import br.com.jmdesenvolvimento.appcomercial.controller.funcionais.Funcoes;
import br.com.jmdesenvolvimento.appcomercial.controller.funcionais.VariaveisControle;
import br.com.jmdesenvolvimento.appcomercial.model.dao.ProdutoDAO;

@SuppressLint("ValidFragment")
public class DialogQuantidadeProduto extends DialogFragment {

    private View view;
    private Button buttonAdicionarQtdProduto;
    private Button somar;
    private Button subtrair;
    private EditText textQtd;
    private AdapterView<?> parent;
    private int position;
    private boolean modificarProduto = false;
    public static int DIALOG_QTDPRODUTO = 1;
    public static int DIALOG_COMANDA = 2;

    public DialogQuantidadeProduto( AdapterView<?> parent, int position) {

        this.parent = parent;
        this.position = position;
        modificarProduto = false;
    }
    /**Construtor deve ser usado somente em caso de alteração da quantidade do produto*/
    public DialogQuantidadeProduto() {

        modificarProduto = true;
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
        buttonAdicionarQtdProduto = view.findViewById(R.id.buttonAdicionarQtdProduto);
        somar = view.findViewById(R.id.buttonQtdProdutoSomar);
        subtrair = view.findViewById(R.id.buttonQtdProdutoSubtrair);
        textQtd = view.findViewById(R.id.editTextQtdProdutoVenda);
        buttonAdicionarQtdProduto = view.findViewById(R.id.buttonAdicionarQtdProduto);
        VariaveisControle.dialogQuantidadeProduto = this;

        textQtd.setText(VariaveisControle.qtdSelecionadaProdutoVenda +"");

        buttonAdicionarQtdProduto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qtdAnterior = VariaveisControle.qtdSelecionadaProdutoVenda; // para ser adicionado ao estoque em caso de mudança na quantidade do produto
                VariaveisControle.qtdSelecionadaProdutoVenda = Funcoes.corrigeValoresCamposInt(textQtd.getText().toString());

                if(modificarProduto == false) {
                    VariaveisControle.dialogEscolherEntidade.addProdutoNaVenda(parent, position);
                } else{
                    VariaveisControle.fragmentProdutos.alteraQtdProdutoVenda(qtdAnterior);
                }
                dismiss();
            }
        });

        somar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qtdAtual = Funcoes.corrigeValoresCamposInt(textQtd.getText().toString());
                textQtd.setText((qtdAtual + 1)+"");
            }
        });

        subtrair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qtdAtual = Funcoes.corrigeValoresCamposInt(textQtd.getText().toString());
                textQtd.setText((qtdAtual - 1)+"");

                if(textQtd.getText().toString().equals("0")){
                    textQtd.setText("1");
                }
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(modificarProduto) {
            textQtd.setText(VariaveisControle.qtdSelecionadaProdutoVenda + "");
            buttonAdicionarQtdProduto.setText("Modificar");
        }
        else {
            buttonAdicionarQtdProduto.setText("Adicionar");
            textQtd.setText("1");
        }
    }
}
