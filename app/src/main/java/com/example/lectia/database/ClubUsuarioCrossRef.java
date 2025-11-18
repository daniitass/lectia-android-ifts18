package com.example.lectia.database;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

@Entity(tableName = "usuario_club_join",
        primaryKeys = {"usuarioId", "clubId"},
        indices = {@Index("clubId")},
        foreignKeys = {
                // Corregido: La columna en la tabla Usuario se llama "id"
                @ForeignKey(entity = Usuario.class,
                        parentColumns = "id",
                        childColumns = "usuarioId",
                        onDelete = ForeignKey.CASCADE),
                // Corregido: La columna en la tabla Club se llama "id"
                @ForeignKey(entity = Club.class,
                        parentColumns = "id",
                        childColumns = "clubId",
                        onDelete = ForeignKey.CASCADE)
        })
public class ClubUsuarioCrossRef {
    public int usuarioId;
    public int clubId;

    public ClubUsuarioCrossRef(int usuarioId, int clubId) {
        this.usuarioId = usuarioId;
        this.clubId = clubId;
    }
}
