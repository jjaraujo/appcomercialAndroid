package br.com.jmdesenvolvimento.appcomercial.view.dialogFragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;

import br.com.jmdesenvolvimento.appcomercial.R;
import br.com.jmdesenvolvimento.appcomercial.controller.funcionaisAndroid.FuncoesViewAndroid;
import com.jmdesenvolvimento.appcomercial.controller.funcoesGerais.FuncoesGerais;
import br.com.jmdesenvolvimento.appcomercial.controller.funcionaisAndroid.VariaveisControleAndroid;
import com.jmdesenvolvimento.appcomercial.controller.VariaveisControleG;
import com.jmdesenvolvimento.appcomercial.model.entidades.estoque.Produto;

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
    private Produto produto;

    public DialogQuantidadeProduto( AdapterView<?> parent, int position, Produto produto) {

        this.parent = parent;
        this.position = position;
        modificarProduto = false;
        this.produto = produto;
    }
    /**Construtor deve ser usado somente em caso de alteração da quantidade do produto*/
    public DialogQuantidadeProduto(Produto produto) {

        modificarProduto = true;
        this.produto = produto;

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
        VariaveisControleAndroid.dialogQuantidadeProduto = this;

        textQtd.setText(VariaveisControleAndroid.qtdSelecionadaProdutoVenda +"");

        buttonAdicionarQtdProduto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qtd = FuncoesGerais.corrigeValoresCamposInt(textQtd.getText().toString());
                if (qtd <= produto.getQtd() || VariaveisControleG.configuracoesSimples.isVendaSemEstoque()) {
                    int qtdAnterior = VariaveisControleAndroid.qtdSelecionadaProdutoVenda; // para ser adicionado ao estoque em caso de mudança na quantidade do produto
                    VariaveisControleAndroid.qtdSelecionadaProdutoVenda = qtd;

                    if (modificarProduto == false) {
                        VariaveisControleAndroid.dialogEscolherEntidade.addProdutoNaVenda(parent, position);
                    } else {
                        VariaveisControleAndroid.produtosVendaActivity.alteraQtdProdutoVenda(qtdAnterior);
                    }
                    dismiss();
                } else {
                    FuncoesViewAndroid.addAlertDialogAlerta(getContext(), "Quantidade maior que o estoque!");
                }
            }
        });

        somar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qtdAtual = FuncoesGerais.corrigeValoresCamposInt(textQtd.getText().toString());
                textQtd.setText((qtdAtual + 1)+"");
            }
        });

        subtrair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qtdAtual = FuncoesGerais.corrigeValoresCamposInt(textQtd.getText().toString());
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
            textQtd.setText(VariaveisControleAndroid.qtdSelecionadaProdutoVenda + "");
            buttonAdicionarQtdProduto.setText("Modificar");
        }
        else {
            buttonAdicionarQtdProduto.setText("Adicionar");
            textQtd.setText("1");
        }
    }
}
