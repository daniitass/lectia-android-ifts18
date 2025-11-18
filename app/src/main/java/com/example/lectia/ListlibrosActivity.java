// Ruta: app/src/main/java/com/example/lectia/ListlibrosActivity.java
package com.example.lectia;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lectia.adapter.BookAdapter;
import com.example.lectia.api.ApiClient;
import com.example.lectia.api.GoogleBooksService;
import com.example.lectia.model.BookResponse;

import java.util.Collections;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListlibrosActivity extends AppCompatActivity {

    private SearchView searchView;
    private RecyclerView recyclerViewLibros;
    private BookAdapter bookAdapter;
    private GoogleBooksService googleBooksService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_libros);

        searchView = findViewById(R.id.searchView);
        recyclerViewLibros = findViewById(R.id.recyclerViewLibros);

        googleBooksService = ApiClient.getClient().create(GoogleBooksService.class);

        setupRecyclerView();
        setupSearchView();

        // ✅ 1. Realiza la búsqueda inicial de los libros más recientes en español.
        fetchNewestSpanishBooks();
    }

    // ✅ 2. Nuevo método para la búsqueda inicial.
    private void fetchNewestSpanishBooks() {
        // Usamos "libros" como término genérico para obtener una buena base de resultados.
        // La magia está en el parámetro "newest" que ordenará los resultados.
        fetchBooks("literatura", null, null);
    }

    private void setupRecyclerView() {
        bookAdapter = new BookAdapter(this);
        recyclerViewLibros.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewLibros.setAdapter(bookAdapter);
    }

    private void setupSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query != null && !query.trim().isEmpty()) {
                    // ✅ 3. Búsqueda manual: busca por relevancia (orderBy = null).
                    fetchBooks(query, null, null);
                    searchView.clearFocus();
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    // ✅ 4. El método fetchBooks ahora es más flexible.
    private void fetchBooks(String query, String langCode, String orderBy) {
        Log.d("API_CALL", "Buscando - Query: '" + query + "', Lang: '" + langCode + "', OrderBy: '" + orderBy + "'");
        Toast.makeText(this, "Buscando libros...", Toast.LENGTH_SHORT).show();

        Call<BookResponse> call = googleBooksService.getNewestBooks(query, orderBy, langCode, BuildConfig.BOOKS_API_KEY);

        call.enqueue(new Callback<BookResponse>() {
            @Override
            public void onResponse(Call<BookResponse> call, Response<BookResponse> response) {
                Log.d("API_CALL", "Respuesta recibida. Código: " + response.code());

                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getItems() != null && !response.body().getItems().isEmpty()) {
                        Log.d("API_CALL", "Éxito: Se encontraron " + response.body().getItems().size() + " libros.");
                        bookAdapter.submitList(response.body().getItems());
                    } else {
                        Log.w("API_CALL", "Éxito, pero no se encontraron libros para la consulta: " + query);
                        bookAdapter.submitList(Collections.emptyList());
                        Toast.makeText(ListlibrosActivity.this, "No se encontraron resultados", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Log.e("API_CALL", "Error en la respuesta del servidor: " + response.message());
                    bookAdapter.submitList(Collections.emptyList());
                    Toast.makeText(ListlibrosActivity.this, "Error en la respuesta: " + response.code(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<BookResponse> call, Throwable t) {
                Log.e("API_CALL", "Fallo en la llamada a la API", t);
                bookAdapter.submitList(Collections.emptyList());
                Toast.makeText(ListlibrosActivity.this, "Error de red. Revisa tu conexión.", Toast.LENGTH_LONG).show();
            }
        });
    }
}
