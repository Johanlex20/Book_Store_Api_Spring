package com.bookstoreapi.bookstoreapi.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;

    private String slug; // identifique un solo recurso

    @Column(name = "description")
    private String desc;

    private Float price;

    private String coverPath;  // ruta donde se almacena la imagen

    private String filePath;    // ruta donde se almacena el pdf o archivos

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    private void prePersist(){
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    private void preUpdate(){
        updatedAt = LocalDateTime.now();
    }


}
