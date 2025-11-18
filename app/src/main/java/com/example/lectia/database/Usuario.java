package com.example.lectia.database; // Asegúrate que el paquete es el correcto

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "usuarios")
public class Usuario {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String nombre;
    private String email;
    private String password;
    // Si tienes un campo para el ID de Google, por ejemplo:
    // private String googleId;

    // --- CONSTRUCTOR VACÍO (necesario para Room) ---
    public Usuario() {
    }

    // --- CONSTRUCTOR PARA USAR NOSOTROS (Room lo ignora) ---
    // Este constructor debe tener TODOS tus campos
    @Ignore
    public Usuario(int id, String nombre, String email) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.password = password;
        // Si añadiste googleId: this.googleId = googleId;
    }

    // --- GETTERS Y SETTERS PARA TODOS LOS CAMPOS ---

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }}