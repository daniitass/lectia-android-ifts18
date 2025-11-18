package com.example.lectia;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // --- TAREA 1: AJUSTAR LA PANTALLA ---
        // Este bloque es solo para el padding.
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        }); // <-- El listener termina aquí.

        // --- TAREA 2: INICIAR EL TEMPORIZADOR ---
        // Este bloque se ejecuta después del anterior y no interfiere con él.
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                // El Intent para pasar de MainActivity a LogActivity
                Intent intent = new Intent(MainActivity.this, LogActivity.class);
                startActivity(intent);
                // Cierra la pantalla de carga para que no se pueda volver atrás
                finish();
            }
        }, 3000); // 3 segundos de espera es más que suficiente.
    }
}
