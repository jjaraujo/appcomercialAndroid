package br.com.jmdesenvolvimento.appcomercial.view.activitys.entidades.estoque;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import br.com.jmdesenvolvimento.appcomercial.R;
import br.com.jmdesenvolvimento.appcomercial.controller.funcionais.Funcoes;
import br.com.jmdesenvolvimento.appcomercial.controller.funcionais.VariaveisControle;
import br.com.jmdesenvolvimento.appcomercial.model.Tabela;
import br.com.jmdesenvolvimento.appcomercial.model.dao.SQLiteDatabaseDao;
import br.com.jmdesenvolvimento.appcomercial.model.entidades.Entidade;
import br.com.jmdesenvolvimento.appcomercial.model.entidades.cadastral.pessoas.Fornecedor;
import br.com.jmdesenvolvimento.appcomercial.model.entidades.cadastral.pessoas.Pessoa;
import br.com.jmdesenvolvimento.appcomercial.model.entidades.estoque.Cfop;
import br.com.jmdesenvolvimento.appcomercial.model.entidades.estoque.Csons;
import br.com.jmdesenvolvimento.appcomercial.model.entidades.estoque.Grupo;
import br.com.jmdesenvolvimento.appcomercial.model.entidades.estoque.Ncm;
import br.com.jmdesenvolvimento.appcomercial.model.entidades.estoque.Produto;
import br.com.jmdesenvolvimento.appcomercial.model.entidades.estoque.TipoItem;
import br.com.jmdesenvolvimento.appcomercial.model.entidades.estoque.Unidade;

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
    private Produto produto = new Produto();
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

        setIdsComponentes();
        ouveCliquesCarregaAutoCompleteEntidade();
        ouveCliquesItensEntidades();

        Intent intent = getIntent();
        produtoAntigo = (Produto) intent.getSerializableExtra("VISUALIZAR");
        if (produto != null && VariaveisControle.produtoEdicao == true) {
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
            menuInflater.inflate(R.menu.menu_formularios, menu);
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
                if (edicao == false) {
                    dao.insere(getValoresProduto());
                    finish();
                    Toast.makeText(this,"Produto " + produto.getId() +" adicionado!",Toast.LENGTH_SHORT).show();
                } else {
                    this.produto = getValoresProduto();
                    Produto produto = (Produto) Funcoes.getTabelaModificada(produtoAntigo, this.produto, new Produto());
                    dao.update(produto);
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
        editTextCustoCompra = findViewById(R.id.editTextCustoCompra);
        editTextQtd = findViewById(R.id.editTextQtd);
        editTextQtdMinima = findViewById(R.id.editTextQtdMinima);
        editTextComissao = findViewById(R.id.editTextComissao);
        editTextLucroBruto = findViewById(R.id.editTextLucroBruto);
        editTextDataCompra = findViewById(R.id.editTextDataCompra);
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
    }

    private Produto getValoresProduto() {
        produto.setAliquota(Funcoes.corrigeValoresCamposDouble(editTextAliquota.getText().toString()));
        produto.setCit(Funcoes.corrigeValoresCampos(editTextCit.getText().toString()));
        produto.setCodigoBarras(Funcoes.corrigeValoresCamposLong(editTextCodigoBarras.getText().toString()));
        produto.setComissao(Funcoes.corrigeValoresCamposDouble(editTextComissao.getText().toString()));
        produto.setCustoCompra(Funcoes.corrigeValoresCamposDouble(editTextCustoCompra.getText().toString()));
        produto.setDataCompra(Funcoes.corrigeValoresCampos(editTextDataCompra.getText().toString()));
        produto.setDescricao(Funcoes.corrigeValoresCampos(editTextDescricao.getText().toString()));
        produto.setCest(Funcoes.corrigeValoresCampos(editTextCest.getText().toString()));
        produto.setImpostoIbpt(Funcoes.corrigeValoresCamposDouble(editTextImpostoIbpt.getText().toString()));
        produto.setLucroBruto(Funcoes.corrigeValoresCamposDouble(editTextLucroBruto.getText().toString()));
        //      produto.setUltimaCompra(Funcoes.corrigeValoresCampos(c));
        produto.setNome_produto(Funcoes.corrigeValoresCampos(editTextNomeProduto.getText().toString()));
        produto.setPreco(Funcoes.corrigeValoresCamposDouble(editTextPreco.getText().toString()));
        produto.setQtd(Funcoes.corrigeValoresCamposInt(editTextQtd.getText().toString()));
        produto.setQtdMinima(Funcoes.corrigeValoresCamposInt(editTextQtdMinima.getText().toString()));
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
                editTextCfopNfce.setAdapter(getAdapterEntidade(new Cfop()));
            }
        });

        editTextGrupo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                editTextGrupo.setAdapter(getAdapterEntidade(new Grupo()));
            }
        });
        editTextFornecedor.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                editTextFornecedor.setAdapter(getAdapterEntidade(new Fornecedor()));
            }
        });
        editTextCsons.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                editTextCsons.setAdapter(getAdapterEntidade(new Csons()));
            }
        });
        editTextTipoItem.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                editTextTipoItem.setAdapter(getAdapterEntidade(new TipoItem()));
            }
        });
        editTextCsonsNfce.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                editTextCsonsNfce.setAdapter(getAdapterEntidade(new Csons()));
            }
        });
        editTextNcmTip.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                editTextNcmTip.setAdapter(getAdapterEntidade(new Ncm()));
            }
        });
        editTextUnidade.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                editTextUnidade.setAdapter(getAdapterEntidade(new Unidade()));
            }
        });
    }

    private ArrayAdapter<Entidade> getAdapterEntidade(Entidade entidade) {
        ProgressDialog dialog = new ProgressDialog(CadastroProdutoActivity.this);
        dialog.setMessage("Carregando dados");
        dialog.show();
        SQLiteDatabaseDao dao = new SQLiteDatabaseDao(CadastroProdutoActivity.this);

        List<Tabela> list = (List<Tabela>) dao.buscaTodos(entidade, null, false);
        ArrayAdapter adapter = new ArrayAdapter(CadastroProdutoActivity.this, android.R.layout.simple_dropdown_item_1line, list);
        dialog.setMessage("Concluído");
        dialog.dismiss();
        dao.close();
        return adapter;
    }

    private void setValoresCampos(Produto produto) {
        Log.i("setValoresCampos", "entrou");
        editTextAliquota.setText(Funcoes.removeNullZeroFormularios(produto.getAliquota() + ""));
        editTextCit.setText(Funcoes.removeNullZeroFormularios(produto.getCit() + ""));
        editTextCodigoBarras.setText(Funcoes.removeNullZeroFormularios(produto.getCodigoBarras() + ""));
        editTextComissao.setText(Funcoes.removeNullZeroFormularios(produto.getComissao() + ""));
        editTextCustoCompra.setText(Funcoes.removeNullZeroFormularios(produto.getCustoCompra() + ""));
        editTextDataCompra.setText(Funcoes.removeNullZeroFormularios(produto.getDataCompra() + ""));
        editTextDescricao.setText(Funcoes.removeNullZeroFormularios(produto.getDescricao() + ""));
        editTextCest.setText(Funcoes.removeNullZeroFormularios(produto.getCest() + ""));
        editTextImpostoIbpt.setText(Funcoes.removeNullZeroFormularios(produto.getImpostoIbpt() + ""));
        editTextLucroBruto.setText(Funcoes.removeNullZeroFormularios(produto.getLucroBruto() + ""));
        //      produto.setUltimaCompra(Funcoes.corrigeValoresCampos(c));
        editTextNomeProduto.setText(Funcoes.removeNullZeroFormularios(produto.getNome_produto() + ""));
        editTextPreco.setText(Funcoes.removeNullZeroFormularios(produto.getPreco() + ""));
        editTextQtd.setText(Funcoes.removeNullZeroFormularios(produto.getQtd() + ""));
        editTextQtdMinima.setText(Funcoes.removeNullZeroFormularios(produto.getQtdMinima() + ""));
        editTextCfopNfce.setText(Funcoes.removeNullZeroFormularios(Funcoes.isNull(produto.getCfop(), "nome_cfop", new Cfop()) + ""));
        editTextGrupo.setText(Funcoes.removeNullZeroFormularios(Funcoes.isNull(produto.getGrupo(), "nome_grupo", new Grupo()) + ""));

        editTextFornecedor.setText(
                Funcoes.removeNullZeroFormularios(
                        Funcoes.isNull(
                                ((Pessoa) Funcoes.isNull(
                                        produto.getFornecedor(), "pessoa", new Pessoa())), "nome_pessoa", null) + ""));
        editTextCsons.setText(Funcoes.removeNullZeroFormularios(Funcoes.isNull(produto.getCsons(), "nome_csons", new Csons()) + ""));
        editTextTipoItem.setText(Funcoes.removeNullZeroFormularios(Funcoes.isNull(produto.getTipoItem(), "nome_tipo_item", new TipoItem()) + ""));
        editTextCsonsNfce.setText(Funcoes.removeNullZeroFormularios(Funcoes.isNull(produto.getCsonsNfce(), "nome_csons", new Csons()) + ""));
        editTextNcmTip.setText(Funcoes.removeNullZeroFormularios(Funcoes.isNull(produto.getNcm(), "codigo", new Ncm()) + ""));
        editTextUnidade.setText(Funcoes.removeNullZeroFormularios(Funcoes.isNull(produto.getUnidade(), "nome_unidade", new Unidade()) + ""));
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
