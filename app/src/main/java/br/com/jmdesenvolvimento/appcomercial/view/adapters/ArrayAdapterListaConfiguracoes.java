package br.com.jmdesenvolvimento.appcomercial.view.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import br.com.jmdesenvolvimento.appcomercial.R;
import br.com.jmdesenvolvimento.appcomercial.controller.funcionais.VariaveisControle;
import br.com.jmdesenvolvimento.appcomercial.model.dao.SQLiteDatabaseDao;
import br.com.jmdesenvolvimento.appcomercial.model.tabelas.configuracoes.Configuracoes;
import br.com.jmdesenvolvimento.appcomercial.view.activitys.entidades.ConfigurarPagamentosActivity;

public class ArrayAdapterListaConfiguracoes extends BaseAdapter {

    private Context context;
    private ListView listView;
    private String[] textosConfiguracoes = {"Meu plano", "Pagamentos aceitos", "Configurar impressora",
            "Configurar leitor de código de barras externo",
            "Alterar senha", "Aceitar venda sem cliente (por comanda/mesa)",
            "Venda sem estoque"};
    private int tamanhoList = textosConfiguracoes.length;

    public ArrayAdapterListaConfiguracoes(Context context,ListView listView) {
        this.context = context;
        this.listView = listView;
    }

    @Override
    public int getCount() {
        return tamanhoList;
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
        View view = layoutInflater.inflate(R.layout.list_model_configuracoes, null);
        addLinhas(position, view);
        return view;
    }

    private void addLinhas(int position, View view) {
        TextView textoConfiguracao = view.findViewById(R.id.textViewNomeConfiguracao);
        final Switch switchButton = view.findViewById(R.id.switchConfiguracao);
        textoConfiguracao.setText(textosConfiguracoes[position]);
        String nomeCampo;
        switchButton.setVisibility(View.INVISIBLE);

        switch (position) {
            case 5:
                switchButton.setVisibility(View.VISIBLE);
                nomeCampo = "vendaSemCliente";
                switchButton.setChecked(VariaveisControle.configuracoesSimples.isVendaSemCliente());
                setAcoesCliquesButton(switchButton, nomeCampo, " vendas sem cliente");
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
    }

    private void setAcoesCliquesButton(final Switch switchButton, final String nomeCampo, final String nomeCampotextoMensagem) {

        final Configuracoes configuracoes = VariaveisControle.configuracoesSimples;
        final HashMap<String, Object> map = configuracoes.getMapAtributos();
        final SQLiteDatabaseDao dao = new SQLiteDatabaseDao(context);
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
                                            configuracoes.setNomeTipoVenda(spinner.getSelectedItem().toString());
                                            Snackbar.make(switchButton, "Agora você aceita" + nomeCampotextoMensagem, Snackbar.LENGTH_LONG).show();
                                            configuracoes.setVendaSemCliente(true);
                                            map.put("vendaSemCliente",1);
                                            configuracoes.setMapAtributos(map);
                                            dao.update(configuracoes, false);
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
                        map.put("vendaSemEstoque",1);
                        configuracoes.setMapAtributos(map);
                        dao.update(configuracoes, false);
                        Snackbar.make(switchButton, "Agora você aceita" + nomeCampotextoMensagem, Snackbar.LENGTH_LONG).show();
                    }
                } else {
                    if(nomeCampo.equals("vendaSemEstoque")) {
                        map.put("vendaSemEstoque",0);
                    } else{
                        map.put("vendaSemCliente",0);
                    }
                    Snackbar.make(switchButton,"Você optou por não aceitar mais" + nomeCampotextoMensagem,Snackbar.LENGTH_LONG).show();
                    configuracoes.setMapAtributos(map);
                    dao.update(configuracoes, false);
                }
            }
        });
    }

    private void setAcoesCliquesList(Class novaActivity){

        Intent intent = new Intent(context, novaActivity);
        context.startActivity(intent);
    }
}
