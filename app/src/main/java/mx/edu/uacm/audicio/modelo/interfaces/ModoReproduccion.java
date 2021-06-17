package mx.edu.uacm.audicio.modelo.interfaces;

public interface ModoReproduccion {
    public void siguiente();
    public void anterior();
    public void terminoCancion();
    public void setTamanio(int tamanio);
    public int getIndice();
    public void setIndice(int indice);
}
