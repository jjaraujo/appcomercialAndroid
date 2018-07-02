package br.com.jmdesenvolvimento.appcomercial.view.activitys.entidades.pessoas;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.com.jmdesenvolvimento.appcomercial.R;
import br.com.jmdesenvolvimento.appcomercial.controller.funcionaisAndroid.FuncoesViewAndroid;
import br.com.jmdesenvolvimento.appcomercial.controller.funcionaisAndroid.Mask;
import br.com.jmdesenvolvimento.appcomercial.controller.funcionaisAndroid.VariaveisControleAndroid;
import com.jmdesenvolvimento.appcomercial.controller.funcoesGerais.FuncoesGerais;
import com.jmdesenvolvimento.appcomercial.model.Tabela;
import br.com.jmdesenvolvimento.appcomercial.model.dao.SQLiteDatabaseDao;
import com.jmdesenvolvimento.appcomercial.model.entidades.cadastral.Estado;
import com.jmdesenvolvimento.appcomercial.model.entidades.cadastral.pessoas.Pessoa;
import com.jmdesenvolvimento.appcomercial.model.entidades.cadastral.pessoas.Vendedor;

public class CadastroVendedoresActivity extends AppCompatActivity {

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
    private Menu menu;
    private boolean visualizacao;
    private boolean edicao;
    private Vendedor pessoaEditada;
    private Vendedor pessoaVisualizar;
    private Vendedor pessoaNovaAposEdicao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_vendedores);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        addIdsEAdapters();

        setTitle("Cadastro de Vendedor");

        pessoaVisualizar = (Vendedor) getIntent().getSerializableExtra("pessoaVisualizar");

        if (getIntent().getSerializableExtra("tipoAbertura").equals("visualizar")) {
            visualizacao = true;
            AlteraCamposPessoas.camposNotAcessible(this);

            setValoresFormularioVendedor( pessoaVisualizar);
            pessoaNovaAposEdicao = new Vendedor();
        }

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
        SQLiteDatabaseDao dao = new SQLiteDatabaseDao(this);
        switch (item.getItemId()) {
            case R.id.menu_formularios_salvar:
                Vendedor pessoaAdicionada = null;
                if (edicao == false) {

                    pessoaAdicionada = getValoresFormularioVendedor();
                    dao.insert(pessoaAdicionada.getPessoa());
                    dao.insert(pessoaAdicionada);
                    finish();
                } else {
                    this.pessoaEditada = getValoresFormularioVendedor();
                            //  this.pessoa = getValoresProduto();
                    pessoaNovaAposEdicao = (Vendedor) FuncoesGerais.getTabelaModificada(this.pessoaVisualizar, this.pessoaEditada, this.pessoaNovaAposEdicao);
                    pessoaNovaAposEdicao.getPessoa().setId(pessoaVisualizar.getPessoa().getId());
                    pessoaNovaAposEdicao.getPessoa().setId(pessoaVisualizar.getPessoa().getId());
                    dao.update(pessoaNovaAposEdicao.getPessoa(), true);
                    dao.update(pessoaNovaAposEdicao, true);
                    finish();
                    Toast.makeText(this, pessoaNovaAposEdicao.getNomeTabela(false) + " " + pessoaNovaAposEdicao.getId() + " alterado!", Toast.LENGTH_SHORT).show();
                }
                edicao = false;

                break;


            case R.id.menu_editar:
                AlteraCamposPessoas.camposAcessible(this,menu);
                item.setVisible(false);
                edicao = true;
                break;

            case android.R.id.home: // botao voltar
                finish();
                break;
        }
        dao.close();
        FuncoesViewAndroid.addSnackBarToast(VariaveisControleAndroid.buttonAddPessoaForSnackbar, this,"Salvo com sucesso!");

        return super.onOptionsItemSelected(item);
    }

    private void addIdsEAdapters() {

        editTextNome = findViewById(R.id.cadastroNomeRSocial);
        editTextCpf = findViewById(R.id.cadastroCpfCnpj);
        editTextCpf.addTextChangedListener(new Mask().insert(Mask.CPF, editTextCpf));

        editTextRg = findViewById(R.id.cadastroRgIe);
        spinnerSexo = findViewById(R.id.cadastroSexo);

        editTextNascimento = findViewById(R.id.cadastroNascimento);
        FuncoesViewAndroid.addCalendar(this,editTextNascimento);

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

        editTextEmail = findViewById(R.id.cadastroUsuario);

        inputTextCpf = findViewById(R.id.inputLayoutCadastroCpfCnpj);
        inputTextNascimento = findViewById(R.id.inputLayoutCadastroNascimento);
        inputTextNome = findViewById(R.id.inputLayoutCadastroNomeRSocial);
        inputTextRg = findViewById(R.id.inputLayoutCadastroRgIe);

        spinnerEstado.setAdapter(getAdapterEstado());
        spinnerSexo.setAdapter(getAdapterSexo());

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

    private Pessoa getValoresFormularioPessoa() {
        Pessoa pessoa = new Pessoa();
        pessoa.setNome(editTextNome.getText().toString());
        pessoa.setCpfCNPJ(FuncoesGerais.removePontosTracos(editTextCpf.getText().toString()));
        pessoa.setRgIE(FuncoesGerais.corrigeValoresCamposInt(editTextRg.getText().toString()));
        pessoa.setSexo(spinnerSexo.getSelectedItemPosition());
        pessoa.setNascimento(FuncoesGerais.removePontosTracos(editTextNascimento.getText().toString()));
        pessoa.setLogradouro(editTextLogradouro.getText().toString());
        pessoa.setBairro(editTextBairro.getText().toString());
        pessoa.setNumero(FuncoesGerais.corrigeValoresCamposInt(editTextNumero.getText().toString()));
        pessoa.setCep(FuncoesGerais.corrigeValoresCamposInt(editTextCep.getText().toString()));
        pessoa.setEstado((Estado) spinnerEstado.getSelectedItem());
        //    pessoa.setMunicipio(editTextMunicipio.getIte);
        pessoa.setTelefone1(FuncoesGerais.removePontosTracos(editTextTelefone1.getText().toString()));
        pessoa.setTelefone2(FuncoesGerais.removePontosTracos(editTextTelefone2.getText().toString()));
        pessoa.setEmail(editTextEmail.getText().toString());
        return pessoa;
    }


    private Vendedor getValoresFormularioVendedor() {

        Pessoa pessoa = getValoresFormularioPessoa();
        Vendedor vendedor = new Vendedor();
        vendedor.setPessoa(pessoa);
        return vendedor;
    }

    private void setValoresFormularioPessoa(Pessoa pessoa) {
        editTextNome.setText(FuncoesGerais.removeNullZeroFormularios(pessoa.getNome()));
        editTextCpf.setText(pessoa.getCpfCNPJ());
        editTextRg.setText(FuncoesGerais.removeNullZeroFormularios(pessoa.getRgIE() + ""));
        editTextNascimento.setText(FuncoesGerais.removeNullZeroFormularios(pessoa.getNascimento() + " "));
        spinnerSexo.setSelection(pessoa.getSexo());
        editTextLogradouro.setText(FuncoesGerais.removeNullZeroFormularios(pessoa.getLogradouro()));
        editTextBairro.setText(FuncoesGerais.removeNullZeroFormularios(pessoa.getBairro()));
        editTextNumero.setText(FuncoesGerais.removeNullZeroFormularios(pessoa.getNumero() + ""));
        editTextCep.setText(FuncoesGerais.removeNullZeroFormularios(pessoa.getCep() + ""));
        spinnerEstado.setSelection(pessoa.getEstado().getId());
        editTextMunicipio.setText(FuncoesGerais.removeNullZeroFormularios(pessoa.getMunicipio().getNome()));
        editTextTelefone1.setText(FuncoesGerais.removeNullZeroFormularios(pessoa.getTelefone1() + ""));
        editTextTelefone2.setText(FuncoesGerais.removeNullZeroFormularios(pessoa.getTelefone2() + ""));
        editTextEmail.setText(FuncoesGerais.removeNullZeroFormularios(pessoa.getEmail()));
    }

    private void setValoresFormularioVendedor(Vendedor vendedor) {
        setValoresFormularioPessoa(vendedor.getPessoa());
    }

}
