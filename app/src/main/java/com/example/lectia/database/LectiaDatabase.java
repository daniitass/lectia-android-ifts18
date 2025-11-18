package com.example.lectia.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Usuario.class, Club.class}, version = 3)
public abstract class LectiaDatabase extends RoomDatabase {

    public abstract UsuarioDao usuarioDao();
    public abstract ClubDao clubDao();

    private static volatile LectiaDatabase database;

    public static LectiaDatabase getDatabase(final Context context) {
        if (database == null) {
            synchronized (LectiaDatabase.class) {
                if (database == null) {
                    database = Room.databaseBuilder(context.getApplicationContext(),
                                    LectiaDatabase.class, "lectia_db")
                            // Has cambiado el esquema (añadido una tabla), así que la migración destructiva
                            // borrará la base de datos anterior y creará la nueva con la tabla de Clubes.
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return database;
    }
}