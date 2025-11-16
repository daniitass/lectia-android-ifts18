// Archivo: model/ImageLinks.java
package com.example.lectia.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ImageLinks implements Serializable {

    // Nombres de variables que coinciden con el JSON de la API
    @SerializedName("thumbnail")
    private String thumbnail;

    @SerializedName("smallThumbnail")
    private String smallThumbnail;

    // --- Getters ---
    public String getThumbnail() {
        return thumbnail;
    }

    public String getSmallThumbnail() {
        return smallThumbnail;
    }
}

