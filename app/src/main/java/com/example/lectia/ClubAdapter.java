package com.example.lectia;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.lectia.database.Club;

import java.util.List;

public class ClubAdapter extends RecyclerView.Adapter<ClubAdapter.ClubViewHolder> {

    private List<Club> clubes;
    private Context context;

    public ClubAdapter(List<Club> clubes, Context context) {
        this.clubes = clubes;
        this.context = context;
    }

    @NonNull
    @Override
    public ClubViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_club, parent, false);
        return new ClubViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClubViewHolder holder, int position) {
        Club club = clubes.get(position);
        holder.textNombreClub.setText(club.getNombre());
        holder.textDescripcionClub.setText(club.getDescripcion());
        String imagenPath = club.getImagenPath();
        Toast.makeText(context, "Cargando imagen: " + imagenPath, Toast.LENGTH_LONG).show();
        Glide.with(context)
                .load(club.getImagenPath())
                .into(holder.imagenClub);

        holder.btnJoinClub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetallesClubActivity.class);
                intent.putExtra("clubId", club.getId());
                intent.putExtra("nombreClub", club.getNombre());
                intent.putExtra("descripcionClub", club.getDescripcion());
                // Añadido: Pasamos también la ruta de la imagen
                intent.putExtra("imagenClubPath", club.getImagenPath());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return clubes.size();
    }

    public static class ClubViewHolder extends RecyclerView.ViewHolder {

        ImageView imagenClub;
        TextView textNombreClub;
        TextView textDescripcionClub;
        ImageButton btnJoinClub;

        public ClubViewHolder(@NonNull View itemView) {
            super(itemView);
            imagenClub = itemView.findViewById(R.id.imagenClub);
            textNombreClub = itemView.findViewById(R.id.textNombreClub);
            textDescripcionClub = itemView.findViewById(R.id.textDescripcionClub);
            btnJoinClub = itemView.findViewById(R.id.btnJoinClub);
        }
    }
}
