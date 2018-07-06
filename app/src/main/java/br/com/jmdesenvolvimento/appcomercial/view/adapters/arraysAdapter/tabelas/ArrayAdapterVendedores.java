package br.com.jmdesenvolvimento.appcomercial.view.adapters.arraysAdapter.tabelas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jmdesenvolvimento.appcomercial.model.Tabela;
import com.jmdesenvolvimento.appcomercial.model.entidades.cadastral.pessoas.Funcionario;

import java.util.List;

import br.com.jmdesenvolvimento.appcomercial.R;

public class ArrayAdapterVendedores extends BaseAdapter {
    private List<Tabela> list;
    private Context context;

    public ArrayAdapterVendedores(Context context, List<?> listTabela) {
        this.list = (List<Tabela>) listTabela;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return list.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.list_model_clientes, null);
        Funcionario fornecedor = (Funcionario) list.get(position);
        TextView nome = view.findViewById(R.id.textViewListClientesNome);
        nome.setText(fornecedor.getPessoa().getNome());
        TextView cpf = view.findViewById(R.id.textViewListClientesCpf);
        cpf.setText(fornecedor.getPessoa().getCpfCNPJ() + "");
        TextView ultimaCompra = view.findViewById(R.id.textViewListClientesUltimaCompra);
//        if(fornecedor.getUltimaVenda().equals("")){
//            ultimaCompra.setText("");
//        } else{
//            ultimaCompra.setText(fornecedor.getUltimaVenda());
//        }
        return view;
    }

}