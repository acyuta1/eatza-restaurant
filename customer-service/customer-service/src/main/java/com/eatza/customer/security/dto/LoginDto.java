package com.eatza.customer.security.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto {

    @NotBlank(message = "username.is.empty")
    private String username;

    @NotBlank(message = "password.is.empty")
    private String password;

}
