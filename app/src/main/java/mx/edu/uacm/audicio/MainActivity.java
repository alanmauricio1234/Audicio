package mx.edu.uacm.audicio;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import mx.edu.uacm.audicio.fragmentos.ReproductorFragment;
import mx.edu.uacm.audicio.modelo.implementaciones.ReproductorImp;
import mx.edu.uacm.audicio.modelo.interfaces.Cancion;
import mx.edu.uacm.audicio.modelo.implementaciones.CancionImp;
import mx.edu.uacm.audicio.modelo.interfaces.ListaCanciones;
import mx.edu.uacm.audicio.modelo.implementaciones.ListaCancionesImp;
import mx.edu.uacm.audicio.modelo.interfaces.Reproductor;
import mx.edu.uacm.audicio.servicio.MyService;

public class MainActivity extends AppCompatActivity {
    /**
     * Se crea la lista de permisos que ocupará la aplicación
     */
    private static final String[] PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE ,
            Manifest.permission.INTERNET
    };
    private static final int REQUEST_PERMISSIONS = 12345;
    private ListaCanciones listaCanciones;
    private BottomNavigationView btnNavView;
    private ReproductorFragment reproductorFragment;
    private Reproductor reproductor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listaCanciones = obtenerCanciones(this);
        btnNavView = findViewById(R.id.btnNavigation);
        reproductor = ReproductorImp.obtenerReproductorImp();
        reproductor.setListaCanciones(listaCanciones);
        //Agregar el fragmento al container
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        reproductorFragment = ReproductorFragment.getReproductorFragment();
        ft.replace(R.id.container, reproductorFragment);
        ft.commit();


    }

    private ListaCanciones obtenerCanciones(final Context context) {
        final ListaCanciones canciones = new ListaCancionesImp();
        canciones.setNombre("Todas");
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI; //Obtenemos la URI para el contenido externo del dispositivo
        //Encontrar los datos que requerimos
        String[] projection = {MediaStore.Audio.AudioColumns.DATA, MediaStore.Audio.AudioColumns.TITLE,
                MediaStore.Audio.AudioColumns.ARTIST, MediaStore.Audio.AudioColumns.DURATION};
        //Se realiza la consulta
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext() ) {
                Cancion cancion = new CancionImp();
                String ruta = cursor.getString(0);
                String titulo = cursor.getString(1);
                String artista = cursor.getString(2);
                Long duracion = Long.valueOf(cursor.getLong(3));
                cancion.setArtista(artista);
                cancion.setTitulo(titulo);
                cancion.setRuta(ruta);
                cancion.setDuracion(duracion);
                canciones.agregar(cancion);
            }
        }
        return canciones;
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean permisosDenegados() {
        boolean band = false;
        for (String permiso : PERMISSIONS) {
            if ( checkSelfPermission(permiso) != PackageManager.PERMISSION_GRANTED) {
                band = true;
            }
        }

        return band;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //En caso de que los permisos sean denegados se vuelve a construir la app
        if (permisosDenegados()) {
            ((ActivityManager)(this.getSystemService(ACTIVITY_SERVICE))).clearApplicationUserData();
            recreate();
        } else {
            onResume();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
            && permisosDenegados()) {
            requestPermissions(PERMISSIONS, REQUEST_PERMISSIONS);
            return;
        }
        listaCanciones = obtenerCanciones(this);
        reproductor = ReproductorImp.obtenerReproductorImp();
        reproductor.setListaCanciones(listaCanciones);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Detiene el servicio
        Intent intent = new Intent(getApplicationContext(), MyService.class);
        stopService(intent);
    }
}