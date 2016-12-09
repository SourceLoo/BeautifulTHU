package com.thu.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.util.Date;
import java.util.stream.Stream;

/**
 * Created by JasonLee on 16/12/1.
 */
@Entity
public class User {
    static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false, unique = true)
    private String uname;
    @Column(nullable = false)
    private String passwd;
    @ManyToOne(optional = false)
    private Role role;
    @Column(nullable = false)
    private String mobileNumber;
    private String fixedNumber;
    private String email;
    @Column(nullable = false)
    private String idNumber;

    protected User () {}

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwordEncoder.encode(passwd);
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getFixedNumber() {
        return fixedNumber;
    }

    public void setFixedNumber(String fixedNumber) {
        this.fixedNumber = fixedNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public User(String uname, String passwd, Role role, String mobileNumber, String fixedNumber, String email, String idNumber) {
        this.uname = uname;
        setPasswd(passwd);
        this.role = role;
        this.mobileNumber = mobileNumber;
        this.fixedNumber = fixedNumber;
        this.email = email;
        this.idNumber = idNumber;
    }

//    public User(String uname, String passwd, String mobileNumber, String fixedNumber, String email, String idNumber) {
//        PasswordEncoder passwordEncoder;
//
//        this.uname = uname;
//        this.passwd = passwd;
////        this.role = role;
//        this.mobileNumber = mobileNumber;
//        this.fixedNumber = fixedNumber;
//        this.email = email;
//        this.idNumber = idNumber;
//    }

}

