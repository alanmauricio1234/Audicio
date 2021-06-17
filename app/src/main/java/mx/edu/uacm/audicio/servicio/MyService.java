package mx.edu.uacm.audicio.servicio;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;

import mx.edu.uacm.audicio.fragmentos.ReproductorFragment;
import mx.edu.uacm.audicio.modelo.implementaciones.ReproductorImp;

public class MyService extends Service implements MediaPlayer.OnPreparedListener {
    private MediaPlayer mediaPlayer;
    private ReproductorFragment rf;

    public MyService() {
        mediaPlayer = ReproductorImp.obtenerReproductorImp().getMediaPlayer();
        mediaPlayer.setOnPreparedListener(this);
        rf = ReproductorFragment.getReproductorFragment();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void preparar(String ruta) {
        try {
            mediaPlayer.reset();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDataSource(ruta);
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int resultado = super.onStartCommand(intent, flags, startId);
        //Se realiza la acción que contiene el intent
        if (intent.getAction().equals("PLAY")) {
            String ruta = intent.getStringExtra("ruta");
            preparar(ruta);

        } else if (intent.getAction().equals("PAUSA")) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
            }
        } else if (intent.getAction().equals("SIGUIENTE")) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            String ruta = intent.getStringExtra("ruta");
            preparar(ruta);
        } else if (intent.getAction().equals("ANTERIOR")) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            String ruta = intent.getStringExtra("ruta");
            preparar(ruta);
        } else if (intent.getAction().equals("RESUME")) {
            mediaPlayer.start();
            rf.notificar();
        }
        return resultado;
    }





    @Override
    public void onPrepared(MediaPlayer mp) {
        mediaPlayer.start();
        rf.getSeekBar().setMax(mediaPlayer.getDuration());
        rf.notificar();
    }
}
