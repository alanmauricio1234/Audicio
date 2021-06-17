package mx.edu.uacm.audicio.modelo.implementaciones;


import mx.edu.uacm.audicio.modelo.interfaces.ModoReproduccion;

public class ModoRepetido implements ModoReproduccion {
    private Integer indice;
    private Integer tamanio;

    public ModoRepetido() {
        indice = 0;
    }

    @Override
    public void siguiente() {
        indice++;
        if (indice > tamanio - 1) {
            indice = 0;
        }
    }

    @Override
    public void anterior() {
        indice--;
        if (indice < 0) {
            indice = tamanio - 1;
        }
    }

    @Override
    public void terminoCancion() {
        siguiente();
    }

    @Override
    public void setTamanio(int tamanio) {
        this.tamanio = tamanio;
    }

    @Override
    public int getIndice() {
        return indice;
    }

    @Override
    public void setIndice(int indice) {
        this.indice = indice;
    }
}
