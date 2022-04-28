package com.eatza.shared.dto.customers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDto {

    private Long id;

    private String username;

    private String email;

    private String firstName;

    private String lastName;

    private Set<String> roles = new HashSet<>();

    private Boolean isActive = false;

    private Boolean isNonLocked = true;

    private int walletAmount;
}
