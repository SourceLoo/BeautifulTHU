package com.thu.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by JasonLee on 16/12/3.
 */
@Entity
public class Pic {
    @Id
    private Long id;
    private String path;
}
