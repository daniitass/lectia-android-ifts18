// Archivo: model/VolumeInfo.java
package com.example.lectia.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class VolumeInfo {

    // Nombres de variables que coinciden con el JSON de la API
    @SerializedName("title")
    private String title;

    @SerializedName("authors")
    private List<String> authors;

    @SerializedName("imageLinks")
    private ImageLinks imageLinks;

    // --- Getters ---
    public String getTitle() {
        return title;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public ImageLinks getImageLinks() {
        return imageLinks;
    }
}
