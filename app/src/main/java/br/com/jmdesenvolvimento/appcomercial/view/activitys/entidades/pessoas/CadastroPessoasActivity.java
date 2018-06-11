package br.com.jmdesenvolvimento.appcomercial.view.activitys.entidades.pessoas;

import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import br.com.jmdesenvolvimento.appcomercial.R;
import br.com.jmdesenvolvimento.appcomercial.controller.funcionais.Funcoes;
import br.com.jmdesenvolvimento.appcomercial.controller.funcionais.Mask;
import br.com.jmdesenvolvimento.appcomercial.controller.funcionais.VariaveisControle;
import br.com.jmdesenvolvimento.appcomercial.model.Tabela;
import br.com.jmdesenvolvimento.appcomercial.model.dao.SQLiteDatabaseDao;
import br.com.jmdesenvolvimento.appcomercial.model.entidades.cadastral.Estado;
import br.com.jmdesenvolvimento.appcomercial.model.entidades.cadastral.pessoas.Cliente;
import br.com.jmdesenvolvimento.appcomercial.model.entidades.cadastral.pessoas.Fornecedor;
import br.com.jmdesenvolvimento.appcomercial.model.entidades.cadastral.pessoas.IPessoa;
import br.com.jmdesenvolvimento.appcomercial.model.entidades.cadastral.pessoas.Pessoa;
import br.com.jmdesenvolvimento.appcomercial.model.entidades.cadastral.pessoas.Vendedor;

public class CadastroPessoasActivity extends AppCompatActivity {

