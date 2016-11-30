package com.thu.web;

public class User {

    private final long id;
    private final String role;
    private final String resp_person;
    private final String fixed_phone;
    private final String mobile_phone;

    public User(long id, String role, String resp_person, String fixed_phone, String mobile_phone) {
        this.id = id;
        this.role = role;
        this.resp_person = resp_person;
        this.fixed_phone = fixed_phone;
        this.mobile_phone = mobile_phone;
    }

    public long getId() {
        return id;
    }

}
