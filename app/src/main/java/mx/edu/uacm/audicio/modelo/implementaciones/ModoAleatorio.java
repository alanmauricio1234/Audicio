package mx.edu.uacm.audicio.modelo.implementaciones;

import java.util.Random;

import mx.edu.uacm.audicio.modelo.interfaces.ModoReproduccion;

public class ModoAleatorio implements ModoReproduccion {
    private Integer indice;
    private Integer tamanio;
    private Integer anterior;
    private Random random;

    public ModoAleatorio() {
        indice = 0;
        random = new Random();
    }

    @Override
    public void siguiente() {
        anterior = indice.intValue();
        indice = random.nextInt(tamanio);
    }

    @Override
    public void anterior() {
        indice = anterior;
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
