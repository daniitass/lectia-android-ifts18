package com.example.lectia;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.lectia.database.LectiaDatabase;
import com.example.lectia.database.MensajesClub;
import com.example.lectia.database.Usuario;

import java.util.ArrayList;
import java.util.List;

public class DetallesClubActivity extends AppCompatActivity {

    private List<Comentario> comentariosParaMostrar;
    private ComentarioAdapter adapter;
    private RecyclerView recyclerComentarios;
    private EditText editTextComentario; // Declaramos el EditText a nivel de clase
    private int clubId;
    private Uri imagenComentarioUri;

    private final ActivityResultLauncher<Intent> pickImageLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    imagenComentarioUri = result.getData().getData();
                    Toast.makeText(this, "Imagen seleccionada", Toast.LENGTH_SHORT).show();
                    try {
                        final int takeFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION;
                        getContentResolver().takePersistableUriPermission(imagenComentarioUri, takeFlags);
                    } catch (SecurityException e) {
                        Log.e("DetallesClubActivity", "Error al tomar permiso persistente", e);
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_club);

        // --- Recibir y mostrar datos del club ---
        clubId = getIntent().getIntExtra("clubId", -1);
        String nombre = getIntent().getStringExtra("nombreClub");
        String descripcion = getIntent().getStringExtra("descripcionClub");
        String imagenPath = getIntent().getStringExtra("imagenClubPath");

        ImageView imagenClub = findViewById(R.id.imagenClubDetalle);
        TextView nombreClub = findViewById(R.id.textNombreClubDetalle);
        TextView descripcionClub = findViewById(R.id.textDescripcionClubDetalle);

        nombreClub.setText(nombre);
        descripcionClub.setText(descripcion);

        if (imagenPath != null && !imagenPath.isEmpty()) {
            Glide.with(this).load(imagenPath).into(imagenClub);
        } else {
            imagenClub.setImageResource(R.drawable.club_default);
        }

        // --- Configuración del RecyclerView ---
        recyclerComentarios = findViewById(R.id.recyclerComentarios);
        recyclerComentarios.setLayoutManager(new LinearLayoutManager(this));
        comentariosParaMostrar = new ArrayList<>();
        adapter = new ComentarioAdapter(comentariosParaMostrar, this);
        recyclerComentarios.setAdapter(adapter);

        if (clubId != -1) {
            cargarComentarios();
        }

        // --- Lógica de la barra de comentarios (SIN DIÁLOGO) ---
        ImageButton btnAdjuntarImagen = findViewById(R.id.btn_adjuntar_imagen);
        editTextComentario = findViewById(R.id.edit_text_comentario); // Vinculamos el nuevo EditText
        ImageButton btnEnviarComentario = findViewById(R.id.btn_enviar_comentario);

        btnAdjuntarImagen.setOnClickListener(v -> lanzarSelectorDeImagenes());

        btnEnviarComentario.setOnClickListener(v -> {
            String textoComentario = editTextComentario.getText().toString().trim();
            if (!textoComentario.isEmpty() || imagenComentarioUri != null) {
                guardarNuevoComentario(textoComentario);
            } else {
                Toast.makeText(this, "Escribe un comentario o adjunta una imagen", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void lanzarSelectorDeImagenes() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        pickImageLauncher.launch(intent);
    }

    private void cargarComentarios() {
        new Thread(() -> {
            LectiaDatabase db = LectiaDatabase.getDatabase(getApplicationContext());
            List<MensajesClub> mensajesDb = db.mensajeClubDao().getMensajesPorClub(clubId);
            List<Comentario> comentariosTransformados = new ArrayList<>();

            for (MensajesClub mensajeDb : mensajesDb) {
                Usuario autor = db.usuarioDao().findById(mensajeDb.getUsuarioId());
                String nombreAutor = (autor != null) ? autor.getNombre() : "Anónimo";
                comentariosTransformados.add(new Comentario(nombreAutor, mensajeDb.getContenido(), mensajeDb.getImagenPath()));
            }

            runOnUiThread(() -> {
                comentariosParaMostrar.clear();
                comentariosParaMostrar.addAll(comentariosTransformados);
                adapter.notifyDataSetChanged();
            });
        }).start();
    }

    // El método mostrarDialogoComentario() ha sido eliminado.

    private void guardarNuevoComentario(String texto) {
        SharedPreferences sharedPreferences = getSharedPreferences(MenuActivity.PREFS_NAME, Context.MODE_PRIVATE);
        int idUsuario = sharedPreferences.getInt(MenuActivity.KEY_USER_ID, -1);

        if (idUsuario == -1) { return; }

        long timestamp = System.currentTimeMillis();
        String imagenPath = (imagenComentarioUri != null) ? imagenComentarioUri.toString() : null;

        MensajesClub nuevoMensaje = new MensajesClub(texto, timestamp, clubId, idUsuario, imagenPath);

        new Thread(() -> {
            LectiaDatabase.getDatabase(getApplicationContext()).mensajeClubDao().insert(nuevoMensaje);

            // Volvemos al hilo principal para limpiar los campos y notificar al usuario
            runOnUiThread(() -> {
                editTextComentario.setText(""); // Limpiamos el EditText
                imagenComentarioUri = null; // Limpiamos la imagen seleccionada
                Toast.makeText(DetallesClubActivity.this, "Comentario enviado", Toast.LENGTH_SHORT).show();
            });

            // Recargamos los comentarios para mostrar el nuevo al instante
            cargarComentarios();
        }).start();
    }
}
