package com.example.lectia.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Usuario.class}, version = 1) // Asegúrate que la versión sea 1
public abstract class LectiaDatabase extends RoomDatabase {

    public abstract UsuarioDao usuarioDao();

    private static volatile LectiaDatabase database;

    public static LectiaDatabase getDatabase(final Context context) {
        if (database == null) {
            synchronized (LectiaDatabase.class) {
                if (database == null) {
                    database = Room.databaseBuilder(context.getApplicationContext(),
                                    LectiaDatabase.class, "lectia_db")
                            .allowMainThreadQueries() // Permite consultas en el hilo principal
                            .fallbackToDestructiveMigration() // Destruye y recrea la BD si hay cambios
                            .build();
                }
            }
        }
        return database;
    }
}
