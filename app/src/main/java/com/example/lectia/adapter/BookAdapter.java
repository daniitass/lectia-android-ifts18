// Ruta: app/src/main/java/com/example/lectia/adapter/BookAdapter.java
package com.example.lectia.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.lectia.DetallesLibro;
import com.example.lectia.R;

// ✅ 1. IMPORTACIONES COMPLETAS DEL MODELO:
//    Importamos todas las clases que necesitamos del paquete 'model'.
import com.example.lectia.model.BookItem;
import com.example.lectia.model.VolumeInfo; // Importamos VolumeInfo
import com.example.lectia.model.ImageLinks; // Importamos ImageLinks (si existe como clase separada)

import java.util.ArrayList;
import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {

    private List<BookItem> books = new ArrayList<>();

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        BookItem book = books.get(position);

        if (book != null) {
            holder.bind(book);

            holder.itemView.setOnClickListener(view -> {
                Log.d("ClickListener", "Clic detectado en BookAdapter. Abriendo DetallesLibro...");

                Context context = view.getContext();
                Intent intent = new Intent(context, DetallesLibro.class);
                intent.putExtra(DetallesLibro.EXTRA_BOOK, book);
                context.startActivity(intent);
            });
        }
    }

    @Override
    public int getItemCount() {
        return books != null ? books.size() : 0;
    }

    public void setBooks(List<BookItem> newBooks) {
        if (newBooks != null) {
            this.books = newBooks;
            notifyDataSetChanged();
        }
    }

    static class BookViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewCover;
        TextView textViewTitle;
        TextView textViewAuthor;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewCover = itemView.findViewById(R.id.imageViewBookCover);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewAuthor = itemView.findViewById(R.id.textViewAuthors);
        }

        public void bind(BookItem book) {
            if (book == null || book.getVolumeInfo() == null) return;

            // ✅ 2. USO DIRECTO DE LAS CLASES IMPORTADAS:
            //    Ahora usamos 'VolumeInfo' directamente, sin prefijo.
            VolumeInfo volumeInfo = book.getVolumeInfo();
            textViewTitle.setText(volumeInfo.getTitle());

            List<String> authors = volumeInfo.getAuthors();
            if (authors != null && !authors.isEmpty()) {
                StringBuilder authorsBuilder = new StringBuilder();
                for (int i = 0; i < authors.size(); i++) {
                    authorsBuilder.append(authors.get(i));
                    if (i < authors.size() - 1) {
                        authorsBuilder.append(", ");
                    }
                }
                textViewAuthor.setText(authorsBuilder.toString());
            } else {
                textViewAuthor.setText("Autor no disponible");
            }

            // ✅ 3. LÓGICA DE IMAGEN CORREGIDA
            ImageLinks imageLinks = volumeInfo.getImageLinks(); // Obtenemos el objeto ImageLinks
            String imageUrl = null;
            if (imageLinks != null) {
                imageUrl = imageLinks.getThumbnail(); // Usamos el getter de ImageLinks
            }

            Glide.with(itemView.getContext())
                    .load((imageUrl != null) ? imageUrl.replace("http://", "https://") : null)
                    .placeholder(R.drawable.logo)
                    .error(R.drawable.logo)
                    .into(imageViewCover);
        }
    }
}
