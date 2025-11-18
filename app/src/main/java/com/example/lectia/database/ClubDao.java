package com.example.lectia.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ClubDao {

    @Insert
    void insert(Club club);

    @Query("SELECT * FROM clubes")
    List<Club> getAll();

    @Query("SELECT * FROM clubes WHERE id = :id")
    Club getById(int id);

    // --- NUEVO MÉTODO ---
    // Busca todos los clubes cuyos IDs estén en la lista que le pasamos.
    @Query("SELECT * FROM clubes WHERE id IN (:clubIds)")
    List<Club> getClubsByIds(List<Integer> clubIds);

    @Update
    void update(Club club);

    @Query("SELECT * FROM clubes")
    List<Club> getAllClubs();

    @Delete
    void delete(Club club);
}
