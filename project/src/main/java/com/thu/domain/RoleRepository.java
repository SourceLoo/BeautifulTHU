package com.thu.domain;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by JasonLee on 16/12/5.
 */
public interface RoleRepository extends JpaRepository<Role, String> {
    Role findByRole(String role);
    long countByRole(String role);
}
