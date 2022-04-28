package com.eatza.customer.domain.converters;

import com.eatza.customer.domain.model.Role;
import com.eatza.customer.domain.service.RoleRepository;
import com.eatza.shared.dto.customers.RoleType;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
@RequiredArgsConstructor
public class RoleConverter implements Converter<RoleType, Role> {

    private final RoleRepository roleRepository;

    @Override
    public Role convert(RoleType roleType) {
        return roleRepository.findByRoleType(roleType)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "role not found"));
    }
}
