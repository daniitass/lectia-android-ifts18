package com.example.lectia;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
// Importamos la clase Club del paquete de la base de datos explícitamente
import com.example.lectia.database.Club;
import com.example.lectia.database.LectiaDatabase;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class ClubesActivity extends AppCompatActivity {

    private RecyclerView recyclerViewClubes;
    private ClubAdapter clubAdapter;
    private List<com.example.lectia.database.Club> listaDeClubes;
    private FloatingActionButton fabAddClub;
    private Button btnClubes, btnMisClubes; // 1. Declaramos los botones de navegación

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clubes);

        // --- Vinculación de Vistas ---
        recyclerViewClubes = findViewById(R.id.recyclerClubes);
        fabAddClub = findViewById(R.id.fabAddClub);
        btnClubes = findViewById(R.id.btnClubes); // 2. Vinculamos los botones
        btnMisClubes = findViewById(R.id.btnMisClubes);

        // --- Configuración del RecyclerView ---
        recyclerViewClubes.setLayoutManager(new LinearLayoutManager(this));
        listaDeClubes = new ArrayList<>();
        clubAdapter = new ClubAdapter(listaDeClubes, this);
        recyclerViewClubes.setAdapter(clubAdapter);

        // --- Configuración de Listeners ---
        configurarListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarClubesDesdeBD();
    }

    private void configurarListeners() {
        fabAddClub.setOnClickListener(v -> {
            Intent intent = new Intent(ClubesActivity.this, CrearClubActivity.class);
            startActivity(intent);
        });

        // 3. Añadimos el listener para "Mis Clubes"
        btnMisClubes.setOnClickListener(v -> {
            Intent intent = new Intent(ClubesActivity.this, MisClubesActivity.class);
            startActivity(intent);
        });

        // El botón "Clubes" ya está en esta actividad, así que no necesita acción
        // o podría tener una para refrescar la lista si quisiéramos.
    }

    private void cargarClubesDesdeBD() {
        new Thread(() -> {
            List<com.example.lectia.database.Club> clubesEnBD = LectiaDatabase.getDatabase(getApplicationContext())
                    .clubDao()
                    .getAllClubs();

            runOnUiThread(() -> {
                listaDeClubes.clear();
                listaDeClubes.addAll(clubesEnBD);
                clubAdapter.notifyDataSetChanged();
            });
        }).start();
    }
}
