package com.example.lectia.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

@Entity(tableName = "mensajes",
        foreignKeys = {
                @ForeignKey(entity = Club.class,
                        parentColumns = "id",
                        childColumns = "clubId",
                        onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = Usuario.class,
                        parentColumns = "id",
                        childColumns = "usuarioId",
                        onDelete = ForeignKey.SET_NULL)
        },
        indices = {@Index("clubId"), @Index("usuarioId")}
)
public class MensajesClub {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    private String contenido;

    private long timestamp;

    // Nuevo campo para la ruta de la imagen del comentario
    private String imagenPath;

    @ColumnInfo(name = "clubId")
    private int clubId;

    @ColumnInfo(name = "usuarioId")
    private Integer usuarioId;

    // --- Constructor Actualizado ---
    public MensajesClub(@NonNull String contenido, long timestamp, int clubId, Integer usuarioId, String imagenPath) {
        this.contenido = contenido;
        this.timestamp = timestamp;
        this.clubId = clubId;
        this.usuarioId = usuarioId;
        this.imagenPath = imagenPath;
    }

    // --- Getters ---
    public int getId() { return id; }
    @NonNull
    public String getContenido() { return contenido; }
    public long getTimestamp() { return timestamp; }
    public int getClubId() { return clubId; }
    public Integer getUsuarioId() { return usuarioId; }
    public String getImagenPath() { return imagenPath; }

    // --- Setters (Room los necesita) ---
    public void setId(int id) { this.id = id; }
    public void setContenido(@NonNull String contenido) { this.contenido = contenido; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
    public void setClubId(int clubId) { this.clubId = clubId; }
    public void setUsuarioId(Integer usuarioId) { this.usuarioId = usuarioId; }
    public void setImagenPath(String imagenPath) { this.imagenPath = imagenPath; }
}
