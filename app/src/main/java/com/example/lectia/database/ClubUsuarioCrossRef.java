// Ruta: app/src/main/java/com/example/lectia/database/ClubUsuarioCrossRef.java
package com.example.lectia.database;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

// Esta clase representa la tabla de relación entre Clubes y Usuarios.
// Cada fila es un registro que dice "este usuario pertenece a este club".

@Entity(tableName = "club_usuario_relacion",
        // La clave primaria está compuesta por ambos campos.
        // Esto asegura que no se pueda añadir al mismo usuario al mismo club dos veces.
        primaryKeys = {"IDClub", "IDUsuario"},

        // Definimos las claves foráneas para mantener la integridad de los datos.
        foreignKeys = {
                @ForeignKey(entity = Club.class,
                        parentColumns = "IDClub",
                        childColumns = "IDClub",
                        // Si se borra un club, se borran todas sus membresías.
                        onDelete = ForeignKey.CASCADE),

                @ForeignKey(entity = Usuario.class,
                        parentColumns = "IDUsuario",
                        childColumns = "IDUsuario",
                        // Si se borra un usuario, se le da de baja de todos los clubes.
                        onDelete = ForeignKey.CASCADE)
        },
        // Índices para que las búsquedas en esta tabla sean rápidas.
        indices = {@Index("IDClub"), @Index("IDUsuario")}
)
public class ClubUsuarioCrossRef {

    // No se usa autoGenerate aquí. Los IDs vienen de las otras tablas.
    private int IDClub;
    private int IDUsuario;

    // --- Constructor ---
    public ClubUsuarioCrossRef(int IDClub, int IDUsuario) {
        this.IDClub = IDClub;
        this.IDUsuario = IDUsuario;
    }

    // --- Getters ---
    public int getIDClub() { return IDClub; }
    public int getIDUsuario() { return IDUsuario; }
}
