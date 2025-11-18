package com.example.lectia;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lectia.database.Club;
import com.example.lectia.database.LectiaDatabase;

import java.util.ArrayList;
import java.util.List;

public class MisClubesActivity extends AppCompatActivity {

    private RecyclerView recyclerViewMisClubes;
    // 1. Cambiamos el tipo del adaptador al nuevo MyClubAdapter
    private MyClubAdapter clubAdapter;
    private List<Club> misClubes;
    private Button btnClubes, btnMisClubes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_clubes);

        // --- Vinculación de Vistas ---
        recyclerViewMisClubes = findViewById(R.id.recyclerClubes);
        btnClubes = findViewById(R.id.btnClubes);
        btnMisClubes = findViewById(R.id.btnMisClubes);

        // --- Configuración del RecyclerView ---
        recyclerViewMisClubes.setLayoutManager(new LinearLayoutManager(this));
        misClubes = new ArrayList<>();
        // 2. Instanciamos el nuevo MyClubAdapter
        clubAdapter = new MyClubAdapter(misClubes, this);
        recyclerViewMisClubes.setAdapter(clubAdapter);

        // --- Configuración de Listeners ---
        configurarListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Cada vez que volvamos a esta pantalla, recargamos los clubes
        cargarMisClubesDesdeBD();
    }

    private void configurarListeners() {
        btnClubes.setOnClickListener(v -> {
            finish(); // Cierra esta actividad y vuelve a la anterior (ClubesActivity)
        });
    }

    private void cargarMisClubesDesdeBD() {
        SharedPreferences sharedPreferences = getSharedPreferences(MenuActivity.PREFS_NAME, Context.MODE_PRIVATE);
        int usuarioId = sharedPreferences.getInt(MenuActivity.KEY_USER_ID, -1);

        if (usuarioId == -1) { return; }

        new Thread(() -> {
            LectiaDatabase db = LectiaDatabase.getDatabase(getApplicationContext());
            List<Integer> clubIds = db.usuarioClubDao().getClubIdsForUser(usuarioId);

            if (clubIds != null && !clubIds.isEmpty()) {
                List<Club> clubesDelUsuario = db.clubDao().getClubsByIds(clubIds);

                runOnUiThread(() -> {
                    misClubes.clear();
                    misClubes.addAll(clubesDelUsuario);
                    clubAdapter.notifyDataSetChanged();
                });
            }
        }).start();
    }
}
