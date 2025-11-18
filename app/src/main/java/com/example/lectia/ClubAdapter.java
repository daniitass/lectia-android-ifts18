package com.example.lectia;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
        holder.imagenClub.setImageResource(club.getImagen());
    }

    @Override
    public int getItemCount() {
        return clubes.size();
    }

    public static class ClubViewHolder extends RecyclerView.ViewHolder {

        ImageView imagenClub;
        TextView textNombreClub;
        TextView textDescripcionClub;

        public ClubViewHolder(@NonNull View itemView) {
            super(itemView);
            imagenClub = itemView.findViewById(R.id.imagenClub);
            textNombreClub = itemView.findViewById(R.id.textNombreClub);
            textDescripcionClub = itemView.findViewById(R.id.textDescripcionClub);
        }
    }
}
