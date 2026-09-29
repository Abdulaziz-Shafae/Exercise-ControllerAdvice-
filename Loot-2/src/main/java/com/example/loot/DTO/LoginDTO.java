package com.example.loot.DTO;

import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginDTO {

    @NotBlank( message = "The email cant be blank")
    @NotEmpty(message = "Email cannot be empty")
    @Email(message = "Email must be valid")
    @Size(max = 150, message = "The email must not exceed 150 characters")
    private String email;

    @NotBlank( message = "The password cant be blank")
    @NotEmpty(message = "Password cannot be empty")
    @Size(min = 8, message = "Password must be at least 8 characters")
    @Pattern(
            regexp = ".*[A-Z].*",
            message = "Password must contain at least one uppercase letter"
    )
    @Pattern(
            regexp = ".*[a-z].*",
            message = "Password must contain at least one lowercase letter"
    )
    @Pattern(
            regexp = "^(?:\\D*\\d){2}.*$",
            message = "Password must contain at least two digits"
    )
    @Pattern(
            regexp = ".*[!@#$%].*",
            message = "Password must contain at least one special character (!@#$%)"
    )
    private String password;
}