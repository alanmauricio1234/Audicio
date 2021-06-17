package mx.edu.uacm.audicio.modelo.interfaces;

public interface Reproductor {
    public void reproducir();
    public void pausar();
    public void detener();
    public void siguiente();
    public void  anterior();
    public void setListaCanciones(ListaCanciones listaCanciones);
    public ListaCanciones getListaCanciones();
    public Cancion obtenerCancionActual();
}
