package com.bookStoreApiSpring.bookStoreApiSpring.models;
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
    private String slug;

    @Column(name = "description")
    private String desc;

    private Float price;

    @Column(name = "cover_path")
    private String coverPath;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
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
