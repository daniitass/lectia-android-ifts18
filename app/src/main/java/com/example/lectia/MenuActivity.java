package com.example.lectia; // Asegúrate de que tu package sea el correcto

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lectia.database.LectiaDatabase;
import com.example.lectia.database.Usuario;
import com.example.lectia.database.UsuarioDao;

public class MenuActivity extends AppCompatActivity {

    // 1. Declarar las vistas que vamos a usar del layout
    private TextView tvSaludo;
    private ImageButton btnEditarFoto;
    private Button btnLibros;
    private Button btnClubes;

    // Añade el botón de clubes si lo necesitas
    // private Button btnClubes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Usamos setContentView para conectar el layout
        setContentView(R.layout.activity_menu);

        // 2. Vincular las vistas con sus IDs del archivo XML
        tvSaludo = findViewById(R.id.tvSaludo);
        btnEditarFoto = findViewById(R.id.btnEditarFoto);
        btnLibros = findViewById(R.id.btnLibros);
        btnClubes = findViewById(R.id.btnClubes);

        // 3. Obtener el email del usuario que pasamos desde LogActivity
        String userEmail = getIntent().getStringExtra("USER_EMAIL");

        // 4. Cargar y mostrar el nombre del usuario
        cargarDatosUsuario(userEmail);

        // 5. Configurar los listeners (acciones) de los botones
        configurarListeners();
    }


    private void cargarDatosUsuario(String email) {
        // Nos aseguramos de que el email no sea nulo o vacío.
        if (email != null && !email.isEmpty()) {

            // Movemos la consulta a la base de datos a un hilo secundario
            // para evitar que la aplicación se cierre.
            new Thread(() -> {
                // Realizamos la búsqueda en la base de datos DENTRO del hilo.
                Usuario usuarioActual = LectiaDatabase.getDatabase(getApplicationContext())
                        .usuarioDao()
                        .findByEmail(email);

                // Para actualizar la interfaz (como un TextView), debemos volver
                // al hilo principal (UI Thread) usando runOnUiThread.
                runOnUiThread(() -> {
                    if (usuarioActual != null) {
                        // Si encontramos al usuario, obtenemos su nombre.
                        String nombreUsuario = usuarioActual.getNombre();
                        // Personalizamos y mostramos el saludo.
                        tvSaludo.setText("¡Hola, " + nombreUsuario + "!");
                    } else {
                        // Si por alguna razón no se encuentra, mostramos un saludo genérico.
                        tvSaludo.setText("¡Bienvenido/a!");
                        Toast.makeText(MenuActivity.this, "Error: no se encontró el usuario.", Toast.LENGTH_SHORT).show();
                    }
                });

            }).start(); // ¡Importante! No olvides iniciar el hilo.
            // --- FIN DE LA SOLUCIÓN ---

        } else {
            // Si no se recibió ningún email, mostramos un saludo genérico.
            // Esto se ejecuta de inmediato si el email es nulo.
            tvSaludo.setText("¡Bienvenido/a!");
            Toast.makeText(MenuActivity.this, "Error: no se recibió el email del usuario.", Toast.LENGTH_SHORT).show();
        }
    }




    // En MenuActivity.java, reemplaza tu método configurarListeners con este:

    private void configurarListeners() {
        // Acción para el botón de editar foto
        btnEditarFoto.setOnClickListener(v -> {
            Toast.makeText(MenuActivity.this, "Función para editar perfil próximamente.", Toast.LENGTH_SHORT).show();
        });

        // Acción para el botón de buscar libros
        btnLibros.setOnClickListener(v -> {
            Intent intent = new Intent(MenuActivity.this, ListlibrosActivity.class);
            startActivity(intent);
        });

        // Acción para el botón de clubes
        if (btnClubes != null) {
            btnClubes.setOnClickListener(v -> {
                // Cambia "ClubesActivity.class" si tu actividad se llama diferente.
                Intent intent = new Intent(MenuActivity.this, ClubesActivity.class);
                startActivity(intent);
            });
        }
    }}