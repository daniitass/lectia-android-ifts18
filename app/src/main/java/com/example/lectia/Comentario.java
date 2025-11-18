package com.example.lectia;

// Esta es una clase simple (POJO) para pasar datos al adaptador.
// No es una entidad de la base de datos.
public class Comentario {
    private String autor;
    private String contenido;
    private String imagenPath; // Nuevo campo para la imagen

    // Constructor actualizado
    public Comentario(String autor, String contenido, String imagenPath) {
        this.autor = autor;
        this.contenido = contenido;
        this.imagenPath = imagenPath;
    }

    // Getters y Setters
    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public String getImagenPath() {
        return imagenPath;
    }

    public void setImagenPath(String imagenPath) {
        this.imagenPath = imagenPath;
    }
}
