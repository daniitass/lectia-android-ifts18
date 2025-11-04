// Ruta: app/src/main/java/com/example/lectia/adapter/BookAdapter.java
package com.example.lectia.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.lectia.R;
import com.example.lectia.model.BookItem;
import com.example.lectia.model.VolumeInfo;

import java.util.List;

public class BookAdapter extends ListAdapter<BookItem, BookAdapter.BookViewHolder> {

    private final Context context;

    public BookAdapter(Context context) {
        super(new DiffUtil.ItemCallback<BookItem>() {
            @Override
            public boolean areItemsTheSame(@NonNull BookItem oldItem, @NonNull BookItem newItem) {
                return oldItem == newItem;
            }

            @Override
            public boolean areContentsTheSame(@NonNull BookItem oldItem, @NonNull BookItem newItem) {
                return oldItem.getVolumeInfo().getTitle().equals(newItem.getVolumeInfo().getTitle());
            }
        });
        this.context = context;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        BookItem bookItem = getItem(position);
        if (bookItem != null && bookItem.getVolumeInfo() != null) {
            holder.bind(bookItem.getVolumeInfo());
        }
    }

    class BookViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewCover;
        TextView textViewTitle;
        TextView textViewAuthor;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewCover = itemView.findViewById(R.id.imageViewBookCover);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewAuthor = itemView.findViewById(R.id.textViewAuthors);
        }

        public void bind(VolumeInfo volumeInfo) {
            textViewTitle.setText(volumeInfo.getTitle());
            List<String> authors = volumeInfo.getAuthors();
            if (authors != null && !authors.isEmpty()) {
                textViewAuthor.setText(String.join(", ", authors));
            } else {
                textViewAuthor.setText("Autor no disponible");
            }
            String imageUrl = null;
            if (volumeInfo.getImageLinks() != null) {
                imageUrl = volumeInfo.getImageLinks().getThumbnail();
            }
            Glide.with(context)
                    .load(imageUrl)
                    .placeholder(R.drawable.logo)
                    .error(R.drawable.logo)
                    .into(imageViewCover);
        }
    }
}
