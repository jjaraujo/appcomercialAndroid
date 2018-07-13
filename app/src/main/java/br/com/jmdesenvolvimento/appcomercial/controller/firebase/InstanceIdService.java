package br.com.jmdesenvolvimento.appcomercial.controller.firebase;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import app.jm.funcional.model.Dispositivo;

import br.com.jmdesenvolvimento.appcomercial.model.dao.SQLiteDatabaseDao;

import static android.support.constraint.Constraints.TAG;

public class InstanceIdService extends FirebaseInstanceIdService{

    @Override
    public void onTokenRefresh() {

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        enviaTokenParaServidor(refreshedToken);
    }

    private void enviaTokenParaServidor(String refreshedToken) {



        SQLiteDatabaseDao dao = new SQLiteDatabaseDao(getApplicationContext());
        Dispositivo dispositivo = new Dispositivo();
        dispositivo.setToken(refreshedToken);
        dispositivo.setEmpresaClienteId(0);
        dao.insert(dispositivo);

    }
}
