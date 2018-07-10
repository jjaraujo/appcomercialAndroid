package br.com.jmdesenvolvimento.appcomercial.model.dao;

import android.database.Cursor;

import com.jmdesenvolvimento.appcomercial.controller.FuncoesSql;
import com.jmdesenvolvimento.appcomercial.model.dao.IRegistros;

public class CursorRegistros implements IRegistros{

    private Cursor cursor;

    public CursorRegistros(Cursor cursor){
        this.cursor = cursor;
    }

    @Override
    public String getString(String s) {
        int index = cursor.getColumnIndex(s);
        return cursor.getString(index);
    }

    @Override
    public int getInt(String s) {
        int index = cursor.getColumnIndex(s);
        return cursor.getInt(index);
    }

    @Override
    public double getDouble(String s) {
        int index = cursor.getColumnIndex(s);
        return cursor.getDouble(index);
    }

    @Override
    public long getLong(String s) {
        int index = cursor.getColumnIndex(s);
        return cursor.getLong(index);
    }

    @Override
    public String getNomeColuna(int i) {
        return cursor.getColumnName(i);
    }

    @Override
    public int getColumnCount() {
        return cursor.getColumnCount();
    }

    @Override
    public int getCount() {
        return cursor.getCount();
    }

    @Override
    public void close() {
        cursor.close();
    }

    @Override
    public int getTipoSql() {
        return FuncoesSql.SQLITE;
    }

    public boolean moveToNext(){
        return cursor.moveToNext();
    }
}
