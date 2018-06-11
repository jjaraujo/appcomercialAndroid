package br.com.jmdesenvolvimento.appcomercial.view.adapters;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import br.com.jmdesenvolvimento.appcomercial.view.fragments.FragmentPessoas;

public class AdapterFragmentActivityPessoas extends FragmentStatePagerAdapter{

    String[] titulos = {"Clientes","Fornecedores","Vendedores"};

    private FragmentPessoas fragmentCliente = new FragmentPessoas(0,"cliente");
    private FragmentPessoas fragmentFornecedor = new FragmentPessoas(1,"fornecedor");
    private FragmentPessoas fragmentVendedor = new FragmentPessoas(2,"pessoa");

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

    public FragmentPessoas getFragmentCliente() {
        return fragmentCliente;
    }

    public FragmentPessoas getFragmentFornecedor() {
        return fragmentFornecedor;
    }

    public FragmentPessoas getFragmentVendedor() {
        return fragmentVendedor;
    }
}
