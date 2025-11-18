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

    // Método para insertar un nuevo club.
    @Insert
    void insert(Club club);

    // Método para obtener todos los clubes.
    // Lo necesita ClubesActivity para mostrar la lista.
    @Query("SELECT * FROM clubes") // "clubes" es el nombre de tu tabla
    List<Club> getAll();

    @Query("SELECT * FROM clubes WHERE id = :id")
    Club getById(int id);

    @Update
    void update(Club club);

    // Le dice a Room que ejecute la consulta SQL "SELECT * FROM clubs"
    // y que el resultado lo convierta en una Lista de objetos Club.
    @Query("SELECT * FROM clubes")
    List<Club> getAllClubs();

    @Delete
    void delete(Club club);
}