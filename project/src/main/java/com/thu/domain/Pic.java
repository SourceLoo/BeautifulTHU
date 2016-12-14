package com.thu.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by JasonLee on 16/12/3.
 */
@Entity
public class Pic {
    public Pic(String path) {
        this.path = path;
    }

    protected Pic() {}
    @Id
    @GeneratedValue
    private Long id;
    private String path;

    public Long getId() {
        return id;
    }

    public String getPath() {
        return path;
    }
}
