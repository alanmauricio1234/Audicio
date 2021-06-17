package mx.edu.uacm.audicio.modelo.interfaces;

public interface Cancion {
    public String getTitulo();
    public void setTitulo(String titulo);
    public String getArtista();
    public void setArtista(String artista);
    public Long getDuracion();
    public void setDuracion(Long duracion);
    public String getRuta();
    public void setRuta(String ruta);

}
