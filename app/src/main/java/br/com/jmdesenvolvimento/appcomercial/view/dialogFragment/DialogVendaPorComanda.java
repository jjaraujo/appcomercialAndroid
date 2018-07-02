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
import br.com.jmdesenvolvimento.appcomercial.controller.funcionaisAndroid.FuncoesVendas;
import br.com.jmdesenvolvimento.appcomercial.controller.funcionaisAndroid.FuncoesViewAndroid;
import com.jmdesenvolvimento.appcomercial.controller.funcoesGerais.FuncoesGerais;
import br.com.jmdesenvolvimento.appcomercial.controller.funcionaisAndroid.VariaveisControleAndroid;
import com.jmdesenvolvimento.appcomercial.controller.funcoesGerais.VariaveisControleG;
import br.com.jmdesenvolvimento.appcomercial.model.dao.SQLiteDatabaseDao;
import com.jmdesenvolvimento.appcomercial.model.entidades.cadastral.pessoas.Cliente;
import com.jmdesenvolvimento.appcomercial.model.entidades.vendas.Venda;

@SuppressLint("ValidFragment")
public class DialogVendaPorComanda extends DialogFragment {


    private View view;
    private Button buttonAdicionarComanda;
    private Button somar;
    private Button subtrair;
    private EditText textComanda;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int theme;
        theme = android.R.style.Theme_Material_Dialog;
        setStyle(DialogFragment.STYLE_NO_TITLE, theme);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.dialog_fragment_comanda, container, false);
        buttonAdicionarComanda = view.findViewById(R.id.buttonAdicionarComanda);
        somar = view.findViewById(R.id.buttonComandaSomar);
        subtrair = view.findViewById(R.id.buttonComandaSubtrair);
        textComanda = view.findViewById(R.id.editTexComanda);
        textComanda.setText("");
        final String nomeTipoVenda = VariaveisControleG.configuracoesSimples.getNomeTipoVenda();
        TextView textViewTextoComandaMesa = view.findViewById(R.id.textViewTextoComandaMesa);
        textViewTextoComandaMesa.setText(textViewTextoComandaMesa.getText().toString() + nomeTipoVenda);
        if(nomeTipoVenda.toLowerCase().equals("comanda")) {
            textViewTextoComandaMesa.setText("Nº da Comanda");
        }
        buttonAdicionarComanda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String numeroComanda = textComanda.getText().toString();
                if (!hasComandaAberta(numeroComanda)) {
                    addVenda();
                    dismiss();
                } else{
                    FuncoesViewAndroid.addAlertDialogAlerta(getContext(),"Já existe uma venda aberta para a "+
                            nomeTipoVenda +" " +numeroComanda+". Verifique!");
                }
            }
        });

        somar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qtdAtual = FuncoesGerais.corrigeValoresCamposInt(textComanda.getText().toString());
                textComanda.setText((qtdAtual + 1) + "");
            }
        });

        subtrair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qtdAtual = FuncoesGerais.corrigeValoresCamposInt(textComanda.getText().toString());
                textComanda.setText((qtdAtual - 1) + "");
                if(textComanda.getText().toString().equals("0")){
                    textComanda.setText("1");
                }
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        textComanda.setText("1");
    }

    private void addVenda() {

        Venda venda = new Venda();
        venda.setCliente(new Cliente());
        venda.setNumeroMesaComanda(Integer.parseInt(textComanda.getText().toString()));
        FuncoesVendas.criaVenda(venda,getContext());
        dismiss();
    }

    private boolean hasComandaAberta(String numero) {
        SQLiteDatabaseDao dao = new SQLiteDatabaseDao(getContext());
        Venda venda = (Venda) dao.select(new Venda(),null, "numeroMesaComanda = " + numero + " AND dataFechamento IS NULL",
                null,null,null );
        dao.close();
        return venda.getId() > 0;
    }
}
