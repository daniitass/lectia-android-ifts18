package com.example.lectia;

import android.content.Context;
import android.content.SharedPreferences;
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
import com.example.lectia.database.LectiaDatabase;

import java.util.List;

public class MyClubAdapter extends RecyclerView.Adapter<MyClubAdapter.MyClubViewHolder> {

    private List<Club> misClubes;
    private Context context;

    public MyClubAdapter(List<Club> misClubes, Context context) {
        this.misClubes = misClubes;
        this.context = context;
    }

    @NonNull
    @Override
    public MyClubViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_my_club, parent, false);
        return new MyClubViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyClubViewHolder holder, int position) {
        Club club = misClubes.get(position);
        holder.clubTitle.setText(club.getNombre());
        holder.clubSubhead.setText(club.getDescripcion());

        Glide.with(context)
                .load(club.getImagenPath())
                .into(holder.clubImage);

        // --- LÓGICA PARA DEJAR UN CLUB ---
        holder.btnDelete.setOnClickListener(v -> {
            SharedPreferences sharedPreferences = context.getSharedPreferences(MenuActivity.PREFS_NAME, Context.MODE_PRIVATE);
            int usuarioId = sharedPreferences.getInt(MenuActivity.KEY_USER_ID, -1);

            if (usuarioId == -1) { return; }

            int clubId = club.getId();

            new Thread(() -> {
                // Eliminamos la relación de la base de datos
                LectiaDatabase.getDatabase(context.getApplicationContext()).usuarioClubDao().leaveClub(usuarioId, clubId);

                // Volvemos al hilo principal para actualizar la UI
                ((MisClubesActivity) context).runOnUiThread(() -> {
                    // Eliminamos el club de la lista actual
                    misClubes.remove(position);
                    // Notificamos al adaptador que un elemento ha sido eliminado en esa posición
                    notifyItemRemoved(position);
                    // Notificamos para actualizar las posiciones del resto de elementos
                    notifyItemRangeChanged(position, misClubes.size());
                    Toast.makeText(context, "Has dejado el club: " + club.getNombre(), Toast.LENGTH_SHORT).show();
                });
            }).start();
        });
    }

    @Override
    public int getItemCount() {
        return misClubes.size();
    }

    public static class MyClubViewHolder extends RecyclerView.ViewHolder {

        TextView clubTitle;
        TextView clubSubhead;
        ImageView clubImage;
        ImageButton btnDelete;

        public MyClubViewHolder(@NonNull View itemView) {
            super(itemView);
            clubTitle = itemView.findViewById(R.id.clubTitle);
            clubSubhead = itemView.findViewById(R.id.clubSubhead);
            clubImage = itemView.findViewById(R.id.clubImage);
            btnDelete = itemView.findViewById(R.id.btn_delete_club);
        }
    }
}
