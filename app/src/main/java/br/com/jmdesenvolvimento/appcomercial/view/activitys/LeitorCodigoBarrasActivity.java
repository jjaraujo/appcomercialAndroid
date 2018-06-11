package br.com.jmdesenvolvimento.appcomercial.view.activitys;

import android.Manifest;
import android.graphics.Color;
import android.hardware.Camera;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.zxing.Result;

import br.com.jmdesenvolvimento.appcomercial.R;
import br.com.jmdesenvolvimento.appcomercial.controller.funcionais.VariaveisControle;
import me.dm7.barcodescanner.core.CameraUtils;
import me.dm7.barcodescanner.zxing.ZXingScannerView;
import pub.devrel.easypermissions.EasyPermissions;
import pub.devrel.easypermissions.PermissionRequest;

public class LeitorCodigoBarrasActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private ZXingScannerView zXingScannerView;
    private int REQUEST_CODE_CAMERA = 182;
    private String codigoLido;
    private boolean somTocado = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leitor_codigo_barras);
        zXingScannerView = findViewById(R.id.z_xing_scanner);
        askCameraPermission();
        Log.i("onCreate", "LeitorCodigoBarrasActivity");
    }

    @Override
    protected void onResume() {
        super.onResume();
        zXingScannerView.setResultHandler(this);
        zXingScannerView.setAutoFocus(true);
        zXingScannerView.setLaserColor(Color.RED);

        startCamera();

    }

    @Override
    protected void onPause() {
        super.onPause();
        zXingScannerView.stopCamera();

        Camera camera = CameraUtils.getCameraInstance();
        if (camera != null) {
            camera.release();
        }
    }

    @Override
    public void handleResult(Result result) {

        codigoLido = result.getText();
        if (VariaveisControle.editTextCodigoBarrasCadastroProduto != null) {
            VariaveisControle.editTextCodigoBarrasCadastroProduto.setText(codigoLido);
        }
        VariaveisControle.codigoDeBarrasLido = codigoLido;
        zXingScannerView.resumeCameraPreview(this);

        MediaPlayer mp = MediaPlayer.create(LeitorCodigoBarrasActivity.this, R.raw.bip);
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }

        });
        if (!somTocado) {
            somTocado = true;
            mp.start();
        }

        finish();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    private void askCameraPermission() {
        EasyPermissions.requestPermissions(
                new PermissionRequest.Builder(this, REQUEST_CODE_CAMERA, Manifest.permission.CAMERA)
                        .setRationale("A permissão de uso de câmera é necessária para que o aplicativo funcione.")
                        .setPositiveButtonText("Ok")
                        .setNegativeButtonText("Cancelar")
                        .build());
    }

    private void startCamera() {
        if (EasyPermissions.hasPermissions(this, Manifest.permission.CAMERA)) {
            zXingScannerView.startCamera();
        }
    }
}


