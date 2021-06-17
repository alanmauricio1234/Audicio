package mx.edu.uacm.audicio.viewmodel;

import androidx.lifecycle.ViewModel;

import mx.edu.uacm.audicio.modelo.interfaces.Cancion;
import mx.edu.uacm.audicio.modelo.interfaces.ModoReproduccion;

public class InfoViewModel extends ViewModel {
    private Cancion cancion;
    private int indice;
    private int duracion;

    public InfoViewModel() {
        duracion = -1;
    }

    public Cancion getCancion() {
        return cancion;
    }

    public void setCancion(Cancion cancion) {
        this.cancion = cancion;
    }

    public int getIndice() {
        return indice;
    }

    public void setIndice(int indice) {
        this.indice = indice;
    }

    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }


}
