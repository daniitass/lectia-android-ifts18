package com.example.lectia.database;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "clubes")
public class Club {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String nombre;
    private String descripcion;
    private String imagenPath;

    //AÑADE UN CONSTRUCTOR VACÍO ---
    // Room usará este constructor para crear los objetos.
    public Club() {
    }

    //AÑADE @IGNORE AL CONSTRUCTOR ACTUAL ---
    // Esto le dice a Room que ignore este constructor. Lo podemos
    // usar nosotros en otras partes de la app si queremos.
    @Ignore
    public Club(int id, String nombre, String descripcion, String imagenPath) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.imagenPath = imagenPath;
    }

    // Getters
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public String getImagenPath() { return imagenPath; }

    // Setters (Room los necesita para el constructor vacío)
    public void setId(int id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public void setImagenPath(String imagenPath) { this.imagenPath = imagenPath; }
}