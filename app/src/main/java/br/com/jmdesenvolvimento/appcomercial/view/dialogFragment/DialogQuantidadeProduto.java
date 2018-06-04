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

import br.com.jmdesenvolvimento.appcomercial.R;
import br.com.jmdesenvolvimento.appcomercial.controller.funcionais.Funcoes;
import br.com.jmdesenvolvimento.appcomercial.controller.funcionais.VariaveisControle;

@SuppressLint("ValidFragment")
public class DialogQuantidadeProduto extends DialogFragment {

    private int numStyle;
    private int theme;
    private View view;
    private Button buttonAdicionarQtdProduto;
    private Button somar;
    private Button subtrair;
    private EditText textQtd;
    private AdapterView<?> parent;
    private int position;
    private boolean modificarProduto = false;


    public DialogQuantidadeProduto(int nulStyle, int theme, AdapterView<?> parent, int position) {
        this.numStyle = nulStyle;
        this.theme = theme;
        this.parent = parent;
        this.position = position;
        modificarProduto = false;
    }

    public DialogQuantidadeProduto(int nulStyle, int theme) {
        this.numStyle = nulStyle;
        this.theme = theme;
        this.parent = parent;
        this.position = position;
        modificarProduto = true;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int style;
        int theme;

        switch (numStyle) {
            case 1:
                style = DialogFragment.STYLE_NO_TITLE;
                break;
            case 2:
                style = DialogFragment.STYLE_NO_FRAME;
                break;
            case 3:
                style = DialogFragment.STYLE_NO_INPUT;
                break;
            default:
                style = DialogFragment.STYLE_NORMAL;
                break;
        }

        switch (this.theme) {
            case 1:
                theme = android.R.style.Theme_Holo_Light;
                break;
            case 2:
                theme = android.R.style.Theme_Holo_Dialog;
                break;
            default:
                theme = android.R.style.Theme_Holo_Light_DarkActionBar;
                break;
        }
        setStyle(style, theme);
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

        if(modificarProduto) {
            textQtd.setText(VariaveisControle.qtdSelecionadaProdutoVenda + "");
            buttonAdicionarQtdProduto.setText("Modificar");
        }
        else {
            buttonAdicionarQtdProduto.setText("Adicionar");
            textQtd.setText("");
        }

        textQtd.setText(VariaveisControle.qtdSelecionadaProdutoVenda +"");

        buttonAdicionarQtdProduto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                VariaveisControle.qtdSelecionadaProdutoVenda = Funcoes.corrigeValoresCamposInt(textQtd.getText().toString());

                if(modificarProduto == false) {
                    VariaveisControle.dialogEscolherEntidade.addProduto(parent, position);
                } else{
                    VariaveisControle.fragmentProdutos.alteraQtdProdutoVenda();
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
            }
        });
        return view;
    }


//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState){
//       super.onCreateDialog(savedInstanceState);
//        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity())
//                .setTitle("Teste")
//          //      .setIcon()
//                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        Toast.makeText(getActivity(),"Ok",Toast.LENGTH_SHORT).show();
//                    }
//                })
//                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dismiss();
//                        Toast.makeText(getActivity(),"Cancelar",Toast.LENGTH_SHORT).show();
//                    }
//                })
//                ;
//        return  null;
//    }
}
