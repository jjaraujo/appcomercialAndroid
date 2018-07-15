package br.com.jmdesenvolvimento.appcomercial.view.activitys.entidades.estoque;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.util.List;

import br.com.jmdesenvolvimento.appcomercial.R;;
import br.com.jmdesenvolvimento.appcomercial.controller.funcionaisAndroid.FuncoesViewAndroid;
import app.jm.funcional.controller.funcoesGerais.FuncoesGerais;
import br.com.jmdesenvolvimento.appcomercial.controller.funcionaisAndroid.VariaveisControleAndroid;
import app.jm.funcional.model.Tabela;
import br.com.jmdesenvolvimento.appcomercial.controller.services.conexoes.ConexaoServiceCadastraProduto;
import br.com.jmdesenvolvimento.appcomercial.model.dao.SQLiteDatabaseDao;
import app.jm.funcional.model.entidades.cadastral.pessoas.Fornecedor;
import app.jm.funcional.model.entidades.estoque.Cfop;
import app.jm.funcional.model.entidades.estoque.Csons;
import app.jm.funcional.model.entidades.estoque.Grupo;
import app.jm.funcional.model.entidades.estoque.Ncm;
import app.jm.funcional.model.entidades.estoque.Produto;
import app.jm.funcional.model.entidades.estoque.TipoItem;
import app.jm.funcional.model.entidades.estoque.Unidade;
import br.com.jmdesenvolvimento.appcomercial.view.activitys.LeitorCodigoBarrasActivity;

public class CadastroProdutoActivity extends AppCompatActivity {

