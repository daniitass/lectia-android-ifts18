package com.example.lectia;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.lectia.database.Club;
import com.example.lectia.database.LectiaDatabase;
import com.google.android.material.floatingactionbutton.FloatingActionButton; // Importar FAB
import java.util.ArrayList;
import java.util.List;

public class ClubesActivity extends AppCompatActivity {

    private RecyclerView recyclerViewClubes;
    private ClubAdapter clubAdapter;
    private List<Club> listaDeClubes;
    private FloatingActionButton fabAddClub; // Declarar el botón flotante

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clubes);

        // --- 1. VINCULACIÓN DE VISTAS ---
        // Corregimos el ID para que coincida con tu XML ("recyclerClubes")
        recyclerViewClubes = findViewById(R.id.recyclerClubes); // <-- ¡CORREGIDO!
        fabAddClub = findViewById(R.id.fabAddClub); // Vinculamos el botón flotante

        // 2. CONFIGURACIÓN DEL RECYCLERVIEW
        recyclerViewClubes.setLayoutManager(new LinearLayoutManager(this));

        // 3. INICIALIZACIÓN DE LISTA Y ADAPTADOR
        // La lista empieza vacía, se llenará desde la base de datos.
        listaDeClubes = new ArrayList<>();
        clubAdapter = new ClubAdapter(listaDeClubes, this);
        recyclerViewClubes.setAdapter(clubAdapter);

        // 4. CONFIGURACIÓN DE LISTENERS
        configurarListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Llamamos a cargar los clubes en onResume. Esto asegura que la lista
        // se actualice cada vez que la pantalla se vuelve visible, por ejemplo,
        // después de crear un nuevo club y volver aquí.
        cargarClubesDesdeBD();
    }

    private void configurarListeners() {
        // Acción para el botón flotante de añadir club
        fabAddClub.setOnClickListener(v -> {
            // Navega a la actividad para crear un nuevo club.
            // Asegúrate de que tu actividad se llama CrearClubActivity.
            Intent intent = new Intent(ClubesActivity.this, CrearClubActivity.class);
            startActivity(intent);
        });

        // Aquí podrías añadir listeners para la barra de búsqueda (searchView)
        // o los botones de filtro (btnClubes, btnMisClubes) en el futuro.
    }

    private void cargarClubesDesdeBD() {
        // Se ejecuta la consulta a la base de datos en un hilo secundario
        // para no bloquear la interfaz y evitar que la app crashee.
        new Thread(() -> {
            // Obtenemos la lista de clubes usando el método que creamos en ClubDao
            List<Club> clubesEnBD = LectiaDatabase.getDatabase(getApplicationContext())
                    .clubDao()
                    .getAllClubs();

            // Volvemos al hilo principal (UI Thread) para actualizar la interfaz gráfica
            runOnUiThread(() -> {
                // Limpiamos la lista actual para evitar duplicados al refrescar
                listaDeClubes.clear();
                // Añadimos todos los clubes obtenidos de la base de datos
                listaDeClubes.addAll(clubesEnBD);
                // Notificamos al adaptador que los datos han cambiado, para que
                // el RecyclerView se redibuje y muestre la nueva lista.
                clubAdapter.notifyDataSetChanged();
            });
        }).start();
    }
}
