package com.thu.domain;

import javax.persistence.*;

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
    //@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "picGenerator")
    //@SequenceGenerator(name = "picGenerator", sequenceName = "picSequence", allocationSize=1)
    private Long id;
    private String path;

    public Long getId() {
        return id;
    }

    public String getPath() {
        return path;
    }
}
