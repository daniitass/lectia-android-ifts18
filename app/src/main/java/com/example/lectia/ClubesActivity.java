package com.example.lectia;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

// Agrega las siguientes importaciones si tus clases están en el mismo paquete
import com.example.lectia.Club;
import com.example.lectia.ClubAdapter;
import com.example.lectia.CrearClubActivity;

public class ClubesActivity extends AppCompatActivity {

    private FloatingActionButton fabAddClub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clubes);

        RecyclerView recyclerClubes = findViewById(R.id.recyclerClubes);
        fabAddClub = findViewById(R.id.fabAddClub);

        // Cargar clubes de ejemplo
        List<Club> clubes = new ArrayList<>();
        clubes.add(new Club("Lectores nocturnos", "Subhead", R.drawable.club_default));
        clubes.add(new Club("Aventureros", "Lectura de viajes", R.drawable.club_default));

        ClubAdapter adapter = new ClubAdapter(clubes, this);
        recyclerClubes.setLayoutManager(new LinearLayoutManager(this));
        recyclerClubes.setAdapter(adapter);

        // Botón flotante: crear nuevo club
        fabAddClub.setOnClickListener(v -> {
            Intent intent = new Intent(this, CrearClubActivity.class);
            startActivity(intent);
        });
    }
}
