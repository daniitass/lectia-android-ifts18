// Ruta: app/src/main/java/com/example/lectia/model/BookResponse.java
package com.example.lectia.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

// Esta clase representa la respuesta completa de la API de Google Books.
public class BookResponse {

    // ✅ CORRECCIÓN: La lista debe ser de tu propia clase 'BookItem'
    @SerializedName("items")
    private List<BookItem> items;

    // ✅ CORRECCIÓN: El Getter ahora es consistente con la variable
    public List<BookItem> getItems() {
        return items;
    }

    // ✅ CORRECCIÓN: El Setter también usa tu propia clase 'BookItem'
    public void setItems(List<BookItem> items) {
        this.items = items;
    }
}

