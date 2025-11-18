// Ruta: app/src/main/java/com/example/lectia/database/UsuarioDao.java
package com.example.lectia.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

// @Dao le dice a Room que esta interfaz es un Objeto de Acceso a Datos.
@Dao
public interface UsuarioDao {

    // --- ACCIÓN DE INSERTAR ---
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void registrarUsuario(Usuario usuario);

    // --- ACCIÓN DE ACTUALIZAR ---
    @Update
    void actualizarUsuario(Usuario usuario);

    //Buscar Usuario Por Mail
    @Query("SELECT * FROM usuarios WHERE email = :email LIMIT 1")
    Usuario findByEmail(String email);

    //Buscar Usuario por ID
    @Query("SELECT * FROM usuarios WHERE id = :id LIMIT 1")
    Usuario findById(int id);


    // @Query("DELETE FROM usuarios WHERE IDUsuario = :id")

}
