package br.com.jmdesenvolvimento.appcomercial.view.adapters.arraysAdapter.tabelas;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.design.widget.Snackbar;
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
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;

import br.com.jmdesenvolvimento.appcomercial.R;
import br.com.jmdesenvolvimento.appcomercial.controller.funcionaisAndroid.VariaveisControleAndroid;
import br.com.jmdesenvolvimento.appcomercial.controller.funcoesGerais.VariaveisControleG;
import br.com.jmdesenvolvimento.appcomercial.model.Tabela;
import br.com.jmdesenvolvimento.appcomercial.model.dao.SQLiteDatabaseDao;
import br.com.jmdesenvolvimento.appcomercial.model.entidades.vendas.TipoPagamento;
import br.com.jmdesenvolvimento.appcomercial.model.tabelasIntermediarias.Configuracoes;
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
    public ArrayAdapterListaConfiguracoes(Context context,ListView listView, List<Tabela> list, int tipo) {
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
                nomeCampo = "vendaSemCliente";
                switchButton.setChecked(VariaveisControleG.configuracoesSimples.isVendaSemCliente());
                setAcoesCliquesButton(switchButton, nomeCampo, " vendas sem cliente");
                break;
            case 6:
                switchButton.setVisibility(View.VISIBLE);
                nomeCampo = "vendaSemEstoque";
                switchButton.setChecked(VariaveisControleG.configuracoesSimples.isVendaSemEstoque());
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
                dao.update(tipoPagamentos,false);
                dao.close();
            }
        });

        return view;
    }

    private void setAcoesCliquesButton(final Switch switchButton, final String nomeCampo, final String nomeCampotextoMensagem) {

        final Configuracoes configuracoes = VariaveisControleG.configuracoesSimples;
        final HashMap<String, Object> map = configuracoes.getMapAtributos(false);

        switchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Spinner spinner = new Spinner(context);
                final String[] tipos = {"Mesa","Comanda"};
                ArrayAdapter<String> adapter = new ArrayAdapter<>(context,android.R.layout.simple_spinner_dropdown_item,tipos);
                spinner.setAdapter(adapter);
                if (switchButton.isChecked()) {
                    if(nomeCampo.equals("vendaSemCliente")) {
                        new AlertDialog.Builder(context)
                                .setView(spinner)
                                .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (spinner.getSelectedItem() != null) {
                                            Snackbar.make(switchButton, "Agora você aceita" + nomeCampotextoMensagem, Snackbar.LENGTH_LONG).show();
                                            map.put("vendaSemCliente",1);
                                            map.put("nomeTipoVenda",spinner.getSelectedItem().toString());
                                            configuracoes.setMapAtributos(map);
                                            final SQLiteDatabaseDao dao = new SQLiteDatabaseDao(context);
                                            dao.update(configuracoes, false);
                                            dao.close();
                                        } else {
                                            Toast.makeText(context, "Ecolha um tipo de venda", Toast.LENGTH_LONG).show();
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
                        dao.update(configuracoes, false);
                        Snackbar.make(switchButton, "Agora você aceita" + nomeCampotextoMensagem, Snackbar.LENGTH_LONG).show();
                        dao.close();
                    }
                } else {
                    if(nomeCampo.equals("vendaSemEstoque")) {
                        map.put("vendaSemEstoque",false);
                    } else{
                        map.put("vendaSemCliente",false);
                    }
                    Snackbar.make(switchButton,"Você optou por não aceitar mais" + nomeCampotextoMensagem,Snackbar.LENGTH_LONG).show();
                    configuracoes.setMapAtributos(map);
                    final SQLiteDatabaseDao dao = new SQLiteDatabaseDao(context);
                    dao.update(configuracoes, false);
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
