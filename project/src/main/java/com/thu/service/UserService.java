package com.thu.service;

import com.thu.domain.Role;
import com.thu.domain.User;
import com.thu.domain.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by JasonLee on 16/12/6.
 */
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User findUser(String name) {
        return userRepository.findByUname(name);
    }

    public List<User> findAll() {return userRepository.findAll();}

    public List<User> findUserByRole(Role role) {
        return userRepository.findByRole(role);
    }

    public boolean containsUname(String uname) {
        return userRepository.countByUname(uname) > 0;
    }

    public boolean insertUser(String uname, String mobileNumber, String fixedNumber, String idNumber, String email, Role role, String passwd) {
        if (userRepository.countByUname(uname) > 0) {
            return false;
        }
        User user = new User(uname, passwd, role, mobileNumber, fixedNumber, email, idNumber);
        try {
            userRepository.save(user);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean updateUser(String uname, String mobileNumber, String fixedNumber, Role role, String passwd) {
        User user = userRepository.findByUname(uname);
        if (user == null) {
            return false;
        }
        user.setMobileNumber(mobileNumber);
        user.setFixedNumber(fixedNumber);
        user.setRole(role);
        user.setPasswd(passwd);
        try {
            userRepository.save(user);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
