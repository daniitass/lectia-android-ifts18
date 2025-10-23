package com.example.lectia.api;

import com.example.lectia.model.BookResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GoogleBooksService {

    // Método para la búsqueda INICIAL (con todos los filtros)
    @GET("volumes")
    Call<BookResponse> getNewestBooks(
            @Query("q") String query,
            @Query("orderBy") String orderBy,
            @Query("langRestrict") String lang,
            @Query("key") String apiKey
    );

    // Método para la búsqueda del USUARIO (sin orderBy y sin langRestrict)
    @GET("volumes")
    Call<BookResponse> searchBooks(
            @Query("q") String query,
            @Query("key") String apiKey
    );
}



