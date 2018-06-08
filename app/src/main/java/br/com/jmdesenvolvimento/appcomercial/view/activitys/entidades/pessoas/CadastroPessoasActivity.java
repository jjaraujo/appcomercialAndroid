package br.com.jmdesenvolvimento.appcomercial.view.activitys.entidades.pessoas;

import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import br.com.jmdesenvolvimento.appcomercial.R;
import br.com.jmdesenvolvimento.appcomercial.controller.funcionais.Funcoes;
import br.com.jmdesenvolvimento.appcomercial.model.Tabela;
import br.com.jmdesenvolvimento.appcomercial.model.dao.SQLiteDatabaseDao;
import br.com.jmdesenvolvimento.appcomercial.model.entidades.cadastral.Estado;
import br.com.jmdesenvolvimento.appcomercial.model.entidades.cadastral.pessoas.Cliente;
import br.com.jmdesenvolvimento.appcomercial.model.entidades.cadastral.pessoas.Fornecedor;
import br.com.jmdesenvolvimento.appcomercial.model.entidades.cadastral.pessoas.Pessoa;

public class CadastroPessoasActivity extends AppCompatActivity {

    private EditText editTextNome;
    private TextInputLayout inputTextNome;
    private EditText editTextCpf;
    private TextInputLayout inputTextCpf;
    private EditText editTextRg;
    private TextInputLayout inputTextRg;
    private Spinner spinnerSexo;
    private EditText editTextNascimento;
    private TextInputLayout inputTextNascimento;
    private EditText editTextLogradouro;
    private Spinner spinnerEstado;
    private AutoCompleteTextView editTextMunicipio;
    private EditText editTextCep;
    private EditText editTextBairro;
    private EditText editTextNumero;
    private EditText editTextTelefone1;
    private EditText editTextTelefone2;
    private EditText editTextEmail;
    private EditText editTextLimite;
    private TextInputLayout inputTextLimite;
    private RadioGroup radioGroupTipoPessoa;
    private Button buttonSalvar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_pessoas);


        editTextNome = findViewById(R.id.cadastroNomeRSocial);
        editTextCpf = findViewById(R.id.cadastroCpfCnpj);
        editTextRg = findViewById(R.id.cadastroRgIe);
        spinnerSexo = findViewById(R.id.cadastroSexo);
        editTextNascimento = findViewById(R.id.cadastroNascimento);
        editTextLogradouro = findViewById(R.id.cadastroLogradouro);
        editTextBairro = findViewById(R.id.cadastroBairro);
        spinnerEstado = findViewById(R.id.cadastroEstado);
        editTextCep = findViewById(R.id.cadastroCep);
        editTextNumero = findViewById(R.id.cadastroNumeroEndereco);
        editTextMunicipio = findViewById(R.id.cadastroMunicipio);
        editTextTelefone1 = findViewById(R.id.cadastroTelefone1);
        editTextTelefone2 = findViewById(R.id.cadastroTelefone2);
        editTextEmail = findViewById(R.id.cadastroEmail);
        buttonSalvar = findViewById(R.id.buttonSalvar);

        inputTextCpf = findViewById(R.id.inputLayoutCadastroCpfCnpj);
       // inputTextLimite = findViewById(R.id.);
        inputTextNascimento = findViewById(R.id.inputLayoutCadastroNascimento);
        inputTextNome = findViewById(R.id.inputLayoutCadastroNomeRSocial);
        inputTextRg = findViewById(R.id.inputLayoutCadastroRgIe);

        spinnerEstado.setAdapter(getAdapterEstado());
        spinnerSexo.setAdapter(getAdapterSexo());

        //editTextLimite = findViewById(R.id.limiteCliente);
        mudaHintComponentes();

        buttonSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String tipoPessoa = getIntent().getStringExtra("tipoPessoa");
                switch (tipoPessoa.toLowerCase()){
                    case "fornecedor":
                        salvaDadosFornecedor();
                        break;
                    case "cliente":
                        salvaDadosCliente();
                        break;
                }
                finish();
            }
        });

    }

    private ArrayAdapter getAdapterEstado(){
        SQLiteDatabaseDao dao = new SQLiteDatabaseDao(this);
        List<Tabela> list = (List<Tabela>) dao.selectAll(new Estado(),null,false);
        ArrayAdapter<Tabela> arrayAdapter = (ArrayAdapter<Tabela>) new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,list);
        ArrayAdapter<Tabela> spinnerArrayAdapter = arrayAdapter;
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);

        dao.close();

        return spinnerArrayAdapter;
    }

    private ArrayAdapter getAdapterSexo(){
        List<String> list = new ArrayList<>();
        list.add("Masculino");
        list.add("Feminino");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,list);
        ArrayAdapter<String> spinnerArrayAdapter = arrayAdapter;
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        return spinnerArrayAdapter;
    }

    private void mudaHintComponentes(){
        String tipoPessoa = getIntent().getStringExtra("tipoPessoa");
        switch (tipoPessoa.toLowerCase()){
            case "fornecedor":
                setTitle("Cadastro de Fornecedor");
                inputTextNome.setHint("Nome / Razão Social");
                inputTextCpf.setHint("CPF / CNPJ");
                inputTextNascimento.setHint("Nascimento / Data Inicio");
                inputTextRg.setHint("RG / Inscrição Estadual");
                spinnerSexo.setVisibility(View.INVISIBLE);
                break;
            case "cliente":
                setTitle("Cadastro de Cliente");
                break;
        }
    }

    private void salvaDadosCliente(){
        Pessoa pessoa = new Pessoa();
        Cliente cliente = new Cliente();
//        cliente.setLimite(Double.parseDouble(editTextLimite.getText().toString()));

        pessoa.setNome(editTextNome.getText().toString());
        pessoa.setCpfCNPJ(Funcoes.corrigeValoresCamposLong(editTextCpf.getText().toString()));
        pessoa.setRgIE(Funcoes.corrigeValoresCamposInt(editTextRg.getText().toString()));
        pessoa.setSexo(spinnerSexo.getSelectedItem().toString());
        pessoa.setNascimento(editTextNascimento.getText().toString());
        pessoa.setRua(editTextLogradouro.getText().toString());
        pessoa.setBairro(editTextBairro.getText().toString());
        pessoa.setNumero(Funcoes.corrigeValoresCamposInt(editTextNumero.getText().toString()));
        pessoa.setCep(Funcoes.corrigeValoresCamposInt(editTextCep.getText().toString()));
        pessoa.setEstado((Estado)spinnerEstado.getSelectedItem());
    //    pessoa.setMunicipio(editTextMunicipio.getIte);
        pessoa.setTelefone1(editTextTelefone1.getText().toString());
        pessoa.setTelefone2(editTextTelefone2.getText().toString());
        pessoa.setEmail(editTextEmail.getText().toString());

        cliente.setPessoa(pessoa);

        SQLiteDatabaseDao dao = new SQLiteDatabaseDao(CadastroPessoasActivity.this);
        dao.insert(pessoa);
        dao.insert(cliente);
        dao.close();
    }

    private void salvaDadosFornecedor(){
        Pessoa pessoa = new Pessoa();
        Fornecedor fornecedor = new Fornecedor();

        pessoa.setNome(editTextNome.getText().toString());
        pessoa.setCpfCNPJ(Funcoes.corrigeValoresCamposLong(editTextCpf.getText().toString()));
        pessoa.setRgIE(Funcoes.corrigeValoresCamposInt(editTextRg.getText().toString()));
        pessoa.setNascimento(editTextNascimento.getText().toString());
        pessoa.setRua(editTextLogradouro.getText().toString());
        pessoa.setBairro(editTextBairro.getText().toString());
        pessoa.setNumero(Funcoes.corrigeValoresCamposInt(editTextNumero.getText().toString()));
        pessoa.setCep(Funcoes.corrigeValoresCamposInt(editTextCep.getText().toString()));
        pessoa.setEstado((Estado)spinnerEstado.getSelectedItem());
        //    pessoa.setMunicipio(editTextMunicipio.getIte);
        pessoa.setTelefone1(editTextTelefone1.getText().toString());
        pessoa.setTelefone2(editTextTelefone2.getText().toString());
        pessoa.setEmail(editTextEmail.getText().toString());

        fornecedor.setPessoa(pessoa);

        SQLiteDatabaseDao dao = new SQLiteDatabaseDao(CadastroPessoasActivity.this);
        dao.insert(pessoa);
        dao.insert(fornecedor);
        dao.close();
    }

}
