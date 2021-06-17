package mx.edu.uacm.audicio.modelo.interfaces;


import mx.edu.uacm.audicio.modelo.interfaces.Cancion;

public interface ListaCanciones extends Iterable<Cancion>{
    public boolean agregar(Cancion cancion);
    public Cancion obtenerCancion(int posicion);
    public void setNombre(String nombre);
    public String getNombre();
    public void limpiar();
    public int tamanio();
    public boolean contiene(Cancion cancion);
}
