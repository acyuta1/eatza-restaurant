package com.eatza.customer.domain.service;

import com.eatza.customer.domain.model.Role;
import com.eatza.shared.dto.customers.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    Optional<Role> findByRoleType(RoleType roleType);
}
