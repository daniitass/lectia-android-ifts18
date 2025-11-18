package com.example.lectia;

import android.content.Intent;
import android.os.Bundle;
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
    // Especificamos el tipo completo para evitar ambigüedad
    private List<com.example.lectia.database.Club> listaDeClubes;
    private FloatingActionButton fabAddClub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clubes);

        recyclerViewClubes = findViewById(R.id.recyclerClubes);
        fabAddClub = findViewById(R.id.fabAddClub);

        recyclerViewClubes.setLayoutManager(new LinearLayoutManager(this));

        listaDeClubes = new ArrayList<>();
        // El adaptador ya está configurado para recibir la clase Club de la base de datos
        clubAdapter = new ClubAdapter(listaDeClubes, this);
        recyclerViewClubes.setAdapter(clubAdapter);

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
    }

    private void cargarClubesDesdeBD() {
        new Thread(() -> {
            // El DAO devuelve una lista del tipo correcto: List<com.example.lectia.database.Club>
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
