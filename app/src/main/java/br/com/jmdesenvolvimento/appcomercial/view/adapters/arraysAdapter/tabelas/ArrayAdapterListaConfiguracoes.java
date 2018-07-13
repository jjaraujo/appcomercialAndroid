package br.com.jmdesenvolvimento.appcomercial.view.adapters.arraysAdapter.tabelas;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.design.widget.TextInputEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

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
            "Configurar leitor de código de barras externo",
            "Alterar senha", "Aceitar venda sem cliente (por comanda/mesa)",
            "Venda sem estoque"};
    private int tamanhoList = textosConfiguracoes.length;
    private List<Tabela> list;
    public final static int TIPO_CONFIGURACAO = 1;
    public final static int TIPO_CONFIGURACAO_PAGAMENTO = 2;
    private int tipo;

    /**Informar um dos tipos statics da classe*/
    public ArrayAdapterListaConfiguracoes(Context context, ListView listView, List<Tabela> list, int tipo) {
        this.context = context;
        this.listView = listView;
        this.list = list;
        this.tipo = tipo;
    }

    @Override
    public int getCount() {
        if(tipo == TIPO_CONFIGURACAO) {
            return tamanhoList;
        } else{
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
        if(tipo == TIPO_CONFIGURACAO) {
            return addLinhasConfiguracoes(position,layoutInflater);
        } else{
            return addLinhasConfiguracoesPagamento(position,layoutInflater);
        }
    }

    private View addLinhasConfiguracoes(int position, LayoutInflater layoutInflater) {
        View view = layoutInflater.inflate(R.layout.list_model_configuracoes, null);
        TextView textoConfiguracao = view.findViewById(R.id.textViewNomeConfiguracaoPagamento);
        final Switch switchButton = view.findViewById(R.id.switchConfiguracao);
        textoConfiguracao.setText(textosConfiguracoes[position]);
        String nomeCampo;
        switchButton.setVisibility(View.INVISIBLE);

        switch (position) {
            case 5:
                switchButton.setVisibility(View.VISIBLE);
                nomeCampo = "vendaMesaComanda";
                switchButton.setChecked(VariaveisControle.configuracoesSimples.isVendaMesaComanda());
                setAcoesCliquesButton(switchButton, nomeCampo, " vendas por ");
                break;
            case 6:
                switchButton.setVisibility(View.VISIBLE);
                nomeCampo = "vendaSemEstoque";
                switchButton.setChecked(VariaveisControle.configuracoesSimples.isVendaSemEstoque());
                setAcoesCliquesButton(switchButton, nomeCampo, " vendas de produtos sem estoque");
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
                if(switchButton.isChecked()){
                    tipoPagamentos.setAceito(true);
                } else{
                    tipoPagamentos.setAceito(false);
                }
                dao.update(tipoPagamentos);
                dao.close();
            }
        });

        return view;
    }

    private void setAcoesCliquesButton(final Switch switchButton, final String nomeCampo, final String nomeCampotextoMensagem) {

        final Configuracoes configuracoes = VariaveisControle.configuracoesSimples;
        final HashMap<String, Object> map = configuracoes.getMapAtributos(false);

        switchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final View view = View.inflate(context,R.layout.dialog_pergunta_tipo_venda,null);
                final Spinner spinner = view.findViewById(R.id.spinnerPerguntaTipoVenda);
                final TextInputEditText editText = view.findViewById(R.id.editTextQtdAbrir);
                final String[] tipos = {"Mesa","Comanda"};
                ArrayAdapter<String> adapter = new ArrayAdapter<>(context,android.R.layout.simple_spinner_dropdown_item,tipos);
                spinner.setAdapter(adapter);
                if (switchButton.isChecked()) {
                    if(nomeCampo.equals("vendaMesaComanda")) {
                        new AlertDialog.Builder(context)
                                .setView(view)
                                .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (spinner.getSelectedItem() != null && !editText.getText().toString().equals("")) {
                                            String tipoVenda =  spinner.getSelectedItem().toString();
                                            map.put("vendaMesaComanda", true);
                                            map.put("nomeTipoVenda",tipoVenda);
                                            map.put("numeroDeMesasComandas", Integer.parseInt(editText.getText().toString()));
                                            configuracoes.setMapAtributos(map);
                                            final SQLiteDatabaseDao dao = new SQLiteDatabaseDao(context);
                                            dao.update(configuracoes);
                                            dao.close();
                                            FuncoesViewAndroid.addSnackBarToast(v,context,
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
                        map.put("vendaSemEstoque",true);
                        configuracoes.setMapAtributos(map);
                        final SQLiteDatabaseDao dao = new SQLiteDatabaseDao(context);
                        dao.update(configuracoes);
                        FuncoesViewAndroid.addSnackBarToast(switchButton, context,"Agora você aceita" + nomeCampotextoMensagem);
                        dao.close();
                    }
                } else {
                    if(nomeCampo.equals("vendaSemEstoque")) {
                        map.put("vendaSemEstoque",false);
                    } else{
                        map.put("vendaMesaComanda",false);
                    }
                    FuncoesViewAndroid.addSnackBarToast(switchButton,context,"Você optou por não aceitar mais" + nomeCampotextoMensagem);
                    configuracoes.setMapAtributos(map);
                    final SQLiteDatabaseDao dao = new SQLiteDatabaseDao(context);
                    dao.update(configuracoes);
                    dao.close();
                }
            }
        });
    }

    private void setAcoesCliquesList(Class novaActivity){
        Intent intent = new Intent(context, novaActivity);
        context.startActivity(intent);
    }


    public Drawable getImageDrawableResId(String imageId) {
        Resources resources = context.getResources();
        int drawableId = resources.getIdentifier(imageId, "drawable", context.getPackageName());
        Drawable dr = context.getResources().getDrawable(drawableId);
        return dr;
    }
}
