package com.example.lectia;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.lectia.database.LectiaDatabase;
import com.example.lectia.database.Usuario;
import com.google.android.material.textfield.TextInputEditText;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText inputMail, inputNombreUsuario, inputPassword, inputConfirmPassword;
    private Button btnRegistrar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        inputMail = findViewById(R.id.inputMail);
        inputNombreUsuario = findViewById(R.id.inputNombreUsuario);
        inputPassword = findViewById(R.id.inputPassword);
        inputConfirmPassword = findViewById(R.id.inputConfirmPassword);
        Button btnRegistrar = findViewById(R.id.button_register);

        btnRegistrar.setOnClickListener(v -> registrarNuevoUsuario());
    }

    private void registrarNuevoUsuario() {
        // 1. Recoger los datos de TODOS los campos de texto
        String mail = inputMail.getText().toString().trim();String nombre = inputNombreUsuario.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();
        String confirmPassword = inputConfirmPassword.getText().toString().trim();

        // 2. Validaciones
        if (mail.isEmpty() || nombre.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            return;
        }

        // --- CÓDIGO CORREGIDO ---
        // 3. Crear el nuevo objeto Usuario usando el constructor vacío y los setters.
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombre(nombre);
        nuevoUsuario.setEmail(mail);
        nuevoUsuario.setPassword(password);

        new Thread(() -> { // Es buena práctica hacer las operaciones de BD en otro hilo
            try {
                // 4. Guardar el usuario en la base de datos usando el método INSERT.
                // Asegúrate que en tu UsuarioDao el método se llama insert()
                LectiaDatabase.getDatabase(getApplicationContext()).usuarioDao().registrarUsuario(nuevoUsuario);

                // 5. Volver al hilo principal para mostrar el Toast y cambiar de actividad
                runOnUiThread(() -> {
                    Toast.makeText(RegisterActivity.this, "¡Registro exitoso! Por favor, inicia sesión.", Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(RegisterActivity.this, LogActivity.class);
                    startActivity(intent);
                    finish(); // Cerramos la actividad de registro
                });

            } catch (Exception e) {
                // Este error puede saltar si el email ya existe.
                runOnUiThread(() -> {
                    Toast.makeText(RegisterActivity.this, "El email ya está registrado o hubo un error.", Toast.LENGTH_LONG).show();
                });
            }
        }).start();
    }

}