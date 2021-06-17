package mx.edu.uacm.audicio.modelo.implementaciones;

import android.os.Parcel;
import android.os.Parcelable;

import mx.edu.uacm.audicio.modelo.interfaces.Cancion;

public class CancionImp implements Cancion, Parcelable {
    private String titulo;
    private String artista;
    private Long duracion;
    private String ruta;

    public CancionImp() {
        duracion = Long.valueOf("1");
    }

    protected CancionImp(Parcel in) {
        titulo = in.readString();
        artista = in.readString();
        if (in.readByte() == 0) {
            duracion = null;
        } else {
            duracion = in.readLong();
        }
        ruta = in.readString();
    }

    public static final Creator<CancionImp> CREATOR = new Creator<CancionImp>() {
        @Override
        public CancionImp createFromParcel(Parcel in) {
            return new CancionImp(in);
        }

        @Override
        public CancionImp[] newArray(int size) {
            return new CancionImp[size];
        }
    };

    @Override
    public String getTitulo() {
        return this.titulo;
    }

    @Override
    public void setTitulo(String titulo) {
        if (!titulo.isEmpty() && !(titulo == null)) {
            this.titulo = titulo;
        } else {
            this.titulo = "Desconocido";
        }
    }

    @Override
    public String getArtista() {
        return this.artista;
    }

    @Override
    public void setArtista(String artista) {
        if (!artista.isEmpty() && !(artista == null)) {
            this.artista = artista;
        } else {
            this.artista = "Desconocido";
        }
    }

    @Override
    public Long getDuracion() {
        return this.duracion;
    }

    @Override
    public void setDuracion(Long duracion) {
        this.duracion = duracion;
    }

    @Override
    public String getRuta() {
        return this.ruta;
    }

    @Override
    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public static String formatoReloj(long tiempo) {
        String formato = "";
        tiempo = tiempo / 1000;
        long segundo = tiempo % 60;
        long minuto = tiempo / 60;
        String fSegundo = "" + segundo;
        String fMinuto = "" + minuto;
        if (segundo < 10) {
            fSegundo = "0" + segundo;
        }
        formato += fMinuto + ":" + fSegundo;
        return formato;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(titulo);
        dest.writeString(artista);
        if (duracion == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(duracion);
        }
        dest.writeString(ruta);
    }
}
