package com.example.lectia;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;import androidx.appcompat.app.AppCompatActivity;

import com.example.lectia.database.LectiaDatabase;
import com.example.lectia.database.Usuario;
import com.google.android.material.textfield.TextInputEditText;

public class LogActivity extends AppCompatActivity {

    private TextInputEditText inputMail, inputPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        // 1. Inicializamos las vistas
        inputMail = findViewById(R.id.inputMail);
        inputPassword = findViewById(R.id.inputPassword);
        Button btnIngresar = findViewById(R.id.materialButton);
        TextView tvRegistro = findViewById(R.id.registro);

        // 2. Configuramos los listeners de los botones
        btnIngresar.setOnClickListener(v -> validarLogin());

        tvRegistro.setOnClickListener(v -> {
            Intent intent = new Intent(LogActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    /**
     * Valida las credenciales del usuario contra la base de datos.
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

        // 2. Validamos que los campos no estén vacíos.
        if (mail.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor, introduce email y contraseña", Toast.LENGTH_SHORT).show();
            return;
        }

        // 3. Realizamos la búsqueda en la base de datos.
        //    Esta es la forma correcta y segura que funciona con tu configuración.
        //    Obtiene la instancia de la BD y ejecuta la consulta en un solo paso.
        Usuario usuario = LectiaDatabase.getDatabase(getApplicationContext()).usuarioDao().findByEmail(mail);

        // 4. Verificamos el resultado de la búsqueda.
        if (usuario == null) {
            // El email no existe en la base de datos.
            Toast.makeText(LogActivity.this, "Email no registrado", Toast.LENGTH_LONG).show();
        } else {
            // El email sí existe, ahora comparamos la contraseña.
            if (usuario.getPassword().equals(password)) {
                // Contraseña CORRECTA: iniciamos sesión.
                Toast.makeText(LogActivity.this, "¡Bienvenido, " + usuario.getNombreUsuario() + "!", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(LogActivity.this, MenuActivity.class);
                intent.putExtra("USER_EMAIL", mail); // Pasamos el email a la siguiente pantalla.
                startActivity(intent);
                finish(); // Cerramos LogActivity para que el usuario no pueda volver atrás.
            } else {
                // Contraseña INCORRECTA.
                Toast.makeText(LogActivity.this, "Contraseña incorrecta", Toast.LENGTH_LONG).show();
            }
        }
    }
}
