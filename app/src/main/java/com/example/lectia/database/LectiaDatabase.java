package com.example.lectia.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

// 1. Corregido: Usamos el nombre de clase correcto ClubUsuarioCrossRef.class
@Database(entities = {Usuario.class, Club.class, MensajesClub.class, ClubUsuarioCrossRef.class}, version = 6)
public abstract class LectiaDatabase extends RoomDatabase {

    public abstract UsuarioDao usuarioDao();
    public abstract ClubDao clubDao();
    public abstract MensajeClubDao mensajeClubDao();
    public abstract UsuarioClubDao usuarioClubDao();

    private static volatile LectiaDatabase database;

    public static LectiaDatabase getDatabase(final Context context) {
        if (database == null) {
            synchronized (LectiaDatabase.class) {
                if (database == null) {
                    database = Room.databaseBuilder(context.getApplicationContext(),
                                    LectiaDatabase.class, "lectia_db")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return database;
    }
}