    private TextInputEditText editTextNome;
    private TextInputLayout inputTextNome;
    private TextInputEditText editTextCpf;
    private TextInputLayout inputTextCpf;
    private TextInputEditText editTextRg;
    private TextInputLayout inputTextRg;
    private Spinner spinnerSexo;
    private TextInputEditText editTextNascimento;
    private TextInputLayout inputTextNascimento;
    private TextInputEditText editTextLogradouro;
    private Spinner spinnerEstado;
    private AutoCompleteTextView editTextMunicipio;
    private TextInputEditText editTextCep;
    private TextInputEditText editTextBairro;
    private TextInputEditText editTextNumero;
    private TextInputEditText editTextTelefone1;
    private TextInputEditText editTextTelefone2;
    private TextInputEditText editTextEmail;
    private TextInputEditText editTextLimite;
    private TextInputLayout inputTextLimite;
    private RadioGroup radioGroupTipoPessoa;
    private Menu menu;
    private boolean visualizacao;
    private String tipoPessoa;
    private boolean edicao;
    private Pessoa pessoaEditada;
    private Pessoa pessoaVisualizar;
    private Pessoa pessoaNovaAposEdicao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_pessoas);


        addIdsEAdapters();

        mudaHintComponentes();

        pessoaVisualizar = (Pessoa) getIntent().getSerializableExtra("pessoaVisualizar");
        tipoPessoa = getIntent().getStringExtra("tipoPessoa");

        if (getIntent().getSerializableExtra("tipoAbertura").equals("visualizar")) {
            visualizacao = true;
            camposNotAcessible();

            switch (tipoPessoa) {
                case "cliente":
                    setValoresFormularioCliente((Cliente) pessoaVisualizar);
                    setValoresFormularioPessoa(((Cliente) pessoaVisualizar).getPessoa());
                    pessoaNovaAposEdicao = new Cliente();
                    break;
                case "fornecedor":
                    setValoresFormularioFornecedor((Fornecedor) pessoaVisualizar);
                    setValoresFormularioPessoa(((Fornecedor) pessoaVisualizar).getPessoa());
                    pessoaNovaAposEdicao = new Fornecedor();
                    break;
                case "vendedor":
                    setValoresFormularioVendedor((Vendedor) pessoaVisualizar);
                    pessoaNovaAposEdicao = new Vendedor();
                    break;
            }
        }

    }

    private void addIdsEAdapters() {

        editTextNome = findViewById(R.id.cadastroNomeRSocial);
        editTextCpf = findViewById(R.id.cadastroCpfCnpj);
        editTextCpf.addTextChangedListener(new Mask().insert(Mask.CPF, editTextCpf));

        editTextRg = findViewById(R.id.cadastroRgIe);
        spinnerSexo = findViewById(R.id.cadastroSexo);

        editTextNascimento = findViewById(R.id.cadastroNascimento);
        editTextNascimento.addTextChangedListener(new Mask().insert(Mask.DATA, editTextNascimento));

        editTextLogradouro = findViewById(R.id.cadastroLogradouro);
        editTextBairro = findViewById(R.id.cadastroBairro);
        spinnerEstado = findViewById(R.id.cadastroEstado);

        editTextCep = findViewById(R.id.cadastroCep);
        editTextCep.addTextChangedListener(new Mask().insert(Mask.CEP, editTextCep));

        editTextNumero = findViewById(R.id.cadastroNumeroEndereco);
        editTextMunicipio = findViewById(R.id.cadastroMunicipio);

        editTextTelefone1 = findViewById(R.id.cadastroTelefone1);
        editTextTelefone1.addTextChangedListener(new Mask().insert(Mask.TELEFONE, editTextTelefone1));
        editTextTelefone2 = findViewById(R.id.cadastroTelefone2);
        editTextTelefone2.addTextChangedListener(new Mask().insert(Mask.TELEFONE, editTextTelefone2));

        editTextEmail = findViewById(R.id.cadastroEmail);

        inputTextCpf = findViewById(R.id.inputLayoutCadastroCpfCnpj);
        // inputTextLimite = findViewById(R.id.);
        editTextLimite = findViewById(R.id.cadastroLimiteCliente);
        inputTextNascimento = findViewById(R.id.inputLayoutCadastroNascimento);
        inputTextNome = findViewById(R.id.inputLayoutCadastroNomeRSocial);
        inputTextRg = findViewById(R.id.inputLayoutCadastroRgIe);

        spinnerEstado.setAdapter(getAdapterEstado());
        spinnerSexo.setAdapter(getAdapterSexo());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        if (visualizacao == false) {
            MenuInflater menuInflater = getMenuInflater();
            menuInflater.inflate(R.menu.menu_salvar, menu);
        } else {
            MenuInflater menuInflater = getMenuInflater();
            menuInflater.inflate(R.menu.menu_editar, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_formularios_salvar:
                SQLiteDatabaseDao dao = new SQLiteDatabaseDao(this);
                IPessoa pessoaAdicionada = null;
                if (edicao == false) {
                    switch (tipoPessoa) {
                        case "cliente":
                            pessoaAdicionada = getValoresFormularioClientes();
                            break;
                        case "fornecedor":
                            pessoaAdicionada = getValoresFormularioFornecedor();
                            break;
                        case "vendedor":
                            pessoaAdicionada = getValoresFormularioFornecedor();
                            //   dao.insert(get)
                            break;
                    }
                    dao.insert(pessoaAdicionada.getPessoa());
                    dao.insert((Tabela) pessoaAdicionada);
                    finish();
                } else {
                    switch (tipoPessoa) {
                        case "cliente":
                            this.pessoaEditada = getValoresFormularioClientes();
                            break;
                        case "fornecedor":
                            this.pessoaEditada = getValoresFormularioFornecedor();
                            break;
                        case "vendedor":
                            this.pessoaEditada = getValoresFormularioVendedor();
                            //  this.pessoa = getValoresProduto();
                            break;
                    }

                    pessoaNovaAposEdicao = (Pessoa) Funcoes.getTabelaModificada(this.pessoaVisualizar, this.pessoaEditada, this.pessoaNovaAposEdicao);
                    pessoaNovaAposEdicao.getPessoa().setId(pessoaVisualizar.getPessoa().getId());
                    dao.update(pessoaNovaAposEdicao.getPessoa(), true);
                    dao.update(pessoaNovaAposEdicao, true);
                    finish();
                    Toast.makeText(this, pessoaNovaAposEdicao.getNomeTabela(false) + " " + pessoaNovaAposEdicao.getId() + " alterado!", Toast.LENGTH_SHORT).show();
                }
                dao.close();
                edicao = false;
                break;


            case R.id.menu_editar:
                camposAcessible();
                item.setVisible(false);
                edicao = true;
                break;
        }
        Snackbar.make(VariaveisControle.buttonAddPessoaForSnackbar, "Salvo com sucesso!", Snackbar.LENGTH_SHORT).show();

        return super.onOptionsItemSelected(item);
    }

    private ArrayAdapter getAdapterEstado() {
        SQLiteDatabaseDao dao = new SQLiteDatabaseDao(this);
        List<Tabela> list = (List<Tabela>) dao.selectAll(new Estado(), null, false, null, null, "nome_estado", null);
        ArrayAdapter<Tabela> arrayAdapter = (ArrayAdapter<Tabela>) new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, list);
        ArrayAdapter<Tabela> spinnerArrayAdapter = arrayAdapter;
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);

        dao.close();

        return spinnerArrayAdapter;
    }

    private ArrayAdapter getAdapterSexo() {
        List<String> list = new ArrayList<>();
        list.add("Masculino");
        list.add("Feminino");

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, list);
       // spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return spinnerArrayAdapter;
    }

    private void mudaHintComponentes() {
        String tipoPessoa = getIntent().getStringExtra("tipoPessoa");
        switch (tipoPessoa.toLowerCase()) {
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

    private Pessoa getValoresFormularioPessoa() {
        Pessoa pessoa = new Pessoa();
        pessoa.setNome(editTextNome.getText().toString());
        pessoa.setCpfCNPJ(Funcoes.removePontosTracos(editTextCpf.getText().toString()));
        pessoa.setRgIE(Funcoes.corrigeValoresCamposInt(editTextRg.getText().toString()));
        pessoa.setSexo(spinnerSexo.getSelectedItemPosition());
        pessoa.setNascimento(Funcoes.removePontosTracos(editTextNascimento.getText().toString()));
        pessoa.setLogradouro(editTextLogradouro.getText().toString());
        pessoa.setBairro(editTextBairro.getText().toString());
        pessoa.setNumero(Funcoes.corrigeValoresCamposInt(editTextNumero.getText().toString()));
        pessoa.setCep(Funcoes.corrigeValoresCamposInt(editTextCep.getText().toString()));
        pessoa.setEstado((Estado) spinnerEstado.getSelectedItem());
        //    pessoa.setMunicipio(editTextMunicipio.getIte);
        pessoa.setTelefone1(Funcoes.removePontosTracos(editTextTelefone1.getText().toString()));
        pessoa.setTelefone2(Funcoes.removePontosTracos(editTextTelefone2.getText().toString()));
        pessoa.setEmail(editTextEmail.getText().toString());
        return pessoa;
    }

    private Cliente getValoresFormularioClientes() {
        Cliente cliente = new Cliente();
//        cliente.setLimite(Double.parseDouble(editTextLimite.getText().toString()));
        cliente.setPessoa(getValoresFormularioPessoa());

        return cliente;
    }

    private Fornecedor getValoresFormularioFornecedor() {

        Pessoa pessoa = getValoresFormularioPessoa();
        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setPessoa(pessoa);

        return fornecedor;
    }

    private Vendedor getValoresFormularioVendedor() {

        Pessoa pessoa = getValoresFormularioPessoa();
        Vendedor vendedor = new Vendedor();
        vendedor.setPessoa(pessoa);
        return vendedor;
    }

    private void setValoresFormularioPessoa(Pessoa pessoa) {
        editTextNome.setText(Funcoes.removeNullZeroFormularios(pessoa.getNome()));
        editTextCpf.setText(pessoa.getCpfCNPJ());
        editTextRg.setText(Funcoes.removeNullZeroFormularios(pessoa.getRgIE() + ""));
        editTextNascimento.setText(Funcoes.removeNullZeroFormularios(pessoa.getNascimento() + " "));
        spinnerSexo.setSelection(pessoa.getSexo());
        editTextLogradouro.setText(Funcoes.removeNullZeroFormularios(pessoa.getLogradouro()));
        editTextBairro.setText(Funcoes.removeNullZeroFormularios(pessoa.getBairro()));
        editTextNumero.setText(Funcoes.removeNullZeroFormularios(pessoa.getNumero() + ""));
        editTextCep.setText(Funcoes.removeNullZeroFormularios(pessoa.getCep() + ""));
        spinnerEstado.setSelection(pessoa.getEstado().getId());
        editTextMunicipio.setText(Funcoes.removeNullZeroFormularios(pessoa.getMunicipio().getNome()));
        editTextTelefone1.setText(Funcoes.removeNullZeroFormularios(pessoa.getTelefone1() + ""));
        editTextTelefone2.setText(Funcoes.removeNullZeroFormularios(pessoa.getTelefone2() + ""));
        editTextEmail.setText(Funcoes.removeNullZeroFormularios(pessoa.getEmail()));
    }

    private void setValoresFormularioCliente(Cliente cliente) {
        setValoresFormularioPessoa(cliente.getPessoa());
        editTextLimite.setText(Funcoes.removeNullZeroFormularios(cliente.getLimite() + ""));

    }

    private void setValoresFormularioFornecedor(Fornecedor fornecedor) {
        setValoresFormularioPessoa(fornecedor.getPessoa());
    }

    private void setValoresFormularioVendedor(Vendedor vendedor) {
        setValoresFormularioPessoa(vendedor.getPessoa());
    }


    private void camposNotAcessible() {
        Log.i("camposNotAcessible", "entrou");
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field f : fields) {
            if (ehFieldEditavel(f)) {
                f.setAccessible(true);
                try {
                    View v = (View) f.get(this);
                    v.setEnabled(false);
                } catch (IllegalAccessException e) {

                    e.printStackTrace();
                } catch (NullPointerException e) {
                    Log.i("Erro", "Algo deu errado com o field " + f.getName());
                    e.printStackTrace();
                }
            }
        }
        Log.i("camposNotAcessible", "saiu");
    }

    private void camposAcessible() {
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field f : fields) {
            if (ehFieldEditavel(f)) {
                f.setAccessible(true);
                View v;
                try {
                    if (f.getType().getSimpleName().equals("TextInputEditText")) {
                        v = (TextInputEditText) f.get(this);
                        TextInputEditText editText = (TextInputEditText) f.get(this);
                        if (editText.getText().toString().equals(" "))
                            editText.setText("");
                    } else {
                        v = (View) f.get(this);
                    }
                    v.setEnabled(true);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        visualizacao = false;
        onCreateOptionsMenu(menu);
    }

    private boolean ehFieldEditavel(Field f) {
        return f.getType().getSimpleName().equals("TextInputEditText")
                || f.getType().getSimpleName().equals("AutoCompleteTextView")
                || f.getType().getSimpleName().contains("Spinner");
    }

}
