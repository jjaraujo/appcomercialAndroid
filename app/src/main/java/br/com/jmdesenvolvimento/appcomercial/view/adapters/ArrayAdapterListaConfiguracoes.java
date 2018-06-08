package br.com.jmdesenvolvimento.appcomercial.view.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import br.com.jmdesenvolvimento.appcomercial.R;
import br.com.jmdesenvolvimento.appcomercial.controller.funcionais.Funcoes;
import br.com.jmdesenvolvimento.appcomercial.controller.funcionais.VariaveisControle;
import br.com.jmdesenvolvimento.appcomercial.model.dao.SQLiteDatabaseDao;
import br.com.jmdesenvolvimento.appcomercial.model.entidades.vendas.TipoPagamentos;
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
        View view = layoutInflater.inflate(R.layout.list_configuracoes, null);
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
                switchButton.setChecked(VariaveisControle.CONFIGURACOES_SIMPLES.isVendaSemCliente());
                setAcoesCliquesButton(switchButton, nomeCampo);
                break;
            case 6:
                switchButton.setVisibility(View.VISIBLE);
                nomeCampo = "vendaSemEstoque";
                switchButton.setChecked(VariaveisControle.CONFIGURACOES_SIMPLES.isVendaSemEstoque());
                setAcoesCliquesButton(switchButton, nomeCampo);
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

    private void setAcoesCliquesButton(final Switch switchButton, final String nomeCampo) {
        final Configuracoes configuracoes = VariaveisControle.CONFIGURACOES_SIMPLES;
        final HashMap<String, Object> map = configuracoes.getMapAtributos();
        final SQLiteDatabaseDao dao = new SQLiteDatabaseDao(context);
        switchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (switchButton.isChecked()) {
                    map.put(nomeCampo, 1);
                } else {
                    map.put(nomeCampo, 0);
                }
                configuracoes.setMapAtributos(map);
                dao.update(configuracoes, false);
            }
        });
    }

    private void setAcoesCliquesList(Class novaActivity){

        Intent intent = new Intent(context, novaActivity);
        context.startActivity(intent);
    }
}
