package br.com.jmdesenvolvimento.appcomercial.view.adapters.arraysAdapter.tabelas;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.design.widget.TextInputEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Switch;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import app.jm.funcional.model.entidades.vendas.Venda;
import br.com.jmdesenvolvimento.appcomercial.R;;
import app.jm.funcional.controller.VariaveisControle;
import app.jm.funcional.model.Configuracoes;
import app.jm.funcional.model.Tabela;

import br.com.jmdesenvolvimento.appcomercial.controller.funcionaisAndroid.FuncoesViewAndroid;
import br.com.jmdesenvolvimento.appcomercial.model.dao.SQLiteDatabaseDao;
import app.jm.funcional.model.entidades.vendas.TipoPagamento;
import br.com.jmdesenvolvimento.appcomercial.view.activitys.configuracoes.ConfigurarPagamentosActivity;

public class ArrayAdapterListaConfiguracoes extends BaseAdapter {

    private Context context;
    private ListView listView;
    private String[] textosConfiguracoes = {"Meu plano", "Pagamentos aceitos", "Configurar impressora",
            "Configurar leitor de código de barras externo","Personalizar encerramento do caixa",
            "Alterar senha", "Definir tela inicial", "Backup em nuvem", "Aceitar venda por comanda/mesa",
            "Venda sem estoque"};
    private int tamanhoList = textosConfiguracoes.length;
    private List<Tabela> list;
    public final static int TIPO_CONFIGURACAO = 1;
    public final static int TIPO_CONFIGURACAO_PAGAMENTO = 2;
    private int tipo;


    /**
     * Informar um dos tipos statics da classe
     */
    public ArrayAdapterListaConfiguracoes(Context context, ListView listView, List<Tabela> list, int tipo) {
        this.context = context;
        this.listView = listView;
        this.list = list;
        this.tipo = tipo;
    }

    @Override
    public int getCount() {
        if (tipo == TIPO_CONFIGURACAO) {
            return tamanhoList;
        } else {
            return list.size();
        }
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        if (tipo == TIPO_CONFIGURACAO) {
            return addLinhasConfiguracoes(position, layoutInflater);
        } else {
            return addLinhasConfiguracoesPagamento(position, layoutInflater);
        }
    }

