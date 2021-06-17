package mx.edu.uacm.audicio.modelo.implementaciones;

import android.content.Intent;
import android.media.MediaPlayer;

import mx.edu.uacm.audicio.fragmentos.ReproductorFragment;
import mx.edu.uacm.audicio.modelo.interfaces.Cancion;
import mx.edu.uacm.audicio.modelo.interfaces.ListaCanciones;
import mx.edu.uacm.audicio.modelo.interfaces.ModoReproduccion;
import mx.edu.uacm.audicio.modelo.interfaces.Reproductor;
import mx.edu.uacm.audicio.servicio.MyService;

public class ReproductorImp implements Reproductor, MediaPlayer.OnCompletionListener {
    private static ReproductorImp REPRODUCTOR;
    private MediaPlayer mediaPlayer;
    private ReproductorFragment reproductorFragment;
    private ModoReproduccion modoReproduccion;
    private boolean pausado = false;
    private ListaCanciones listaCanciones;

    private ReproductorImp() {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(this);
    }

    public static ReproductorImp obtenerReproductorImp() {
        if (REPRODUCTOR == null) {
            REPRODUCTOR = new ReproductorImp();
        }

        return REPRODUCTOR;
    }



    public MediaPlayer getMediaPlayer() {
        return this.mediaPlayer;
    }

    public void setReproductorFragment(ReproductorFragment reproductorFragment) {
        this.reproductorFragment = reproductorFragment;
    }

    public void setModoReproduccion(ModoReproduccion modoReproduccion) {
        modoReproduccion.setTamanio(listaCanciones.tamanio());
        this.modoReproduccion = modoReproduccion;
    }

    private void resume() {
        Intent intent = new Intent(reproductorFragment.getContext(), MyService.class);
        intent.setAction("RESUME");
        reproductorFragment.getActivity().startService(intent);
    }

    private void enviarIntent(String action) {
        Intent intent = new Intent(reproductorFragment.getContext(), MyService.class);
        intent.putExtra("ruta", listaCanciones.obtenerCancion(
                modoReproduccion.getIndice()).getRuta());
        intent.setAction(action);
        reproductorFragment.getActivity().startService(intent);
    }

    @Override
    public void reproducir() {
        if ( pausado ) {
            resume();
            return;
        }
        enviarIntent("PLAY");
    }

    @Override
    public void pausar() {
        Intent intent = new Intent(reproductorFragment.getContext(), MyService.class);
        intent.setAction("PAUSA");
        reproductorFragment.getActivity().startService(intent);
        pausado = true;
    }

    @Override
    public void detener() {
    }

    @Override
    public void siguiente() {
        modoReproduccion.siguiente();
        enviarIntent("SIGUIENTE");
    }

    @Override
    public void anterior() {
        modoReproduccion.siguiente();
        enviarIntent("ANTERIOR");
    }

    @Override
    public void setListaCanciones(ListaCanciones listaCanciones) {
        this.listaCanciones = listaCanciones;
    }

    @Override
    public ListaCanciones getListaCanciones() {
        return this.listaCanciones;
    }

    @Override
    public Cancion obtenerCancionActual() {
        return listaCanciones.obtenerCancion( modoReproduccion.getIndice() );
    }


    @Override
    public void onCompletion(MediaPlayer mp) {
        modoReproduccion.terminoCancion();
        siguiente();
        //Actualizamos los datos de la canción
        reproductorFragment.obtenerDatosCancion();
    }
}
