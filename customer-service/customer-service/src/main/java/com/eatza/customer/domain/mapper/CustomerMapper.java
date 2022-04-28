package com.eatza.customer.domain.mapper;

import com.eatza.customer.domain.dto.CustomerUpdateDto;
import com.eatza.customer.domain.model.Customer;
import com.eatza.customer.domain.model.Role;
import com.eatza.customer.security.dto.RegistrationDto;
import com.eatza.shared.dto.customers.CustomerDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper
public abstract class CustomerMapper {

    @Mapping(target = "roles", qualifiedByName = "roleConvertToDto")
    public abstract CustomerDto toDto(Customer customer);

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "token", ignore = true)
    public abstract Customer fromDto(CustomerDto customerDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "isNonLocked", ignore = true)
    @Mapping(target = "token", ignore = true)
    @Mapping(target = "walletAmount", ignore = true)
    public abstract Customer fromRegistrationDto(RegistrationDto registrationDto);

    @Named("roleConvertToDto")
    public Set<String> roleConvertToDto(Set<Role> roles) {
        return roles.stream().map(Role::getRoleType).map(Objects::toString).collect(Collectors.toSet());
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "isNonLocked", ignore = true)
    @Mapping(target = "token", ignore = true)
    @Mapping(target = "walletAmount", ignore = true)
    public abstract Customer updateCustomer(CustomerUpdateDto customerUpdateDto);
}