    private TextInputLayout inputTextNomeProduto;
    private TextInputEditText editTextDescricao;
    private AutoCompleteTextView editTextUnidade;
    private TextInputEditText editTextPreco;
    private AutoCompleteTextView editTextGrupo;
    private TextInputEditText editTextCodigoBarras;
    private TextInputEditText editTextCustoCompra;
    private TextInputEditText editTextQtd;
    private TextInputEditText editTextQtdMinima;
    private TextInputEditText editTextComissao;
    private TextInputEditText editTextLucroBruto;
    private TextInputEditText editTextDataCompra;
    private AutoCompleteTextView editTextFornecedor;
    private TextInputEditText editTextImpostoIbpt;
    private AutoCompleteTextView editTextCsons;
    private TextInputEditText editTextCit;
    private AutoCompleteTextView editTextTipoItem;
    private AutoCompleteTextView editTextNcmTip;
    private TextInputEditText editTextCest;
    private AutoCompleteTextView editTextCfopNfce;
    private AutoCompleteTextView editTextCsonsNfce;
    private TextInputEditText editTextAliquota;
    private Produto produto;
    private Produto produtoAntigo;
    private Button buttonAddGrupo;
    private Button buttonCameraCodigo;
    private boolean visualizacao;
    private boolean edicao;
    private Menu menu;
    private TextInputEditText editTextNomeProduto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_produto);
        setTitle("Cadastro de Produtos");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        setIdsComponentes();
        ouveCliquesCarregaAutoCompleteEntidade();
        ouveCliquesItensEntidades();

        Intent intent = getIntent();
        produtoAntigo = (Produto) intent.getSerializableExtra("produtoVisualizar");
        if (intent.getSerializableExtra("tipoAbertura").equals("visualizar")) {
            visualizacao = true;
            camposNotAcessible();
            setValoresCampos(produtoAntigo);
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
        switch (item.getItemId()) {
            case android.R.id.home: // botao voltar
                finish();
                break;
            case R.id.menu_formularios_salvar:
                SQLiteDatabaseDao dao = new SQLiteDatabaseDao(this);
                if (edicao == false) {

                   new ConexaoServiceCadastraProduto(this,getValoresProduto()).execute();
                    Toast.makeText(this,"Produto " + produto.getId() +" adicionado!",Toast.LENGTH_SHORT).show();

                } else {
                    this.produto = getValoresProduto();
                //    Produto produto = (Produto) FuncoesGerais.getTabelaModificada(produtoAntigo, this.produto, new Produto());
                    dao.update(this.produto);
                    finish();
                    Toast.makeText(this,"Produto " + produto.getId() +" alterado!",Toast.LENGTH_SHORT).show();
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
        return super.onOptionsItemSelected(item);
    }

    private void setIdsComponentes() {
        inputTextNomeProduto = findViewById(R.id.inputTextNomeProduto);
        editTextNomeProduto = findViewById(R.id.editTextNomeProduto);
        editTextDescricao = findViewById(R.id.editTextDescricaoProduto);
        editTextUnidade = findViewById(R.id.autoCompleteTextViewUnidade);
        editTextPreco = findViewById(R.id.editTextPreco);
        editTextGrupo = findViewById(R.id.autoCompleteTextViewGrupo);
        editTextCodigoBarras = findViewById(R.id.editTextCodigoBarras);
        VariaveisControleAndroid.editTextCodigoBarrasCadastroProduto = editTextCodigoBarras;
        editTextCustoCompra = findViewById(R.id.editTextCustoCompra);
        editTextQtd = findViewById(R.id.editTextQtd);
        editTextQtdMinima = findViewById(R.id.editTextQtdMinima);
        editTextComissao = findViewById(R.id.editTextComissao);
        editTextLucroBruto = findViewById(R.id.editTextLucroBruto);
        editTextDataCompra = findViewById(R.id.editTextDataCompra);
        FuncoesViewAndroid.addCalendar(this,editTextDataCompra);
        editTextFornecedor = findViewById(R.id.autoCompleteTextViewFornecedor);
        editTextImpostoIbpt = findViewById(R.id.editTextImposto);
        editTextCsons = findViewById(R.id.autoCompleteTextViewCSOSN);
        editTextCit = findViewById(R.id.editTextCIT);
        editTextTipoItem = findViewById(R.id.autoCompleteTextViewTipoItem);
        editTextNcmTip = findViewById(R.id.autoCompleteTextViewNCMTIP);
        editTextCfopNfce = findViewById(R.id.autoCompleteTextViewCFOP);
        editTextAliquota = findViewById(R.id.editTextAliquotaParaNFC);
        editTextCest = findViewById(R.id.editTextCEST);
        editTextCsonsNfce = findViewById(R.id.autoCompleteTextViewCSOSNparaNFC);
        buttonAddGrupo = findViewById(R.id.buttonAddGrupo);
        buttonCameraCodigo = findViewById(R.id.buttonCameraCodigoBarras);
        buttonCameraCodigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CadastroProdutoActivity.this, LeitorCodigoBarrasActivity.class);
                startActivity(intent);
            }
        });
    }

    private Produto getValoresProduto() {
        produto = produtoAntigo == null ? new Produto() : produtoAntigo;
        produto.setAliquota(FuncoesGerais.corrigeValoresCamposDouble(editTextAliquota.getText().toString()));
        produto.setCit(FuncoesGerais.corrigeValoresCampos(editTextCit.getText().toString()));
        produto.setCodigoBarras(FuncoesGerais.corrigeValoresCamposLong(editTextCodigoBarras.getText().toString()));
        produto.setComissao(FuncoesGerais.corrigeValoresCamposDouble(editTextComissao.getText().toString()));
        produto.setCustoCompra(FuncoesGerais.corrigeValoresCamposDouble(editTextCustoCompra.getText().toString()));
        produto.setDataCompra(FuncoesGerais.stringToCalendar(editTextDataCompra.getText().toString(), FuncoesGerais.ddMMyyyy));
        produto.setDescricao(FuncoesGerais.corrigeValoresCampos(editTextDescricao.getText().toString()));
        produto.setCest(FuncoesGerais.corrigeValoresCampos(editTextCest.getText().toString()));
        produto.setImpostoIbpt(FuncoesGerais.corrigeValoresCamposDouble(editTextImpostoIbpt.getText().toString()));
        produto.setLucroBruto(FuncoesGerais.corrigeValoresCamposDouble(editTextLucroBruto.getText().toString()));
        //      produto.setUltimaCompra(FuncoesGerais.corrigeValoresCampos(c));
        produto.setNome_produto(FuncoesGerais.corrigeValoresCampos(editTextNomeProduto.getText().toString()));
        produto.setPreco(FuncoesGerais.corrigeValoresCamposDouble(editTextPreco.getText().toString()));
        produto.setQtd(FuncoesGerais.corrigeValoresCamposInt(editTextQtd.getText().toString()));
        produto.setQtdMinima(FuncoesGerais.corrigeValoresCamposInt(editTextQtdMinima.getText().toString()));
        return produto;
    }

    // pega a entidade escolhida pelo usuário, no autoComplete, e adiciona no produto
    private void ouveCliquesItensEntidades() {

        editTextNcmTip.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Ncm ncm = (Ncm) parent.getItemAtPosition(position);
                produto.setNcm(ncm);
            }
        });

        editTextTipoItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TipoItem tipoItem = (TipoItem) parent.getItemAtPosition(position);
                produto.setTipoItem(tipoItem);
            }
        });

        editTextCsonsNfce.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Csons csons = (Csons) parent.getItemAtPosition(position);
                produto.setCsonsNfce(csons);
            }
        });

        editTextCfopNfce.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cfop cfop = (Cfop) parent.getItemAtPosition(position);
                produto.setCfop(cfop);
            }
        });

        editTextUnidade.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Unidade unidade = (Unidade) parent.getItemAtPosition(position);
                produto.setUnidade(unidade);
            }
        });

        editTextCsons.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Csons csons = (Csons) parent.getItemAtPosition(position);
                produto.setCsons(csons);
            }
        });

        editTextFornecedor.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Fornecedor fornecedor = (Fornecedor) parent.getItemAtPosition(position);
                produto.setFornecedor(fornecedor);
            }
        });

        editTextGrupo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Grupo grupo = (Grupo) parent.getItemAtPosition(position);
                produto.setGrupo(grupo);
            }
        });

    }

    // quando o autoComplete for clicado, os valores serão buscados na base
    private void ouveCliquesCarregaAutoCompleteEntidade() {

        editTextCfopNfce.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                editTextCfopNfce.setAdapter(getAdapterEntidade(new Cfop(),"id",""));
            }
        });

        editTextGrupo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                editTextGrupo.setAdapter(getAdapterEntidade(new Grupo(),"id",""));
            }
        });

        editTextFornecedor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                editTextFornecedor.setAdapter(getAdapterFonecedor(s.toString()));
            }
        });

        editTextCsons.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                editTextCsons.setAdapter(getAdapterEntidade(new Csons(),"id",""));
            }
        });
        editTextTipoItem.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                editTextTipoItem.setAdapter(getAdapterEntidade(new TipoItem(),"id",""));
            }
        });
        editTextCsonsNfce.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                editTextCsonsNfce.setAdapter(getAdapterEntidade(new Csons(),"id",""));
            }
        });
        editTextNcmTip.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                editTextNcmTip.setAdapter(getAdapterEntidade(new Ncm(),"id",""));
            }
        });
        editTextUnidade.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                editTextUnidade.setAdapter(getAdapterEntidade(new Unidade(),"id",""));
            }
        });
    }

    /**Monta o adapter genérico para as tabelas que serão listadas no formulário de cadastro através de autocomplete*/
    private ArrayAdapter<Tabela> getAdapterEntidade(Tabela entidade, String colunaFiltro, String sequencia) {
        ProgressDialog dialog = new ProgressDialog(CadastroProdutoActivity.this);
        dialog.show();
        SQLiteDatabaseDao dao = new SQLiteDatabaseDao(CadastroProdutoActivity.this);
        List<Tabela> list = (List<Tabela>) dao.selectAll(entidade, colunaFiltro +" like '%" +sequencia+"%'" , false);
        ArrayAdapter adapter = new ArrayAdapter(CadastroProdutoActivity.this, android.R.layout.simple_dropdown_item_1line, list);
        dialog.dismiss();
        dao.close();
        return adapter;
    }

    private  ArrayAdapter<Fornecedor> getAdapterFonecedor(String s){
        SQLiteDatabaseDao dao = new SQLiteDatabaseDao(CadastroProdutoActivity.this);
        List<Tabela> list = dao.buscaPessoaPorNomeCpf(new Fornecedor(), "nomePessoa",s);
        ArrayAdapter adapter = new ArrayAdapter(CadastroProdutoActivity.this, android.R.layout.simple_dropdown_item_1line, list);
        dao.close();
        return adapter;
    }

    private void setValoresCampos(Produto produto) {
        Log.i("setValoresCampos", "entrou");
        editTextAliquota.setText(FuncoesGerais.removeNullZeroFormularios(produto.getAliquota() + ""));
        editTextCit.setText(FuncoesGerais.removeNullZeroFormularios(produto.getCit() + ""));
        editTextCodigoBarras.setText(FuncoesGerais.removeNullZeroFormularios(produto.getCodigoBarras() + ""));
        editTextComissao.setText(FuncoesGerais.removeNullZeroFormularios(produto.getComissao() + ""));
        editTextCustoCompra.setText(FuncoesGerais.removeNullZeroFormularios(produto.getCustoCompra() + ""));
        editTextDataCompra.setText(FuncoesGerais.removeNullZeroFormularios(produto.getDataCompra() + ""));
        editTextDescricao.setText(FuncoesGerais.removeNullZeroFormularios(produto.getDescricao() + ""));
        editTextCest.setText(FuncoesGerais.removeNullZeroFormularios(produto.getCest() + ""));
        editTextImpostoIbpt.setText(FuncoesGerais.removeNullZeroFormularios(produto.getImpostoIbpt() + ""));
        editTextLucroBruto.setText(FuncoesGerais.removeNullZeroFormularios(produto.getLucroBruto() + ""));
        //      produto.setUltimaCompra(FuncoesGerais.corrigeValoresCampos(c));
        editTextNomeProduto.setText(FuncoesGerais.removeNullZeroFormularios(produto.getNome_produto() + ""));
        editTextPreco.setText(FuncoesGerais.removeNullZeroFormularios(produto.getPreco() + ""));
        editTextQtd.setText(FuncoesGerais.removeNullZeroFormularios(produto.getQtd() + ""));
        editTextQtdMinima.setText(FuncoesGerais.removeNullZeroFormularios(produto.getQtdMinima() + ""));
        editTextCfopNfce.setText(FuncoesGerais.removeNullZeroFormularios(produto.getCfop().getNome_cfop()));
        editTextGrupo.setText(FuncoesGerais.removeNullZeroFormularios(produto.getGrupo().getNome_grupo()));

        editTextFornecedor.setText(FuncoesGerais.removeNullZeroFormularios(produto.getFornecedor().getPessoa().getNome()));
        editTextCsons.setText(FuncoesGerais.removeNullZeroFormularios(produto.getCsons().getNome_csons()));
        editTextTipoItem.setText(FuncoesGerais.removeNullZeroFormularios(produto.getTipoItem().getNome_tipo_item()));
        editTextCsonsNfce.setText(FuncoesGerais.removeNullZeroFormularios(produto.getCsonsNfce().getNome_csons()));
        editTextNcmTip.setText(FuncoesGerais.removeNullZeroFormularios(produto.getNcm()+""));
        editTextUnidade.setText(FuncoesGerais.removeNullZeroFormularios(produto.getUnidade().getNome_unidade()));
        Log.i("setValoresCampos", "saiu");
    }


    private void camposNotAcessible() {
        Log.i("camposNotAcessible", "entrou");
        buttonCameraCodigo.setVisibility(View.INVISIBLE);
        buttonAddGrupo.setVisibility(View.INVISIBLE);
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field f : fields) {
            if (f.getType().getSimpleName().equals("TextInputEditText") ||
                    f.getType().getSimpleName().equals("AutoCompleteTextView")) {
                f.setAccessible(true);
                try {
                    EditText v = (EditText) f.get(this);
                    v.setEnabled(false);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        Log.i("camposNotAcessible", "saiu");
    }

    private void camposAcessible() {
        buttonCameraCodigo.setVisibility(View.VISIBLE);
        buttonAddGrupo.setVisibility(View.VISIBLE);
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field f : fields) {
            if (f.getType().getSimpleName().equals("TextInputEditText") ||
                    f.getType().getSimpleName().equals("AutoCompleteTextView")) {
                f.setAccessible(true);
                try {
                    EditText v = (EditText) f.get(this);
                    if (v.getText().toString().equals(" "))
                        v.setText("");
                    v.setEnabled(true);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        visualizacao = false;
        onCreateOptionsMenu(menu);
    }

}
