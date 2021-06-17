package mx.edu.uacm.audicio.fragmentos;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import mx.edu.uacm.audicio.R;
import mx.edu.uacm.audicio.adaptador.AdaptadorLista;
import mx.edu.uacm.audicio.modelo.implementaciones.CancionImp;
import mx.edu.uacm.audicio.modelo.implementaciones.ReproductorImp;
import mx.edu.uacm.audicio.modelo.interfaces.Cancion;
import mx.edu.uacm.audicio.modelo.util.FabricaModo;
import mx.edu.uacm.audicio.viewmodel.InfoViewModel;


public class ReproductorFragment extends Fragment {
    private static ReproductorFragment REPRODUCTOR_FRAGMENT;
    private Button btnPlayPause, btnSiguiente, btnAnterior, btnModo, btnLista;
    private TextView lblTitulo, lblArtista, lblTiempo, lblDuracion;
    private SeekBar seekBar;
    private ReproductorImp reproductorImp;
    private Cancion cancionActual;
    private FabricaModo fabricaModo;
    private int indiceModo;
    private Handler handler;
    private InfoViewModel infoViewModel;
    private AdaptadorLista adaptadorLista;



    private ReproductorFragment() {
    }

    public static ReproductorFragment getReproductorFragment() {
        if (REPRODUCTOR_FRAGMENT == null) {
            REPRODUCTOR_FRAGMENT = new ReproductorFragment();
        }

        return REPRODUCTOR_FRAGMENT;
    }

    public SeekBar getSeekBar() {
        return this.seekBar;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_reproductor, container, false);
        handler = new Handler();
        btnAnterior = v.findViewById(R.id.btnAnterior);
        btnLista = v.findViewById(R.id.btnLista);
        btnModo = v.findViewById(R.id.btnModo);
        btnPlayPause = v.findViewById(R.id.btnPlayPause);
        btnSiguiente = v.findViewById(R.id.btnSiguiente);
        lblTitulo = v.findViewById(R.id.lblTitulo);
        lblDuracion = v.findViewById(R.id.lblDuracion);
        lblTiempo = v.findViewById(R.id.lblTiempo);
        lblArtista = v.findViewById(R.id.lblMArtista);
        seekBar = v.findViewById(R.id.seekBar);
        indiceModo = 0;
        fabricaModo = new FabricaModo();
        reproductorImp = ReproductorImp.obtenerReproductorImp();
        reproductorImp.setReproductorFragment(this);
        reproductorImp.setModoReproduccion(fabricaModo.getModo("REPETIDO"));
        infoViewModel = new ViewModelProvider(requireActivity()).get(InfoViewModel.class);
        adaptadorLista = new AdaptadorLista(reproductorImp.getListaCanciones());
        adaptadorLista.setInfoViewModel(infoViewModel);

        //Datos de la primera canción
        lblTitulo.setText(
                reproductorImp.obtenerCancionActual().getTitulo()
        );
        lblArtista.setText(
                reproductorImp.obtenerCancionActual().getArtista()
        );
        lblDuracion.setText(
                CancionImp.formatoReloj( reproductorImp.obtenerCancionActual().getDuracion() )
        );

        if (infoViewModel.getCancion() != null) {
            lblTitulo.setText( infoViewModel.getCancion().getTitulo() );
            lblArtista.setText( infoViewModel.getCancion().getArtista() );
            lblDuracion.setText(
                    CancionImp.formatoReloj( infoViewModel.getCancion().getDuracion() )
            );
        }

        if (infoViewModel.getDuracion() >= 0) {
            seekBar.setMax(infoViewModel.getDuracion());
            lblDuracion.setText( CancionImp.formatoReloj( infoViewModel.getDuracion() ) );
            btnPlayPause.setBackgroundResource(R.drawable.ic_baseline_pause_circle_filled_24);
        }


