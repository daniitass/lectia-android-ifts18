package com.example.lectia;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ComentarioAdapter extends RecyclerView.Adapter<ComentarioAdapter.ComentarioViewHolder> {

    private List<Comentario> comentarios;
    private Context context;

    public ComentarioAdapter(List<Comentario> comentarios, Context context) {
        this.comentarios = comentarios;
        this.context = context;
    }

    @NonNull
    @Override
    public ComentarioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_comentario, parent, false);
        return new ComentarioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ComentarioViewHolder holder, int position) {
        Comentario comentario = comentarios.get(position);

        // Mostrar autor
        holder.autor.setText(comentario.getAutor());

        // Gestionar visibilidad del texto
        if (comentario.getContenido() != null && !comentario.getContenido().isEmpty()) {
            holder.contenido.setText(comentario.getContenido());
            holder.contenido.setVisibility(View.VISIBLE);
        } else {
            holder.contenido.setVisibility(View.GONE);
        }

        // Gestionar visibilidad de la imagen
        if (comentario.getImagenPath() != null && !comentario.getImagenPath().isEmpty()) {
            holder.imagenAdjunta.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .load(comentario.getImagenPath())
                    .into(holder.imagenAdjunta);
        } else {
            holder.imagenAdjunta.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return comentarios.size();
    }

    public static class ComentarioViewHolder extends RecyclerView.ViewHolder {
        TextView autor;
        TextView contenido;
        ImageView imagenAdjunta; // 1. Vinculamos el ImageView

        public ComentarioViewHolder(@NonNull View itemView) {
            super(itemView);
            autor = itemView.findViewById(R.id.textAutorComentario);
            contenido = itemView.findViewById(R.id.textContenidoComentario);
            imagenAdjunta = itemView.findViewById(R.id.imagenComentarioAdjunta); // 2. Y lo encontramos por su ID
        }
    }
}
