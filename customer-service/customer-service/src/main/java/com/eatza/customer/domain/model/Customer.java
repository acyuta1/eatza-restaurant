package com.eatza.customer.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "customer")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "username.is.empty")
    @Size(min = 5, max = 16)
    private String username;

    @NotBlank(message = "email.is.empty")
    @Email(message = "email.is.invalid")
    private String email;

    @NotBlank(message = "password.is.empty")
    @JsonIgnore
    private String password;

    @NotBlank(message = "first.name.is.empty")
    private String firstName;

    @NotBlank(message = "last.name.is.empty")
    private String lastName;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "customer_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    private Boolean isActive = false;

    private Boolean isNonLocked = true;

    @JsonIgnore
    private String token;

    private Integer walletAmount = 0;
}
