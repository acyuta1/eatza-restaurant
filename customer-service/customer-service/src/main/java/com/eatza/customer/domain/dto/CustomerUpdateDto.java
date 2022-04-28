package com.eatza.customer.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class CustomerUpdateDto {

    @NotBlank(message = "username.is.empty")
    @Size(min = 5, max = 16)
    private String username;

    @NotBlank(message = "email.is.empty")
    @Email(message = "email.is.invalid")
    private String email;

    @NotBlank(message = "first.name.is.empty")
    private String firstName;

    @NotBlank(message = "last.name.is.empty")
    private String lastName;
}
