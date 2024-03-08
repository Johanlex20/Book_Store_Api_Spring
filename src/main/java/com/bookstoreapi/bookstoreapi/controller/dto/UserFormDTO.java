package com.bookstoreapi.bookstoreapi.controller.dto;

import com.bookstoreapi.bookstoreapi.domain.User;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NonNull;
import org.aspectj.bridge.IMessage;

@Data
public class UserFormDTO {

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
    @Pattern(regexp = "[a-z0-9-]+", message = "Contraseña debe tener un formato Valido")
    private String password;

    @NotNull(message = "Role no puede ser nulo")
    private User.Role role;

    public String getFullName() {
        return firstName + " " + lastName;
    }

}
