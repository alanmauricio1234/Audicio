package mx.edu.uacm.audicio.modelo.util;

import mx.edu.uacm.audicio.modelo.implementaciones.ModoAleatorio;
import mx.edu.uacm.audicio.modelo.implementaciones.ModoRepetido;
import mx.edu.uacm.audicio.modelo.implementaciones.ModoRepetirUno;
import mx.edu.uacm.audicio.modelo.interfaces.ModoReproduccion;

public class FabricaModo {

    public ModoReproduccion getModo(String id) {
        ModoReproduccion modoReproduccion = null;
        if (id == null) {
            modoReproduccion = new ModoRepetido();
        }
        if (id.equalsIgnoreCase("REPETIDO")) {
            modoReproduccion = new ModoRepetido();
        } else if (id.equalsIgnoreCase("ALEATORIO")) {
            modoReproduccion = new ModoAleatorio();
        } else if (id.equalsIgnoreCase("REPETIR_UNO")) {
            modoReproduccion = new ModoRepetirUno();
        }
        return modoReproduccion;
    }
}
