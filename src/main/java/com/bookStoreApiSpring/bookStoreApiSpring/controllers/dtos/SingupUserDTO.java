package com.bookStoreApiSpring.bookStoreApiSpring.controllers.dtos;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SingupUserDTO {

    @NotEmpty(message="Nombre no debe estar vacio")
    @Size(min = 3, message = "Nombre debe tener minimo 3 caracteres" )
    @Size(max = 25, message = "Nombre debe tener maximo 25 caracteres")
    private String firstName;

    @NotEmpty(message = "Apellido no debe estar vacio")
    @Size(min = 3, message = "Apellido debe tener minimo 3 caracteres")
    @Size(max = 25, message = "Apellido debe tener maximo 25 caracteres")
    private String lastName;

    @Email(message = "Email debe tener un formato valido")
    @NotEmpty(message = "Email no debe estar vacio")
    private String email;

    @NotEmpty(message = "Contraseña no debe estar vacia")
    @Size(min = 4)
    private String password;
}
