package com.example.lectia;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler; // ¡Importante para el temporizador!
import android.os.Looper;  // ¡Importante para el temporizador!
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Usamos un Handler para ejecutar código después de un retraso.
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {

                // 1. Crear un Intent para ir a LogActivity.
                Intent intent = new Intent(MainActivity.this, LogActivity.class);

                // 2. Iniciar la nueva actividad.
                startActivity(intent);

                // Esto evita que el usuario pueda volver a la pantalla de carga

                finish();
            }
        },4000);
    }
}
