// Ruta: app/src/main/java/com/example/lectia/DetallesLibro.java
package com.example.lectia;

import android.database.sqlite.SQLiteCursor;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.lectia.model.BookItem;
import com.example.lectia.model.ImageLinks;
import com.example.lectia.model.VolumeInfo;

import java.util.List;
import java.util.Objects;

import com.example.lectia.database.LectiaDatabase;

public class DetallesLibro extends AppCompatActivity {

    // ✅ La llave DEBE ser pública, estática y final para ser accedida desde el Adapter.
    public static final String EXTRA_BOOK = "extra_book_key";

    private ImageView detailBookCover;
    private TextView detailBookTitle;
    private TextView detailBookAuthor;
    private RatingBar detailBookRating; // Asumiendo que quieres añadir esto
    private TextView detailBookDescription;

    // Instancia la base de datos
    private LectiaDatabase lectiaDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_libro);

        new Thread(() -> {
            lectiaDb = LectiaDatabase.getDatabase(getApplicationContext());
        }).start(); //.start() para que el hilo se ejecute.


        // 1. Enlazamos las vistas del XML
        detailBookCover = findViewById(R.id.detail_book_cover);
        detailBookTitle = findViewById(R.id.detail_book_title);
        detailBookAuthor = findViewById(R.id.detail_book_author);
        detailBookRating = findViewById(R.id.detail_book_rating);
        detailBookDescription = findViewById(R.id.detail_book_description);

        // 2. RECUPERAMOS EL OBJETO DE FORMA SEGURA
        BookItem bookItem = null;
        if (getIntent() != null && getIntent().hasExtra(EXTRA_BOOK)) {
            // Hacemos el casting a (BookItem) porque sabemos que enviamos un BookItem Serializable.
            bookItem = (BookItem) getIntent().getSerializableExtra(EXTRA_BOOK);
        }

        // 3. Verificamos que el objeto no sea nulo antes de usarlo
        if (bookItem != null) {
            // Si el objeto existe, llamamos a un método para rellenar la UI.
            populateUI(bookItem);
        } else {
            // Si el objeto es nulo por alguna razón, mostramos un error y cerramos.
            Toast.makeText(this, "Error al cargar la información del libro.", Toast.LENGTH_LONG).show();
            finish(); // Cierra la actividad para no mostrar una pantalla vacía.
        }
    }

    private void populateUI(BookItem book) {
        // ✅ BLINDADO CONTRA NULOS: Nos aseguramos de que VolumeInfo no sea nulo.
        VolumeInfo volumeInfo = book.getVolumeInfo();
        if (volumeInfo == null) {
            // Si no hay 'volumeInfo', no podemos mostrar nada.
            Toast.makeText(this, "Información del volumen no disponible.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // --- Título ---
        detailBookTitle.setText(volumeInfo.getTitle() != null ? volumeInfo.getTitle() : "Título no disponible");

        // --- Autor ---
        List<String> authors = volumeInfo.getAuthors();
        if (authors != null && !authors.isEmpty()) {
            StringBuilder authorsBuilder = new StringBuilder();
            for (int i = 0; i < authors.size(); i++) {
                authorsBuilder.append(authors.get(i));
                if (i < authors.size() - 1) {
                    authorsBuilder.append(", ");
                }
            }
            detailBookAuthor.setText(authorsBuilder.toString());
        } else {
            detailBookAuthor.setText("Autor no disponible");
        }

        // --- Descripción ---
        // Asumiendo que 'description' es un campo que tienes en tu VolumeInfo
        // Si no lo tienes, puedes comentar o eliminar estas líneas.
        String description = volumeInfo.getDescription(); // Asegúrate de tener este campo en tu modelo
        detailBookDescription.setText(description != null ? description : "Descripción no disponible.");


        // --- Imagen de Portada ---
        ImageLinks imageLinks = volumeInfo.getImageLinks();
        String imageUrl = null;
        if (imageLinks != null) {
            // Priorizamos la imagen grande (thumbnail) sobre la pequeña (smallThumbnail)
            imageUrl = imageLinks.getThumbnail() != null ? imageLinks.getThumbnail() : imageLinks.getSmallThumbnail();
        }

        // ✅ LA CORRECCIÓN ESTÁ AQUÍ
        Glide.with(this)
                // Si la URL no es nula, reemplaza http por https. Si es nula, pasa null.
                .load(imageUrl != null ? imageUrl.replace("http://", "https://") : null)
                .placeholder(R.drawable.logo) // Un placeholder mientras carga (ej: tu logo)
                .error(R.drawable.logo)       // Imagen que se muestra si falla la carga (ej: tu logo)
                .into(detailBookCover);


        // --- Rating (Ejemplo) ---
        // La API de Google Books puede devolver 'averageRating'. Si lo tienes en tu modelo, lo puedes usar.
        // float rating = volumeInfo.getAverageRating();
        // detailBookRating.setRating(rating);
        detailBookRating.setRating(4.5f); // Valor de ejemplo
    }
}
