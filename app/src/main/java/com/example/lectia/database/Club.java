package com.example.lectia.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

@Entity(tableName = "clubes")
public class Club {

    @PrimaryKey(autoGenerate = true)
    private int IDClub;

    @NonNull
    private String NombreClub;

    private String Descripcion;

    // Este campo lo actualizaremos manualmente con la cuenta de miembros.
    private int Miembros;

    // Guardaremos la ruta o URL de la imagen del club.
    private String fotoClub;

    // --- Constructor ---
    public Club(@NonNull String NombreClub, String Descripcion, int Miembros, String fotoClub) {
        this.NombreClub = NombreClub;
        this.Descripcion = Descripcion;
        this.Miembros = Miembros;
        this.fotoClub = fotoClub;
    }

    // --- Getters ---
    public int getIDClub() { return IDClub; }
    @NonNull
    public String getNombreClub() { return NombreClub; }
    public String getDescripcion() { return Descripcion; }
    public int getMiembros() { return Miembros; }
    public String getFotoClub() { return fotoClub; }

    // --- Setters (necesarios para Room) ---
    public void setIDClub(int IDClub) { this.IDClub = IDClub; }
    public void setMiembros(int Miembros) { this.Miembros = Miembros; }
}
