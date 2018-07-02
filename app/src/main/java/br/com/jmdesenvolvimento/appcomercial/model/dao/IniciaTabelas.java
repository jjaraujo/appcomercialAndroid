package br.com.jmdesenvolvimento.appcomercial.model.dao;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public class IniciaTabelas extends AsyncTask<Void, Void, Void>{
    private Context context;
    private ProgressDialog dialog;
    private SQLiteDatabaseDao db;


    public IniciaTabelas(Context context, SQLiteDatabaseDao db){
        this.context = context;
        this.db = db;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog = new ProgressDialog(context);
        dialog.setMessage("Aguarde, iniciando tabelas!");
        dialog.show();
    }

    @Override
    protected Void doInBackground(Void... voids) {
//        Tabela[] tabelas = db.tabelas;
//
//        Calendar calendar = new GregorianCalendar();
//        calendar.setTimeInMillis(0);
//        Log.i("longs",calendar.getTimeInMillis()+"");
//        for (int i = 0; i < tabelas.length; i++) {
//            String sql = "CREATE TABLE IF NOT EXISTS " + tabelas[i].getNomeTabela(false) + "(";
//
//            String[] nomeAtributos = tabelas[i].getNomesAtributos();
//            // verificar a possibilidade de pegar esses nomes direto do map
//            for (int j = 0; j < nomeAtributos.length; j++) {
//                String nome = nomeAtributos[j];
//                Object atributo = FuncoesGerais.getFieldDeTabela(nome,tabelas[i]);
//                if (nome == null) {
//                    continue;
//                }
//                sql += "," + db.substituiTiposVariaveis(nomeAtributos[j], tabelas[i],atributo);
//            }
//            sql = sql.replace("(,", "(");
//            sql = sql + ");";
//
//            Log.i("Criando tabela", sql);
//            db.execSQL(sql);
//            db.insereRegistrosIniciais( tabelas[i]);
//        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        dialog.dismiss();

    }
}
