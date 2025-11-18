package com.example.lectia; // Asegúrate de que tu package sea el correcto

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lectia.database.LectiaDatabase;
import com.example.lectia.database.Usuario;

public class MenuActivity extends AppCompatActivity {

    private TextView tvSaludo;
    private ImageButton btnEditarFoto;
    private Button btnLibros;
    private Button btnClubes;

    public static final String PREFS_NAME = "LectiaPrefs";
    public static final String KEY_USER_NAME = "userName";
    public static final String KEY_USER_ID = "userId"; // Nueva clave para el ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        tvSaludo = findViewById(R.id.tvSaludo);
        btnEditarFoto = findViewById(R.id.btnEditarFoto);
        btnLibros = findViewById(R.id.btnLibros);
        btnClubes = findViewById(R.id.btnClubes);

        String userEmail = getIntent().getStringExtra("USER_EMAIL");

        cargarDatosUsuario(userEmail);
        configurarListeners();
    }

    private void cargarDatosUsuario(String email) {
        if (email != null && !email.isEmpty()) {
            new Thread(() -> {
                Usuario usuarioActual = LectiaDatabase.getDatabase(getApplicationContext())
                        .usuarioDao()
                        .findByEmail(email);

                runOnUiThread(() -> {
                    if (usuarioActual != null) {
                        String nombreUsuario = usuarioActual.getNombre();
                        int idUsuario = usuarioActual.getId(); // Obtenemos el ID
                        tvSaludo.setText("¡Hola, " + nombreUsuario + "!");

                        // --- GUARDAR DATOS DE USUARIO EN PREFERENCES ---
                        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(KEY_USER_NAME, nombreUsuario);
                        editor.putInt(KEY_USER_ID, idUsuario); // Guardamos el ID
                        editor.apply();
                        // --- FIN DEL CAMBIO ---

                    } else {
                        tvSaludo.setText("¡Bienvenido/a!");
                        Toast.makeText(MenuActivity.this, "Error: no se encontró el usuario.", Toast.LENGTH_SHORT).show();
                    }
                });

            }).start();
        } else {
            tvSaludo.setText("¡Bienvenido/a!");
            Toast.makeText(MenuActivity.this, "Error: no se recibió el email del usuario.", Toast.LENGTH_SHORT).show();
        }
    }

    private void configurarListeners() {
        btnEditarFoto.setOnClickListener(v -> {
            Toast.makeText(MenuActivity.this, "Función para editar perfil próximamente.", Toast.LENGTH_SHORT).show();
        });

        btnLibros.setOnClickListener(v -> {
            Intent intent = new Intent(MenuActivity.this, ListlibrosActivity.class);
            startActivity(intent);
        });

        if (btnClubes != null) {
            btnClubes.setOnClickListener(v -> {
                Intent intent = new Intent(MenuActivity.this, ClubesActivity.class);
                startActivity(intent);
            });
        }
    }
}