package br.com.jmdesenvolvimento.appcomercial.view.adapters.adaptersFragments;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import br.com.jmdesenvolvimento.appcomercial.view.fragments.FragmentClientes;
import br.com.jmdesenvolvimento.appcomercial.view.fragments.FragmentFornecedor;
import br.com.jmdesenvolvimento.appcomercial.view.fragments.FragmentVendedor;

public class AdapterFragmentActivityPessoas extends FragmentStatePagerAdapter{

    String[] titulos = {"Clientes","Fornecedores","Vendedores"};

    private FragmentClientes fragmentCliente = new FragmentClientes(0,"cliente");
    private FragmentFornecedor fragmentFornecedor = new FragmentFornecedor();
    private FragmentVendedor fragmentVendedor = new FragmentVendedor(2,"pessoa");

    public AdapterFragmentActivityPessoas(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return fragmentCliente;
            case 1:
                return fragmentFornecedor;
            case 2:
                return fragmentVendedor;
        }
        return null;
    }

    @Override
    public int getCount() {
        return titulos.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titulos[position];
    }

}
