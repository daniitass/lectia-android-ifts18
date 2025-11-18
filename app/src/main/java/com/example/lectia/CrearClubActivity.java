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
    private Uri imagenUri;

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    lanzarSelectorDeImagenes();
                } else {
                    Toast.makeText(this, "Permiso necesario para seleccionar una imagen", Toast.LENGTH_LONG).show();
                }
            });

    private final ActivityResultLauncher<Intent> pickImageLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    imagenUri = result.getData().getData();
                    
                    // --- SOLUCIÓN: Tomar permiso persistente para la URI ---
                    try {
                        final int takeFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION;
                        getContentResolver().takePersistableUriPermission(imagenUri, takeFlags);
                    } catch (SecurityException e) {
                        Log.e("CrearClubActivity", "Error al tomar permiso persistente", e);
                    }
                    // --- FIN DE LA SOLUCIÓN ---

                    binding.imgSeleccion.setImageURI(imagenUri);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCrearClubBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.imgSeleccion.setOnClickListener(v -> verificarPermisoYSeleccionarImagen());
        binding.btnCrearClub.setOnClickListener(v -> crearClub());
    }

    private void verificarPermisoYSeleccionarImagen() {
        String permisoRequerido;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permisoRequerido = Manifest.permission.READ_MEDIA_IMAGES;
        } else {
            permisoRequerido = Manifest.permission.READ_EXTERNAL_STORAGE;
        }

        if (ContextCompat.checkSelfPermission(this, permisoRequerido) == PackageManager.PERMISSION_GRANTED) {
            lanzarSelectorDeImagenes();
        } else {
            requestPermissionLauncher.launch(permisoRequerido);
        }
    }

    private void lanzarSelectorDeImagenes() {
        // Corregido: Usamos ACTION_OPEN_DOCUMENT para obtener URIs persistentes
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        pickImageLauncher.launch(intent);
    }

    private void crearClub() {
        String nombre = binding.inputNombreClub.getText().toString().trim();
        String descripcion = binding.inputDescripcion.getText().toString().trim();

        if (nombre.isEmpty()) {
            binding.textInputNombreClub.setError("El nombre es obligatorio");
            return;
        } else {
            binding.textInputNombreClub.setError(null);
        }

        if (descripcion.isEmpty()) {
            binding.textInputDescripcion.setError("La descripción es obligatoria");
            return;
        } else {
            binding.textInputDescripcion.setError(null);
        }

        if (imagenUri == null) {
            Toast.makeText(this, "Por favor, selecciona una imagen para el club", Toast.LENGTH_SHORT).show();
            return;
        }

        String imagenPath = imagenUri.toString();

        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                LectiaDatabase db = LectiaDatabase.getDatabase(getApplicationContext());
                Club nuevoClub = new Club();
                nuevoClub.setNombre(nombre);
                nuevoClub.setDescripcion(descripcion);
                nuevoClub.setImagenPath(imagenPath);
                db.clubDao().insert(nuevoClub);

                runOnUiThread(() -> {
                    Toast.makeText(CrearClubActivity.this, "Club '" + nombre + "' creado con éxito", Toast.LENGTH_LONG).show();
                    finish();
                });

            } catch (Exception e) {
                Log.e("CrearClubActivity", "Error al insertar el club en la base de datos", e);
                runOnUiThread(() -> {
                    Toast.makeText(CrearClubActivity.this, "Error al crear el club", Toast.LENGTH_LONG).show();
                });
            }
        });
    }
}
