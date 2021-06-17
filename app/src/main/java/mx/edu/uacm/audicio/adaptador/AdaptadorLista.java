package mx.edu.uacm.audicio.adaptador;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import mx.edu.uacm.audicio.R;
import mx.edu.uacm.audicio.modelo.interfaces.Cancion;
import mx.edu.uacm.audicio.modelo.interfaces.ListaCanciones;
import mx.edu.uacm.audicio.servicio.MyService;
import mx.edu.uacm.audicio.viewmodel.InfoViewModel;

public class AdaptadorLista extends RecyclerView.Adapter<AdaptadorLista.MyViewHolder> {
    private ListaCanciones listaCanciones;
    private InfoViewModel infoViewModel;

    public AdaptadorLista(ListaCanciones listaCanciones) {
        this.listaCanciones = listaCanciones;
    }

    public ListaCanciones getListaCanciones() {
        return listaCanciones;
    }

    public void setListaCanciones(ListaCanciones listaCanciones) {
        this.listaCanciones = listaCanciones;
    }

    public InfoViewModel getInfoViewModel() {
        return infoViewModel;
    }

    public void setInfoViewModel(InfoViewModel infoViewModel) {
        this.infoViewModel = infoViewModel;
    }

    @NonNull
    @Override
    public AdaptadorLista.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from( parent.getContext() )
                .inflate(R.layout.mi_item, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorLista.MyViewHolder holder, int position) {
        holder.lblTitulo.setText( listaCanciones.obtenerCancion(position).getTitulo() );
        holder.lblArtista.setText( listaCanciones.obtenerCancion(position).getArtista() );
    }

    @Override
    public int getItemCount() {
        return listaCanciones.tamanio();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView lblTitulo, lblArtista;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            lblTitulo = itemView.findViewById(R.id.lblTituloCancion);
            lblArtista = itemView.findViewById(R.id.lblArtistaCancion);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int posicion = getAdapterPosition();
                    Cancion cancion = listaCanciones.obtenerCancion( posicion );
                    Intent intent = new Intent(v.getContext(), MyService.class);
                    intent.putExtra("ruta", cancion.getRuta());
                    intent.setAction("PLAY");
                    infoViewModel.setCancion(cancion);
                    infoViewModel.setDuracion( cancion.getDuracion().intValue() );

                }
            });
        }
    }
}