    private View addLinhasConfiguracoes(int position, LayoutInflater layoutInflater) {
        View view = layoutInflater.inflate(R.layout.list_model_configuracoes, null);
        TextView textoConfiguracao = view.findViewById(R.id.textViewNomeConfiguracaoPagamento);
        final Switch switchButton = view.findViewById(R.id.switchConfiguracao);
        textoConfiguracao.setText(textosConfiguracoes[position]);
        switchButton.setVisibility(View.INVISIBLE);

        switch (position) {

            case 7:
                switchButton.setVisibility(View.VISIBLE);
                switchButton.setChecked(VariaveisControle.configuracoesSimples.isBackupEmNuvem());
                setAcoesButtonBackup(switchButton);
                break;
            case 8:
                view.setClickable(true);
                switchButton.setVisibility(View.VISIBLE);
                switchButton.setChecked(VariaveisControle.configuracoesSimples.isVendaMesaComanda());
                setAcoesCliqueButtonVendasComanda(switchButton);
                break;
            case 9:
                view.setClickable(true);
                switchButton.setVisibility(View.VISIBLE);
                switchButton.setChecked(VariaveisControle.configuracoesSimples.isVendaSemEstoque());
                setAcoesCliquesButton(switchButton);
                break;
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        break;
                    case 1:
                        setAcoesCliquesList(ConfigurarPagamentosActivity.class);
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    case 4:

                        break;
                    case 5:
                        setAcoesEscolherTelaInicial(view);
                        break;
                }

            }
        });
        return view;
    }

    private View addLinhasConfiguracoesPagamento(int position, LayoutInflater layoutInflater) {
        final TipoPagamento tipoPagamentos = (TipoPagamento) list.get(position);

        View view = layoutInflater.inflate(R.layout.list_model_configuracoes_pagamentos, null);
        TextView textoConfiguracao = view.findViewById(R.id.textViewNomeConfiguracaoPagamento);
        ImageView imageView = view.findViewById(R.id.imageViewIconeConfiguracaoPagamento);
        final Switch switchButton = view.findViewById(R.id.switchConfiguracao);
        textoConfiguracao.setText(tipoPagamentos.getNome());
        imageView.setImageAlpha(tipoPagamentos.getIdIcone());

        switchButton.setChecked(tipoPagamentos.isAceito());

        switchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabaseDao dao = new SQLiteDatabaseDao(context);
                if (switchButton.isChecked()) {
                    tipoPagamentos.setAceito(true);
                } else {
                    tipoPagamentos.setAceito(false);
                }
                dao.update(tipoPagamentos);
                dao.close();
            }
        });

        return view;
    }


    private void setAcoesCliqueButtonVendasComanda(final Switch switchButton) {
        final Configuracoes configuracoes = VariaveisControle.configuracoesSimples;
        final HashMap<String, Object> map = configuracoes.getMapAtributos(false);
        final View view = View.inflate(context, R.layout.dialog_pergunta_tipo_venda, null);
        final Spinner spinner = view.findViewById(R.id.spinnerPerguntaTipoVenda);
        final String[] tipos = {"Mesa", "Comanda"};
        ArrayAdapter adapter = new ArrayAdapter(context,android.R.layout.simple_spinner_dropdown_item,tipos);
        spinner.setAdapter(adapter);
        final TextInputEditText editText = view.findViewById(R.id.editTextQtdAbrir);
        switchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(view.getParent() != null)
                    ((ViewGroup)view.getParent()).removeView(view);

                if (switchButton.isChecked()) {
                   AlertDialog alertDialog = new AlertDialog.Builder(context)
                            .setView(view)
                            .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    final SQLiteDatabaseDao dao = new SQLiteDatabaseDao(context);

                                    if (spinner.getSelectedItem() != null && !editText.getText().toString().equals("")) {
                                        String tipoVenda = spinner.getSelectedItem().toString();
                                        configuracoes.setVendaMesaComanda(true);
                                        configuracoes.setNomeTipoVenda(tipoVenda);
                                        configuracoes.setNumeroDeMesasComandas(Integer.parseInt(editText.getText().toString()));
                                        dao.update(configuracoes);
                                        dao.close();
                                        FuncoesViewAndroid.addSnackBarToast(v, context,
                                                "Agora você aceita venda por " + tipoVenda.toLowerCase());
                                    } else {
                                        dialog.cancel();
                                        FuncoesViewAndroid.addSnackBarToast(v, context,
                                                "Ecolha um tipo de venda ou informe a quantidade para abrir");
                                        switchButton.setChecked(false);
                                    }
                                }
                            })
                            .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switchButton.setChecked(false);
                                    dialog.dismiss();
                                }
                            })
                            .setTitle("Suas vendas são por número de mesa ou comanda?")
                            .setIcon(R.drawable.icone_pergunta)
                            .setCancelable(false)
                            .show();
                } else {
                    SQLiteDatabaseDao dao = new SQLiteDatabaseDao(context);
                    String where = " numeroMesaComanda IS NOT NULL AND numeroMesaComanda > 0 AND " + Tabela.getDataExclusaoNome() + " IS NULL";
                    Venda vendaAberta = (Venda) dao.select(new Venda(), null, where, null, null, null);

                    if (vendaAberta != null) {
                        FuncoesViewAndroid.addAlertDialogAlerta(context, "Falha", "Há vendas abertas. Encerre-as antes de modificar esta configuração");
                        switchButton.setChecked(true);
                    }
                    configuracoes.setVendaMesaComanda(false);
                    FuncoesViewAndroid.addSnackBarToast(switchButton, context, "Você optou por não aceitar mais venda por " + configuracoes.getNomeTipoVenda());
                    dao.update(configuracoes);
                    dao.close();
                }
            }
        });

    }

    private void setAcoesCliquesButton(final Switch switchButton) {

        final Configuracoes configuracoes = VariaveisControle.configuracoesSimples;
        final HashMap<String, Object> map = configuracoes.getMapAtributos(false);

        switchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (switchButton.isChecked()) {

                    configuracoes.setVendaSemEstoque(true);
                    final SQLiteDatabaseDao dao = new SQLiteDatabaseDao(context);
                    dao.update(configuracoes);
                    FuncoesViewAndroid.addSnackBarToast(switchButton, context, "Agora você aceita venda sem estoque");
                    dao.close();

                } else {
                    configuracoes.setVendaSemEstoque(false);
                    SQLiteDatabaseDao dao = new SQLiteDatabaseDao(context);
                    FuncoesViewAndroid.addSnackBarToast(switchButton, context, "Você optou por não aceitar mais venda por comandasem estoque");
                    dao.update(configuracoes);
                    dao.close();
                }
            }
        });
    }

    private void setAcoesCliquesList(Class novaActivity) {
        Intent intent = new Intent(context, novaActivity);
        context.startActivity(intent);
    }

    private void setAcoesEscolherTelaInicial(View view) {

        View viewEscolha = View.inflate(context, R.layout.dialog_escolher_tela_inicial, null);
        new AlertDialog.Builder(context)
                .setView(viewEscolha)
                .setTitle("Escolha a tela que vai ser exibida ao abrir o aplicativo")
                .show();
    }

    private void setAcoesButtonBackup(final Switch switchButton){
        switchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!switchButton.isChecked()){
                   new AlertDialog.Builder(context)
                           .setTitle("Atenção")
                           .setMessage("Desative esta ferramenta somente se todos os dados não forem importantes e outras pessoas da loja não forem vê-los.")
                           .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    SQLiteDatabaseDao dao = new SQLiteDatabaseDao(context);
                                    Configuracoes configuracoes = VariaveisControle.configuracoesSimples;
                                    configuracoes.setBackupEmNuvem(false);
                                    dao.update(configuracoes);
                                    dao.close();
                                    FuncoesViewAndroid.addAlertDialogAlerta(context,"Ateção","Agora seus dados poderão ser perdidos caso aconteça algo com o dispositivo");
                                }
                            })
                           .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                               @Override
                               public void onClick(DialogInterface dialogInterface, int i) {
                                   dialogInterface.cancel();
                                   switchButton.setChecked(true);
                                   FuncoesViewAndroid.addAlertDialogSucesso(context,"Parabéns","Seus continuam seguros!");
                               }
                           })
                           .show();
                } else{
                    FuncoesViewAndroid.addAlertDialogSucesso(context,"Parabéns","Agora seus dados estão seguros!");
                    SQLiteDatabaseDao dao = new SQLiteDatabaseDao(context);
                    Configuracoes configuracoes = VariaveisControle.configuracoesSimples;
                    configuracoes.setBackupEmNuvem(true);
                    dao.update(configuracoes);
                    dao.close();
                }
            }
        });
    }

    private void personalizarEncerramentoCaixa(){

    }

    public Drawable getImageDrawableResId(String imageId) {
        Resources resources = context.getResources();
        int drawableId = resources.getIdentifier(imageId, "drawable", context.getPackageName());
        Drawable dr = context.getResources().getDrawable(drawableId);
        return dr;
    }
}
