package com.example.lectia;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.lectia.database.Club;
import com.example.lectia.database.LectiaDatabase;
import com.example.lectia.databinding.ActivityCrearClubBinding;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CrearClubActivity extends AppCompatActivity {

    private ActivityCrearClubBinding binding;
    private Uri imagenUri; // Variable para guardar la URI de la imagen seleccionada

    // Lanzador para solicitar el permiso de lectura de medios.
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // Permiso concedido, ahora podemos abrir la galería.
                    lanzarSelectorDeImagenes();
                } else {
                    // Permiso denegado. Informamos al usuario.
                    Toast.makeText(this, "Permiso necesario para seleccionar una imagen", Toast.LENGTH_LONG).show();
                }
            });

    // Lanzador para obtener la imagen desde la galería.
    private final ActivityResultLauncher<Intent> pickImageLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    // Imagen seleccionada con éxito.
                    imagenUri = result.getData().getData();
                    // Mostramos la imagen seleccionada en el ImageView.
                    binding.imgSeleccion.setImageURI(imagenUri);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inflamos el layout usando ViewBinding.
        binding = ActivityCrearClubBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Configuramos los listeners para los botones.
        binding.imgSeleccion.setOnClickListener(v -> verificarPermisoYSeleccionarImagen());
        binding.btnCrearClub.setOnClickListener(v -> crearClub());
    }

    /**
     * Comprueba si la app tiene permiso para acceder a la galería.
     * Si lo tiene, la abre. Si no, lo solicita.
     */
    private void verificarPermisoYSeleccionarImagen() {
        String permisoRequerido;
        // El permiso a solicitar depende de la versión de Android.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permisoRequerido = Manifest.permission.READ_MEDIA_IMAGES;
        } else {
            permisoRequerido = Manifest.permission.READ_EXTERNAL_STORAGE;
        }

        if (ContextCompat.checkSelfPermission(this, permisoRequerido) == PackageManager.PERMISSION_GRANTED) {
            // Ya tenemos permiso, abrimos la galería directamente.
            lanzarSelectorDeImagenes();
        } else {
            // No tenemos permiso, así que lo solicitamos.
            requestPermissionLauncher.launch(permisoRequerido);
        }
    }

    /**
     * Crea y lanza el Intent para que el usuario elija una imagen.
     */
    private void lanzarSelectorDeImagenes() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickImageLauncher.launch(intent);
    }

    /**
     * Valida los datos del formulario y los inserta en la base de datos.
     */
    private void crearClub() {
        //Recolectamos los datos del formulario.
        String nombre = binding.inputNombreClub.getText().toString().trim();
        String descripcion = binding.inputDescripcion.getText().toString().trim();

        //Validamos que los campos no estén vacíos.
        if (nombre.isEmpty()) {
            binding.textInputNombreClub.setError("El nombre es obligatorio");
            return; // Detenemos la ejecución si falta el nombre.
        } else {
            binding.textInputNombreClub.setError(null); // Limpiamos el error si estaba presente.
        }

        if (descripcion.isEmpty()) {
            binding.textInputDescripcion.setError("La descripción es obligatoria");
            return; // Detenemos la ejecución si falta la descripción.
        } else {
            binding.textInputDescripcion.setError(null);
        }

        if (imagenUri == null) {
            Toast.makeText(this, "Por favor, selecciona una imagen para el club", Toast.LENGTH_SHORT).show();
            return; // Detenemos la ejecución si no hay imagen.
        }

        //Preparamos los datos para la base de datos.
        String imagenPath = imagenUri.toString();

        //Ejecutamos la inserción en un hilo secundario para no bloquear la UI.
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                // Obtenemos la instancia de la base de datos.
                LectiaDatabase db = LectiaDatabase.getDatabase(getApplicationContext());

                Club nuevoClub = new Club();

                nuevoClub.setNombre(nombre);
                nuevoClub.setDescripcion(descripcion);
                nuevoClub.setImagenPath(imagenPath);

                // Insertamos el objeto en la base de datos.
                db.clubDao().insert(nuevoClub);

                // 5. Volvemos al hilo principal para notificar al usuario y cerrar la actividad.
                runOnUiThread(() -> {
                    Toast.makeText(CrearClubActivity.this, "Club '" + nombre + "' creado con éxito", Toast.LENGTH_LONG).show();
                    finish(); // Cierra esta actividad y regresa a la anterior.
                });

            } catch (Exception e) {
                // Si ocurre un error, lo registramos y notificamos al usuario.
                Log.e("CrearClubActivity", "Error al insertar el club en la base de datos", e);
                runOnUiThread(() -> {
                    Toast.makeText(CrearClubActivity.this, "Error al crear el club", Toast.LENGTH_LONG).show();
                });
            }

        });
    }
}