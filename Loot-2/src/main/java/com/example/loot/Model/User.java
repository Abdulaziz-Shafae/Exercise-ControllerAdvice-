package com.example.loot.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank( message = "The name cant be blank")
    @NotEmpty( message = "The name cant be empty")
    @Size(max = 100, message = "The name must not exceed 100 characters")
    @Pattern(
            regexp = "^[A-Za-z ]+$",
            message = "The name must contain only letters and spaces"
    )
    @Column(columnDefinition = "VARCHAR(100) not null")
    private String name;

    @NotBlank( message = "The email cant be blank")
    @NotEmpty( message = "The email cant be empty")
    @Email
    @Size(max = 150, message = "The email must not exceed 150 characters")
    @Column(columnDefinition = "VARCHAR(150) not null unique")
    private String email;

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
    @Column(columnDefinition = "varchar(255) not null")
    private String password;

    @NotNull(message = "The phone number cant be empty")    @Pattern(
            regexp = "^05\\d{8}$",
            message = "Phone number must be a valid Saudi phone number"
    )
    @Column(columnDefinition = "varchar(10) not null")    private String phoneNumber;
}
