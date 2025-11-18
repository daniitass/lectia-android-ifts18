package com.example.lectia.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UsuarioClubDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void joinClub(ClubUsuarioCrossRef join);

    @Query("SELECT clubId FROM usuario_club_join WHERE usuarioId = :usuarioId")
    List<Integer> getClubIdsForUser(int usuarioId);

    // --- NUEVO MÉTODO ---
    // Elimina la relación entre un usuario y un club.
    @Query("DELETE FROM usuario_club_join WHERE usuarioId = :usuarioId AND clubId = :clubId")
    void leaveClub(int usuarioId, int clubId);

}
