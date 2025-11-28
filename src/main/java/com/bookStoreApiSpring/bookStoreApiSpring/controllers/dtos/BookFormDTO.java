package com.bookStoreApiSpring.bookStoreApiSpring.controllers.dtos;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class BookFormDTO {

    @NotBlank
    @Size(min = 3, message = "Titulo debe tener almenos 3 caracteres!")
    @Size(max = 50, message = "Titulo puede tener un maximo de 50 caracteres@!")
    private String title;

    @NotBlank
    @Pattern(regexp = "[A-Za-z0-9-]+",message = "El slug debe tener un formato válido  : [letras:a ala z numeros: 0 al 9 signos: -+ ]")
    private String slug;

    @NotBlank
    @Size(min = 3, message = "Descripción debe tener almenos 3 caracteres !")
    @Size(max = 100, message = "Descripción puede tener un maximo de 100 caracteres !")
    private String desc;

    @NotNull
    @Min(0)
    private Float price;

    @NotBlank
    private String coverPath;

    @NotBlank
    private String filePath;

}
