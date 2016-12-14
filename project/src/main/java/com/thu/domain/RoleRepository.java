package com.thu.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by JasonLee on 16/12/5.
 */
public interface RoleRepository extends JpaRepository<Role, String> {
    Role findByRole(String role);
    long countByRole(String role);
    List<Role> findByByZongban(boolean byZongban);
}
