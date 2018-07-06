package br.com.jmdesenvolvimento.appcomercial.controller.funcionaisAndroid;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.jmdesenvolvimento.appcomercial.controller.funcoesGerais.FuncoesVendasG;
import com.jmdesenvolvimento.appcomercial.controller.VariaveisControleG;
import com.jmdesenvolvimento.appcomercial.model.dao.IConnection;
import br.com.jmdesenvolvimento.appcomercial.model.dao.SQLiteDatabaseDao;
import com.jmdesenvolvimento.appcomercial.model.entidades.vendas.Venda;
public final class FuncoesVendas {

    public static void finalizaVenda(AppCompatActivity context){
        if (VariaveisControleG.valorRestante > 0) {
            FuncoesViewAndroid.addAlertDialogAlerta(context, "Ainda há valores a pagar!");
        } else {
            IConnection dao = new SQLiteDatabaseDao(context);
            FuncoesVendasG.finalizaVenda(dao);
            dao.close();
            limpaCamposVenda();
            context.finish();
            Toast.makeText(context, "Venda finalizada!", Toast.LENGTH_SHORT).show();
        }
    }

    /**Método para criar vendas por cliente ou comanda/mesa*/
    public static void criaVenda(Venda venda, Context context){
        SQLiteDatabaseDao dao = new SQLiteDatabaseDao(context);
        FuncoesVendasG.criaVenda(dao, venda);
        FuncoesViewAndroid.alteraViewVendaSelecionada();
    }

    public static void limpaCamposVenda(){
        VariaveisControleG.vendaSelecionada = null;
     //   VariaveisControleAndroid.buttonValorTotal.setText("FINALIZAR \n\nR$0,00");
     //   VariaveisControleAndroid.textViewVendaSelectionada.setText("Nenhuma venda selecionada");
      //  VariaveisControleAndroid.fragmentProdutos.carregaLista();
     //   VariaveisControleAndroid.fragmentVendasAbertas.carregaLista();
    }

    public static boolean isUsandoVenda(Venda v){

        return v.getTabelaProdutosVenda() != null && !v.getTabelaProdutosVenda().isEmpty();
    }
}
