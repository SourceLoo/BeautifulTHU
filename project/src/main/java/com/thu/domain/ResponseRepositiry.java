package com.thu.domain;

import org.springframework.data.jpa.repository.JpaRepository;
/**
 * Created by JasonLee on 16/12/6.
 */
public interface ResponseRepositiry extends JpaRepository<Response, Long> {
    Response findByResponseId(Long id);
}
