package br.com.jmdesenvolvimento.appcomercial.view.activitys.iniciais;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.jmdesenvolvimento.appcomercial.controller.VariaveisControleG;
import com.jmdesenvolvimento.appcomercial.model.entidades.cadastral.pessoas.EmpresaCliente;
import com.jmdesenvolvimento.appcomercial.model.entidades.cadastral.pessoas.Funcionario;

import br.com.jmdesenvolvimento.appcomercial.R;
import br.com.jmdesenvolvimento.appcomercial.controller.Logout;
import br.com.jmdesenvolvimento.appcomercial.view.activitys.configuracoes.ConfiguracoesActivity;
import br.com.jmdesenvolvimento.appcomercial.view.activitys.entidades.contas.ContaReceberActivity;
import br.com.jmdesenvolvimento.appcomercial.view.activitys.entidades.estoque.EstoqueActivity;
import br.com.jmdesenvolvimento.appcomercial.view.activitys.entidades.pessoas.PessoasActivity;

public class MenuLateral {

    public static void iniciaComponentesMenuLateral(Toolbar toolbar, AppCompatActivity appCompatActivity) {

        DrawerLayout drawer = appCompatActivity.findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                appCompatActivity, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = appCompatActivity.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) appCompatActivity);
        Menu menu = navigationView.getMenu();
        MenuItem item = menu.findItem(R.id.menuContasReceber);
        View cabecalho = navigationView.getHeaderView(0);
        TextView textViewNomeEmpresa = cabecalho.findViewById(R.id.textViewNomeEmpresa);
        TextView textViewNomePessoaLogada = cabecalho.findViewById(R.id.textViewNomePessoaLogada);

        EmpresaCliente empCli = VariaveisControleG.empresaCliente;
        Funcionario vendedor = VariaveisControleG.funcionarioLogado;

        textViewNomeEmpresa.setText(vendedor == null ? "" : empCli.getPessoa().getNomeFantasia());
        textViewNomePessoaLogada.setText(vendedor == null ? empCli.getPessoa().getNomeFantasia() : vendedor.getPessoa().getNome());
        textViewNomePessoaLogada.setTextSize(TypedValue.COMPLEX_UNIT_DIP,25);
    }

    public static void onNavigationItemSelected(MenuItem item, AppCompatActivity appCompatActivity){

        Intent intent;
        switch (item.getItemId()) {
            case R.id.menuContasAbertas:
                intent = new Intent(appCompatActivity, VendasAbertasActivity.class);
                appCompatActivity.startActivity(intent);
                break;
            case R.id.menuPessoas:
                intent = new Intent(appCompatActivity, PessoasActivity.class);
                appCompatActivity.startActivity(intent);
                break;
            case R.id.menuEstoque:
                intent = new Intent(appCompatActivity, EstoqueActivity.class);
                appCompatActivity.startActivity(intent);
                break;
            case R.id.menuContasReceber:
                intent = new Intent(appCompatActivity, ContaReceberActivity.class);
                appCompatActivity.startActivity(intent);
                break;
            case R.id.menuCaixa:
                if(!appCompatActivity.getClass().getName().equals(CaixaActivity.class.getName())) {
                    intent = new Intent(appCompatActivity, CaixaActivity.class);
                    appCompatActivity.startActivity(intent);
                }
                break;
            case R.id.menuConfiguracoes:
                intent = new Intent(appCompatActivity, ConfiguracoesActivity.class);
                appCompatActivity.startActivity(intent);
                break;
            case R.id.sair:
                Logout logout = new Logout(appCompatActivity);
                logout.execute();
        }

        DrawerLayout drawer = appCompatActivity.findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }
}
