package br.com.jmdesenvolvimento.appcomercial.view.activitys.entidades.pessoas;


import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

import br.com.jmdesenvolvimento.appcomercial.R;;
import br.com.jmdesenvolvimento.appcomercial.controller.funcionaisAndroid.Mask;

import app.jm.funcional.controller.funcoesGerais.EstrategiaLogin;
import app.jm.funcional.controller.funcoesGerais.FuncoesGerais;
import br.com.jmdesenvolvimento.appcomercial.controller.services.conexoes.ConexaoServiceCadastraEmpresa;
import app.jm.funcional.model.Tabela;
import br.com.jmdesenvolvimento.appcomercial.model.dao.SQLiteDatabaseDao;
import br.com.jmdesenvolvimento.appcomercial.view.activitys.entidades.pessoas.AlteraCamposPessoas;

import app.jm.funcional.model.entidades.cadastral.Estado;
import app.jm.funcional.model.entidades.cadastral.pessoas.EmpresaCliente;
import app.jm.funcional.model.entidades.cadastral.pessoas.Pessoa;

public class CadastroEmpresaActivity extends AppCompatActivity {

    private TextInputEditText editTextNome;
    private TextInputEditText editTextFantsasia;
    private TextInputEditText editTextCpf;
    private TextInputEditText editTextRg;
    private TextInputEditText editTextLogradouro;
    private Spinner spinnerEstado;
    private AutoCompleteTextView editTextMunicipio;
    private TextInputEditText editTextCep;
    private TextInputEditText editTextBairro;
    private TextInputEditText editTextNumero;
    private TextInputEditText editTextTelefone1;
    private TextInputEditText editTextTelefone2;
    private TextInputEditText editTextEmail;
    private TextInputEditText editTextSenha;
    private TextInputEditText editTextConfirmarSenha;
    private Menu menu;
    private boolean visualizacao;
    private boolean edicao;
    private EmpresaCliente pessoaEditada;
    private EmpresaCliente pessoaVisualizar;
    private EmpresaCliente pessoaNovaAposEdicao;
    private boolean primeiroCadastro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_empresa);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        addIdsEAdapters();

        setTitle("Cadastre sua empresa");

        pessoaVisualizar = (EmpresaCliente) getIntent().getSerializableExtra("pessoaVisualizar");

        if (getIntent().getSerializableExtra("tipoAbertura").equals("visualizar")) {
            visualizacao = true;
            AlteraCamposPessoas.camposNotAcessible(this);

            setValoresFormularioFornecedor( pessoaVisualizar);
            setValoresFormularioPessoa(( pessoaVisualizar).getPessoa());
            pessoaNovaAposEdicao = new EmpresaCliente();
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
                EmpresaCliente empresaAdicionada = null;
                if (edicao == false) {

                    empresaAdicionada = getValoresFormularioEmpresa();
                    empresaAdicionada.setPessoa(getValoresFormularioPessoa());
                    if (!verificaPreenchimentoCampos()) {
                        ConexaoServiceCadastraEmpresa conexaoService = new ConexaoServiceCadastraEmpresa( this, empresaAdicionada);
                        conexaoService.execute();
                    }

                } else {

                    this.pessoaEditada = getValoresFormularioEmpresa();

                    pessoaNovaAposEdicao = (EmpresaCliente) FuncoesGerais.getTabelaModificada(this.pessoaVisualizar, this.pessoaEditada, this.pessoaNovaAposEdicao);
                    pessoaNovaAposEdicao.getPessoa().setId(pessoaVisualizar.getPessoa().getId());
                    dao.update(pessoaNovaAposEdicao.getPessoa());
                    dao.update(pessoaNovaAposEdicao);

                    Toast.makeText(this, pessoaNovaAposEdicao.getNomeTabela(false) + " " + pessoaNovaAposEdicao.getId() + " alterado!", Toast.LENGTH_SHORT).show();
                }
                edicao = false;
                break;


            case R.id.menu_editar:
                AlteraCamposPessoas.camposAcessible(this, menu);
                item.setVisible(false);
                edicao = true;
                break;

            case android.R.id.home: // botao voltar
                finish();
                break;
        }
        dao.close();
    //    Snackbar.make(VariaveisControleAndroid.buttonAddPessoaForSnackbar, "Salvo com sucesso!", Snackbar.LENGTH_SHORT).show();

        return super.onOptionsItemSelected(item);
    }

    private void addIdsEAdapters() {

        editTextNome = findViewById(R.id.cadastroNomeRSocial);
        editTextFantsasia = findViewById(R.id.cadastroEmpresaFantasia);
        editTextCpf = findViewById(R.id.cadastroCpfCnpj);
        editTextCpf.addTextChangedListener(new Mask().insert(Mask.CPF, editTextCpf));

        editTextRg = findViewById(R.id.cadastroRgIe);

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

        editTextSenha = findViewById(R.id.cadastroEmpresaSenha);
        editTextConfirmarSenha = findViewById(R.id.cadastroEmpresaConfirmarSenha);
//
//        inputTextCpf = findViewById(R.id.inputLayoutCadastroCpfCnpj);
//        inputTextNome = findViewById(R.id.inputLayoutCadastroNomeRSocial);
//        inputTextRg = findViewById(R.id.inputLayoutCadastroRgIe);    se for depois de 05/07/2018, pode remover

        spinnerEstado.setAdapter(getAdapterEstado());

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

    private Pessoa getValoresFormularioPessoa() {
        Pessoa pessoa = new Pessoa();
        pessoa.setNome(editTextNome.getText().toString());
        pessoa.setNomeFantasia(editTextFantsasia.getText().toString());
        pessoa.setCpfCNPJ(FuncoesGerais.removePontosTracos(editTextCpf.getText().toString()));
        pessoa.setRgIE(FuncoesGerais.corrigeValoresCamposInt(editTextRg.getText().toString()));
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

    private EmpresaCliente getValoresFormularioEmpresa() {

        Pessoa pessoa = getValoresFormularioPessoa();
        EmpresaCliente empresaCliente = new EmpresaCliente();
        empresaCliente.setPessoa(pessoa);
        empresaCliente.setLogin(editTextEmail.getText().toString());
        empresaCliente.setSenha(EstrategiaLogin.criptografaSenha(editTextSenha.getText().toString()));

        return empresaCliente;
    }

    private void setValoresFormularioPessoa(Pessoa pessoa) {
        editTextNome.setText(FuncoesGerais.removeNullZeroFormularios(pessoa.getNome()));
        editTextFantsasia.setText(FuncoesGerais.removeNullZeroFormularios(pessoa.getNomeFantasia()));
        editTextCpf.setText(pessoa.getCpfCNPJ());
        editTextRg.setText(FuncoesGerais.removeNullZeroFormularios(pessoa.getRgIE() + ""));
        editTextLogradouro.setText(FuncoesGerais.removeNullZeroFormularios(pessoa.getLogradouro()));
        editTextBairro.setText(FuncoesGerais.removeNullZeroFormularios(pessoa.getBairro()));
        editTextNumero.setText(FuncoesGerais.removeNullZeroFormularios(pessoa.getNumero() + ""));
        editTextCep.setText(FuncoesGerais.removeNullZeroFormularios(pessoa.getCep() + ""));
        spinnerEstado.setSelection((int)pessoa.getEstado().getId());
        editTextMunicipio.setText(FuncoesGerais.removeNullZeroFormularios(pessoa.getMunicipio().getNome()));
        editTextTelefone1.setText(FuncoesGerais.removeNullZeroFormularios(pessoa.getTelefone1() + ""));
        editTextTelefone2.setText(FuncoesGerais.removeNullZeroFormularios(pessoa.getTelefone2() + ""));
        editTextEmail.setText(FuncoesGerais.removeNullZeroFormularios(pessoa.getEmail()));
    }

    private void setValoresFormularioFornecedor(EmpresaCliente fornecedor) {
        setValoresFormularioPessoa(fornecedor.getPessoa());
    }

    private boolean verificaPreenchimentoCampos(){
        boolean retorno = true;
        if(editTextNome.getText().toString().length() < 5) {
            editTextNome.setError("Informe um nome correto");
            retorno = false;
        }

        int tamanhoCPF = FuncoesGerais.removePontosTracos(editTextCpf.getText().toString()).length();
        if(tamanhoCPF < 11) {
            editTextCpf.setError("CPF Inválido");
            retorno = false;
        }
        if(tamanhoCPF > 11 && tamanhoCPF < 15 ) {
            editTextCpf.setError("CNPJ Inválido");
            retorno = false;
        }

        if(editTextRg.getText().toString().length() < 6){
            String msg = tamanhoCPF == 11 ? "RG Inválido" : "IE Inválida";
            editTextRg.setError(msg);
            retorno = false;
        }

        if(editTextLogradouro.getText().toString().length() < 5){
            editTextLogradouro.setError("Informe um logradouro correto");
            retorno = false;
        }

        if(editTextBairro.getText().toString().equals("")){
            editTextBairro.setError("Informe seu bairro");
            retorno =  false;
        }

        if(editTextNumero.getText().toString().equals("")){
            editTextNumero.setError("Informe o número do endereço");
            retorno =  false;
        }

        if(editTextCep.getText().toString().length() < 9){
            editTextCep.setError("Informe o seu CEP correto");
            retorno =  false;
        }

        if(editTextMunicipio.getText().toString().equals("")){
            editTextMunicipio.setError("Informe o seu município");
            retorno =  false;
        }

        if(FuncoesGerais.removePontosTracos(editTextTelefone1.getText().toString()).length() < 10){
            editTextTelefone1.setError("Informe um telefone de contato válido!");
            retorno =  false;
        }

        if(!editTextEmail.getText().toString().contains("@") && !editTextEmail.getText().toString().contains(".com")){
            editTextEmail.setError("Informe um e-mail válido");
            retorno =  false;
        }

        if(!editTextSenha.getText().toString().equals("") && !editTextSenha.getText().toString().equals("")){
           if(!editTextSenha.getText().toString().equals(editTextConfirmarSenha.getText().toString())){
               editTextSenha.setText("");
               editTextSenha.setError("As senhas não são iguais");
               editTextConfirmarSenha.setText("");
               retorno = false;
           }
        } else {
            editTextSenha.setError("Informe uma senha");
            editTextConfirmarSenha.setError("Confirme a senha");
            retorno = false;
        }

        return retorno;
    }
}
