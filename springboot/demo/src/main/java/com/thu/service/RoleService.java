package com.thu.service;

import com.thu.domain.Role;
import com.thu.domain.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by JasonLee on 16/12/6.
 */
@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    private Role tuanwei;
    private Role xiaoban;
    private Role zongban;
    private Role student;

    @PostConstruct
    public void init() {
        tuanwei = roleRepository.findByRole("tuanwei");
        xiaoban = roleRepository.findByRole("xiaoban");
        zongban = roleRepository.findByRole("zongban");
        student = roleRepository.findByRole("student");
    }

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

    public List<Role> findMain() {
        List<Role> mainRoles = new ArrayList<Role>();
        mainRoles.add(tuanwei);
        mainRoles.add(xiaoban);
        mainRoles.add(zongban);
        return mainRoles;
    }

    public List<Role> findRelated() {
        List<Role> relatedRoles = roleRepository.findAll();
        relatedRoles.remove(tuanwei);
        relatedRoles.remove(xiaoban);
        relatedRoles.remove(zongban);
        relatedRoles.remove(student);
        return relatedRoles;
    }

    public List<Role> findFellowRole(Role role) {
        if (role.equals(xiaoban)) {
            return findRelated();
        }
        else if (role.equals(zongban)) {
            return roleRepository.findByByZongban(true);
        }
        else {
            return null;
        }
    }
}
