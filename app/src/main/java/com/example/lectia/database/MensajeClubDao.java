package com.example.lectia.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MensajeClubDao {

    @Insert
    void insert(MensajesClub mensaje);

    // Corregido: La consulta ahora usa el nombre de columna correcto "clubId"
    @Query("SELECT * FROM mensajes WHERE clubId = :clubId ORDER BY Timestamp ASC")
    List<MensajesClub> getMensajesPorClub(int clubId);
}
