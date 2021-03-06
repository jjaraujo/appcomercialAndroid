package br.com.jmdesenvolvimento.appcomercial.view.adapters.adaptersFragments;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import br.com.jmdesenvolvimento.appcomercial.view.fragments.FragmentProdutosVendas;
import br.com.jmdesenvolvimento.appcomercial.view.fragments.FragmentVendasAbertas;

public class AdapterFragmentActivityPrincipal extends FragmentStatePagerAdapter {

    String[] titulosTelas = {"Vendas Abertas","Produtos"};
    private FragmentVendasAbertas fragmentVendasAbertas = new FragmentVendasAbertas();
    private FragmentProdutosVendas fragmentProdutos = new FragmentProdutosVendas();


    public AdapterFragmentActivityPrincipal(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return fragmentVendasAbertas;
            case 1:
                return fragmentProdutos;
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return titulosTelas.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titulosTelas[position];
    }

    public FragmentVendasAbertas getFragmentVendasAbertas() {
         return fragmentVendasAbertas;
    }

}
