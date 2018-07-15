package br.com.jmdesenvolvimento.appcomercial.view.adapters.arraysAdapter.tabelas;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import app.jm.funcional.controller.funcoesGerais.FuncoesGerais;
import app.jm.funcional.model.entidades.vendas.HorariosCaixa;
import br.com.jmdesenvolvimento.appcomercial.R;
import br.com.jmdesenvolvimento.appcomercial.model.dao.SQLiteDatabaseDao;

public class ArrayAdapterHorariosCaixa extends BaseAdapter {

    private List<HorariosCaixa> listHorarios;
    private AppCompatActivity context;


    public ArrayAdapterHorariosCaixa(AppCompatActivity context, List listHorarios){
        this.context = context;
        this.listHorarios = listHorarios;
    }

    @Override
    public int getCount() {
        return listHorarios.size();
    }

    @Override
    public Object getItem(int i) {
        return listHorarios.get(i);
    }

    @Override
    public long getItemId(int i) {
        return listHorarios.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        HorariosCaixa horario = listHorarios.get(i);
        View model = View.inflate(context, R.layout.list_model_horarios_caixa,null);
        TextView diaDaSemana = model.findViewById(R.id.textViewHrCaixaDiaSemana);
        TextView abertura = model.findViewById(R.id.textViewHrCaixaAbetura);
        TextView encerramento = model.findViewById(R.id.textViewHrCaixaEncerramento);

        diaDaSemana.setText(horario.getDiaSemana());
        abertura.setText(FuncoesGerais.timeToString(horario.getAbertura(), FuncoesGerais.hhmm));
        encerramento.setText(FuncoesGerais.timeToString(horario.getEncerramento(), FuncoesGerais.hhmm));

        return model;
    }

}
