// Ruta: app/src/main/java/com/example/lectia/database/Mensaje.java
package com.example.lectia.database;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

// Definimos la tabla y sus relaciones con las otras dos.
@Entity(tableName = "mensajes",
        // Aquí declaramos las "Claves Foráneas" (Foreign Keys).
        // Son las reglas que conectan esta tabla con las otras.
        foreignKeys = {
                // Relación 1: Conecta 'IDClub' de esta tabla con 'IDClub' de la tabla 'clubes'.
                @ForeignKey(entity = Club.class,
                        parentColumns = "IDClub", // Campo en la tabla padre (Club)
                        childColumns = "IDClub",    // Campo en esta tabla (Mensaje)
                        onDelete = ForeignKey.CASCADE), // Si se borra un club, se borran todos sus mensajes.

                // Relación 2: Conecta 'IDUsuario' de esta tabla con 'IDUsuario' de la tabla 'usuarios'.
                @ForeignKey(entity = Usuario.class,
                        parentColumns = "IDUsuario", // Campo en la tabla padre (Usuario)
                        childColumns = "IDUsuario",    // Campo en esta tabla (Mensaje)
                        onDelete = ForeignKey.SET_NULL) // Opción: Si un usuario se borra, el mensaje no se borra,
                // pero su autoría queda como 'nula' o anónima.
                // Podríamos usar CASCADE si preferimos que se borren.
        },
        // Los índices hacen que las búsquedas por ID de club o usuario sean mucho más rápidas.
        indices = {@Index("IDClub"), @Index("IDUsuario")}
)
public class MensajesClub {

    @PrimaryKey(autoGenerate = true)
    private int IDMensaje; // Identificador único para cada mensaje.

    @NonNull
    private String Contenido; // El texto del mensaje.

    private long Timestamp; // La fecha/hora en que se envió el mensaje.
    // Se guarda como 'long' (milisegundos desde 1970), que es muy eficiente.

    // --- Campos de Relación (Claves Foráneas) ---

    // Este campo guarda el ID del club al que pertenece el mensaje.
    private int IDClub;

    // Este campo guarda el ID del usuario que envió el mensaje.
    // Usamos 'Integer' en lugar de 'int' para permitir que sea nulo (por la regla onDelete = SET_NULL).
    private Integer IDUsuario;

    // --- Constructor ---
    public MensajesClub(@NonNull String Contenido, long Timestamp, int IDClub, Integer IDUsuario) {
        this.Contenido = Contenido;
        this.Timestamp = Timestamp;
        this.IDClub = IDClub;
        this.IDUsuario = IDUsuario;
    }

    // --- Getters ---
    public int getIDMensaje() { return IDMensaje; }
    @NonNull
    public String getContenido() { return Contenido; }
    public long getTimestamp() { return Timestamp; }
    public int getIDClub() { return IDClub; }
    public Integer getIDUsuario() { return IDUsuario; }

    // --- Setter para el ID (Room lo necesita) ---
    public void setIDMensaje(int IDMensaje) { this.IDMensaje = IDMensaje; }
}
