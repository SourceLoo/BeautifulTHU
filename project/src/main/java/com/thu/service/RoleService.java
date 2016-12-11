package com.thu.service;

import com.thu.domain.Role;
import com.thu.domain.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by JasonLee on 16/12/6.
 */
@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    public Role findByRole(String role) {
        return roleRepository.findByRole(role);
    }

    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    public boolean updateRole(String role, String respPerson) {
        Role r = roleRepository.findByRole(role);
        if (r == null) {
            return false;
        }
        r.setRespPerson(respPerson);
        try {
            roleRepository.save(r);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
