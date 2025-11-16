// EN: RegisterActivity.java
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
        String mail = inputMail.getText().toString().trim();
        // --- ESTA LÍNEA ES LA CLAVE ---
        String nombre = inputNombreUsuario.getText().toString().trim(); // Se recoge el nombre del campo de texto.
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

        // --- SOLUCIÓN DEFINITIVA ---

        // 3. Crear el nuevo objeto Usuario USANDO EL NOMBRE RECOGIDO
        Usuario nuevoUsuario = new Usuario(mail, password, nombre, null); // Se pasa 'nombre' al constructor.

        try {
            // 4. Guardar el usuario en la base de datos usando tu método existente
            LectiaDatabase.getDatabase(getApplicationContext()).usuarioDao().registrarUsuario(nuevoUsuario);

            Toast.makeText(this, "¡Registro exitoso! Por favor, inicia sesión.", Toast.LENGTH_LONG).show();

            // Llevamos al usuario a la pantalla de Login
            Intent intent = new Intent(RegisterActivity.this, LogActivity.class);
            startActivity(intent);
            finish(); // Cerramos la actividad de registro

        } catch (Exception e) {
            // Este error puede saltar si el email ya existe, gracias a 'OnConflictStrategy.IGNORE'
            Toast.makeText(this, "El email ya está registrado o hubo un error.", Toast.LENGTH_LONG).show();
        }
    }
}

