package com.example.lectia;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.lectia.database.LectiaDatabase;
import com.example.lectia.database.Usuario;
import com.google.android.material.textfield.TextInputEditText;

public class LogActivity extends AppCompatActivity {

    private TextInputEditText inputMail, inputPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        // --- INICIALIZACIÓN DE VISTAS ---
        inputMail = findViewById(R.id.inputMail);
        inputPassword = findViewById(R.id.inputPassword);
        Button btnIngresar = findViewById(R.id.materialButton);
        TextView tvRegistro = findViewById(R.id.registro);

        // --- CONFIGURACIÓN DE LISTENERS ---
        btnIngresar.setOnClickListener(v -> validarLogin());

        tvRegistro.setOnClickListener(v -> {
            Intent intent = new Intent(LogActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    /**
     * Valida las credenciales del usuario moviendo la lógica de la base de datos
     * a un hilo secundario para evitar que la aplicación se crashee.
     */
    private void validarLogin() {
        // 1. Obtenemos el texto de los campos de entrada.
        String mail = "";
        if (inputMail.getText() != null) {
            mail = inputMail.getText().toString().trim();
        }

        String password = "";
        if (inputPassword.getText() != null) {
            password = inputPassword.getText().toString().trim();
        }

        // 2. Validamos que los campos no estén vacíos (esto se puede hacer en el hilo principal).
        if (mail.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor, introduce email y contraseña", Toast.LENGTH_SHORT).show();
            return;
        }

        // --- INICIO DE LA OPERACIÓN EN HILO SECUNDARIO ---
        // Esto es CRUCIAL. Evita el crash por "NetworkOnMainThreadException".
        final String finalMail = mail;
        final String finalPassword = password;

        new Thread(() -> {
            try {
                // 3. Realizamos la búsqueda en la base de datos DENTRO del hilo secundario.
                Usuario usuario = LectiaDatabase.getDatabase(getApplicationContext()).usuarioDao().findByEmail(finalMail);

                // 4. Procesamos el resultado VOLVIENDO al hilo principal (UI Thread).
                //    No se pueden mostrar Toasts o cambiar de actividad desde un hilo secundario.
                runOnUiThread(() -> {
                    if (usuario == null) {
                        // El email no existe en la base de datos.
                        Toast.makeText(LogActivity.this, "Email no registrado o incorrecto", Toast.LENGTH_LONG).show();
                    } else {
                        // El email sí existe, ahora comparamos la contraseña.
                        if (usuario.getPassword().equals(finalPassword)) {
                            // Contraseña CORRECTA: iniciamos sesión.
                            Toast.makeText(LogActivity.this, "¡Bienvenido, " + usuario.getNombre() + "!", Toast.LENGTH_LONG).show();

                            // Navegamos a la siguiente actividad.
                            Intent intent = new Intent(LogActivity.this, MenuActivity.class); // Asumo que tu menú se llama MenuActivity
                            intent.putExtra("USER_EMAIL", finalMail); // Es útil pasar datos a la siguiente pantalla.
                            startActivity(intent);
                            finish(); // Cerramos LogActivity para que el usuario no pueda volver con el botón "atrás".
                        } else {
                            // Contraseña INCORRECTA.
                            Toast.makeText(LogActivity.this, "Contraseña incorrecta", Toast.LENGTH_LONG).show();
                        }
                    }
                });

            } catch (Exception e) {
                // Captura cualquier otro error inesperado durante la operación de la base de datos.
                runOnUiThread(() -> {
                    Toast.makeText(LogActivity.this, "Ocurrió un error inesperado al validar.", Toast.LENGTH_LONG).show();
                    // Para depuración, es útil imprimir el error en el Logcat.
                    e.printStackTrace();
                });
            }
        }).start(); // ¡No olvides iniciar el hilo!
    }
}