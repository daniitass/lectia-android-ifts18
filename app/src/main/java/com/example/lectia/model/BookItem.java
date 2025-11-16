
package com.example.lectia.model;

import java.io.Serializable;

public class BookItem implements Serializable {
    private String id;
    private VolumeInfo volumeInfo;

    // Getters y Setters...
    public String getId() { return id; }
    public VolumeInfo getVolumeInfo() { return volumeInfo; }
}