        btnLista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarModal(v);
            }
        });

        btnPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playPause();
            }
        });

        btnSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                siguiente();
            }
        });

        btnAnterior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                anterior();
            }
        });

        btnModo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seleccionarModo();
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progreso;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progreso = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                reproductorImp.getMediaPlayer().seekTo(progreso);
            }
        });

        return v;
    }

    private void mostrarModal(View v) {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                this.getContext(), R.style.BottomSheetDialogTheme
        );

        View bottomSheetDialogView = LayoutInflater.from(this.getContext())
                .inflate(
                        R.layout.layout_bottom_sheet,
                        (LinearLayout) v.findViewById(R.id.bottomSheetContainer)
                );
        final RecyclerView rcv = bottomSheetDialogView.findViewById(R.id.rcLista);
        rcv.setLayoutManager(new LinearLayoutManager(v.getContext()));
        rcv.setAdapter(adaptadorLista);

        Button btnCerrarModal = bottomSheetDialogView.findViewById(R.id.btnCerrarModal);
        btnCerrarModal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });
        bottomSheetDialog.setContentView(bottomSheetDialogView);
        bottomSheetDialog.show();
    }

    /**
     * Método que lanza el Handle para actualizar el seekBar y demás elementos
     * Es llamado en el servicio MyService
     */
    public void notificar() {
        actualizarSeekBar();
    }

    private Runnable actualizacion = new Runnable() {
        @Override
        public void run() {
            actualizarSeekBar();
            long tiempoTranscurrido = reproductorImp.getMediaPlayer().getCurrentPosition();
            lblTiempo.setText( CancionImp.formatoReloj(tiempoTranscurrido) );
        }
    };


    private void actualizarSeekBar() {
        try {
            if ( reproductorImp.
                    getMediaPlayer().isPlaying() ) {
                seekBar.setProgress(reproductorImp.
                        getMediaPlayer().getCurrentPosition());
                handler.postDelayed(actualizacion, 1000);//Se detiene el hilo 1 segundo
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void seleccionarModo() {
        indiceModo++;
        if ( indiceModo == 3 ) {
            indiceModo = 0;
        }
        if ( indiceModo == 0 ) {
            reproductorImp.
                    setModoReproduccion( fabricaModo.getModo("REPETIDO") );
            btnModo.setBackgroundResource(R.drawable.ic_baseline_repeat_24);
        } else if ( indiceModo == 1 ) {
            reproductorImp.
                    setModoReproduccion( fabricaModo.getModo("REPETIR_UNO") );
            btnModo.setBackgroundResource(R.drawable.ic_baseline_repeat_one_24);
        } else if ( indiceModo == 2 ) {
            reproductorImp.
                    setModoReproduccion( fabricaModo.getModo("ALEATORIO") );
            btnModo.setBackgroundResource(R.drawable.ic_baseline_shuffle_24);
        }
    }

    private void playPause() {
        if (!reproductorImp.getMediaPlayer().
                isPlaying()) {
            reproductorImp.reproducir();
            obtenerDatosCancion();
            btnPlayPause.setBackgroundResource(R.drawable.ic_baseline_pause_circle_filled_24);
        } else {
            reproductorImp.pausar();
            btnPlayPause.setBackgroundResource(R.drawable.ic_baseline_play_circle_filled_24);
            handler.removeCallbacks(actualizacion);
        }
    }

    private void siguiente() {
        reproductorImp.siguiente();
        obtenerDatosCancion();
        btnPlayPause.setBackgroundResource(R.drawable.ic_baseline_pause_circle_filled_24);
    }

    private void anterior() {
        reproductorImp.anterior();
        obtenerDatosCancion();
        btnPlayPause.setBackgroundResource(R.drawable.ic_baseline_pause_circle_filled_24);
    }

    public void obtenerDatosCancion() {
        cancionActual = reproductorImp.obtenerCancionActual();
        infoViewModel.setCancion(cancionActual);
        lblTitulo.setText(cancionActual.getTitulo());
        lblArtista.setText(cancionActual.getArtista());
        lblDuracion.setText(CancionImp.formatoReloj(cancionActual.getDuracion()));
    }




}