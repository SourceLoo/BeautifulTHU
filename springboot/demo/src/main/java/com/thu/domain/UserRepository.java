package com.thu.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;

/**
 * Created by JasonLee on 16/12/1.
 */
public interface UserRepository extends JpaRepository<User, Long>, QueryDslPredicateExecutor<User> {
//    User findByName(String name);
//    User findByNameAndAge(String name, Integer age);
//    @Query("select u from User u where u.name=:name")
//    User findUser(@Param("name") String name);
}
