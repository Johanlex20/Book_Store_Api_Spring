package com.bookstoreapi.bookstoreapi.web.dto;


import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NonNull;

@Data
public class BookFormDto {

    @NonNull
    @Size(min = 3, max = 100)
    private String title;

    @NonNull
    @Min(0)
    private Float price;

    @NonNull
    @Pattern(regexp = "[a-z0-9-]+", message = "debe tener un formato valido con carcteres")
    private String slug;

    @NotEmpty(message = "Descripcion no debe estar vacia.")
    private String desc;

    @NotEmpty
    private String coverPath;

    @NotEmpty
    private String filePath;
}
