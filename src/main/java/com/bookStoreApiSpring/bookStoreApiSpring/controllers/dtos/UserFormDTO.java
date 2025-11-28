package com.bookStoreApiSpring.bookStoreApiSpring.controllers.dtos;
import com.bookStoreApiSpring.bookStoreApiSpring.models.User;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UserFormDTO {

    @NotBlank
    @Size(min = 3, message = "Nombre debe tener minimo 3 caracteres" )
    @Size(max = 25, message = "Nombre debe tener maximo 25 caracteres")
    private String firstName;

    @NotBlank
    @Size(min = 3, message = "Apellido debe tener minimo 3 caracteres")
    @Size(max = 25, message = "Apellido debe tener maximo 25 caracteres")
    private String lastName;

    @NotNull
    @Email
    private String email;

    @NotNull
    @Pattern(regexp = "[a-z0-9-]+", message = "Contraseña debe tener un formato Valido")
    private String password;

    @NotNull
    private User.Role role;

    public String getFullName(){
        return firstName+" "+lastName;
    }
}
