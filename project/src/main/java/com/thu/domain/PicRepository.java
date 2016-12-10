package com.thu.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.Repository;

/**
 * Created by JasonLee on 16/12/4.
 */
public interface PicRepository extends JpaRepository<Pic, Long> {

}
