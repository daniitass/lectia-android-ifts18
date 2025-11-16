// EN: Usuario.java
package com.example.lectia.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "usuarios")
public class Usuario {

    @PrimaryKey(autoGenerate = true)
    private int IDUsuario;

    // --- CORRECCIÓN CLAVE ---
    // Todas las variables que son columnas en la BD necesitan la anotación @ColumnInfo.

    @NonNull
    @ColumnInfo(name = "email") // La columna se llamará 'email'
    private String email;

    @NonNull
    @ColumnInfo(name = "password") // La columna se llamará 'password'
    private String password;

    @ColumnInfo(name = "nombre_usuario") // La columna se llamará 'nombre_usuario'
    private String nombreUsuario;

    @ColumnInfo(name = "foto_perfil") // La columna se llamará 'foto_perfil'
    private String fotoPerfil;


    // --- Constructor Corregido ---
    // Los nombres de los parámetros ahora coinciden con los nombres de las variables de la clase.
    public Usuario(@NonNull String email, @NonNull String password, String nombreUsuario, String fotoPerfil) {
        this.email = email;
        this.password = password;
        this.nombreUsuario = nombreUsuario;
        this.fotoPerfil = fotoPerfil;
    }


    // --- Getters Corregidos ---
    // Los nombres de los métodos ahora son consistentes (getEmail en lugar de getMail).
    public int getIDUsuario() { return IDUsuario; }

    @NonNull
    public String getEmail() { return email; }

    @NonNull
    public String getPassword() { return password; }

    public String getNombreUsuario() { return nombreUsuario; }

    public String getFotoPerfil() { return fotoPerfil; }


    // --- Setter para el ID ---
    // Room necesita este setter para poder asignar el ID autogenerado.
    public void setIDUsuario(int IDUsuario) { this.IDUsuario = IDUsuario; }
}

