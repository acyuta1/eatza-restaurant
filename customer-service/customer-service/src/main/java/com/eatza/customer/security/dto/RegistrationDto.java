package com.eatza.customer.security.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationDto {

    @NotBlank(message = "username.is.empty")
    @Size(min = 5, max = 16)
    private String username;

    @NotBlank(message = "email.is.empty")
    @Email(message = "email.is.invalid")
    private String email;

    @NotBlank(message = "password.is.empty")
    @Size(min=4, max = 36)
    private String password;

    @NotBlank(message = "first.name.is.empty")
    private String firstName;

    @NotBlank(message = "last.name.is.empty")
    private String lastName;
}
