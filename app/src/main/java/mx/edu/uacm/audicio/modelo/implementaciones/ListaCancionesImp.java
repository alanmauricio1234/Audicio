package mx.edu.uacm.audicio.modelo.implementaciones;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import mx.edu.uacm.audicio.modelo.interfaces.Cancion;
import mx.edu.uacm.audicio.modelo.interfaces.ListaCanciones;


public class ListaCancionesImp implements ListaCanciones {
    private List<Cancion> canciones;
    private String nombre;

    public ListaCancionesImp() {
        canciones = new ArrayList<>();
        nombre = "";
    }

    @Override
    public boolean agregar(Cancion cancion) {
        boolean verificar = false;
        if (cancion != null) {
            verificar = canciones.add(cancion);
        }
        return verificar;
    }



    @Override
    public Cancion obtenerCancion(int posicion) {
        Cancion resultado = null;
        if (posicion >= 0 && posicion < canciones.size()) {
            resultado = canciones.get(posicion);
        }
        return resultado;
    }



    @Override
    public String getNombre() {
        return nombre;
    }

    @Override
    public void limpiar() {
        canciones.clear();
    }

    @Override
    public int tamanio() {
        return canciones.size();
    }

    @Override
    public boolean contiene(Cancion cancion) {
        return canciones.contains(cancion);
    }

    @NonNull
    @Override
    public Iterator<Cancion> iterator() {
        return canciones.iterator();
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
