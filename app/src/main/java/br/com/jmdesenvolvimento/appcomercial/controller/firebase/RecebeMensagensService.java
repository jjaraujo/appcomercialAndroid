package br.com.jmdesenvolvimento.appcomercial.controller.firebase;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class RecebeMensagensService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Map<String, String> map = remoteMessage.getData();
       // RemoteMessage.Notification map = remoteMessage.getNotification();
        Log.i("Mensagem Recebida",String.valueOf(map));
    }
}
