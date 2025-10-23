// Archivo: model/BookItem.java
package com.example.lectia.model;

import com.google.gson.annotations.SerializedName;

public class BookItem {

    @SerializedName("volumeInfo")
    private VolumeInfo volumeInfo;

    public VolumeInfo getVolumeInfo() {
        return volumeInfo;
    }
}

