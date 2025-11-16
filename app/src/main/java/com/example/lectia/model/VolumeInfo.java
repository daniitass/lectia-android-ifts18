
package com.example.lectia.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

public class VolumeInfo implements Serializable {

    @SerializedName("title")
    private String title;

    @SerializedName("authors")
    private List<String> authors;

    @SerializedName("imageLinks")
    private ImageLinks imageLinks;

    // ✅ PASO 1: AÑADE EL NUEVO ATRIBUTO PARA LA DESCRIPCIÓN
    @SerializedName("description")
    private String description;

    // --- Getters y Setters existentes ---
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public void setAuthors(List<String> authors) {
        this.authors = authors;
    }

    public ImageLinks getImageLinks() {
        return imageLinks;
    }

    public void setImageLinks(ImageLinks imageLinks) {
        this.imageLinks = imageLinks;
    }

    // ✅ PASO 2: AÑADE EL GETTER PARA EL NUEVO ATRIBUTO
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
