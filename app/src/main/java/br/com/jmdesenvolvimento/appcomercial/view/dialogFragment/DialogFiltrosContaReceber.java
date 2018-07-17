package br.com.jmdesenvolvimento.appcomercial.view.dialogFragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

import app.jm.funcional.model.filtrosOrdenacao.FiltroContasReceber;
import br.com.jmdesenvolvimento.appcomercial.R;;
import br.com.jmdesenvolvimento.appcomercial.controller.funcionaisAndroid.FuncoesViewAndroid;
import br.com.jmdesenvolvimento.appcomercial.controller.funcionaisAndroid.VariaveisControleAndroid;
import app.jm.funcional.controller.funcoesGerais.FuncoesGerais;
import app.jm.funcional.model.Tabela;
import br.com.jmdesenvolvimento.appcomercial.model.dao.SQLiteDatabaseDao;
import app.jm.funcional.model.entidades.cadastral.pessoas.Cliente;


/**Dialog onde o usuário irá informar tipo de pagamento, parcelas etc.*/
@SuppressLint("ValidFragment")
public class DialogFiltrosContaReceber extends android.support.v4.app.DialogFragment {
    private TextView textViewOk;
    private TextView textViewCancelar;
    private AutoCompleteTextView editTextNomeCliente;
    private TextInputEditText editTextDataInicio;
    private TextInputEditText editTextDataFim;
    private Calendar dataInicio;
    private Calendar dataFim;
    private FiltroContasReceber contaReceber;
    private List<Tabela> list;


    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int theme;
        int style = DialogFragment.STYLE_NO_TITLE;
        setStyle(style, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_filtro_contas_receber, container, false);
        editTextNomeCliente = view.findViewById(R.id.editTextContaReceberNomeCliente);
        editTextDataInicio = view.findViewById(R.id.editTextContaReceberInicio);
        editTextDataFim = view.findViewById(R.id.editTextContaReceberFim);
        FuncoesViewAndroid.addCalendar((AppCompatActivity) getActivity(),editTextDataInicio);
        FuncoesViewAndroid.addCalendar((AppCompatActivity) getActivity(),editTextDataFim);
        textViewCancelar = view.findViewById(R.id.textViewCancelar);
        textViewOk = view.findViewById(R.id.textViewOk);

        ouveCliqueElementos();

        return view;
    }

    private void ouveCliqueElementos(){
        editTextNomeCliente.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

            }
        });
        editTextNomeCliente.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                 editTextNomeCliente.setAdapter(getAdapterPessoa(s.toString()));
            }
        });

        editTextNomeCliente.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("Clicado","clicado");
                contaReceber = contaReceber == null ? new FiltroContasReceber() : contaReceber;
                contaReceber.setCliente((Cliente) list.get(position));
            }
        });

        editTextDataInicio.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.i("datInicio",s.toString());
                contaReceber = contaReceber == null ? new FiltroContasReceber() : contaReceber;
                dataInicio = FuncoesGerais.stringToCalendar(s.toString(),FuncoesGerais.ddMMyyyy);
                contaReceber.setDataInicio(dataInicio);
                if(dataFim != null){
                    if(dataFim.getTimeInMillis() < dataInicio.getTimeInMillis())
                        FuncoesViewAndroid.addAlertDialogAlerta(getContext(),"Atenção","Data fim menor que a data inicio!");
                }
            }
        });

        editTextDataFim.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.i("datFim",s.toString());
                dataFim = FuncoesGerais.stringToCalendar(s.toString(),FuncoesGerais.ddMMyyyy);
                contaReceber.setDataFim(dataFim);
                if(dataInicio != null) {
                    if (dataFim.getTimeInMillis() < dataInicio.getTimeInMillis())
                        FuncoesViewAndroid.addAlertDialogAlerta(getContext(), "Atenção","Data FIM menor que a data INICIO!");
                    }
                }
        });

        textViewOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VariaveisControleAndroid.contaReceberActivity.carregaLista(contaReceber);
                dismiss();
            }
        });

        textViewCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private ArrayAdapter<Tabela> getAdapterPessoa(String sequencia) {
            SQLiteDatabaseDao dao = new SQLiteDatabaseDao(getContext());
            list = sequencia.length() < 15 ? dao.buscaPessoaPorNomeCpf(new Cliente(), "nome_pessoa", sequencia) : list;
            dao.close();
        ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_dropdown_item_1line, list);
        return adapter;
    }
}
