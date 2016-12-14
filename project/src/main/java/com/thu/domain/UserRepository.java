package com.thu.domain;

import com.querydsl.core.QueryFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by JasonLee on 16/12/1.
 */
public interface UserRepository extends JpaRepository<User, Long>, QueryDslPredicateExecutor<User> {
//    User findByName(String name);
//    User findByNameAndAge(String name, Integer age);
//    @Query("select u from User u where u.mobileNumber=:number")
//    User findUser(@Param("number") String number);
//    Boolean existsByUname(String uname)
    User findByUname(String name);
    User findById(Long id);
    List<User> findByRole(Role role);
    long countByUname(String name);
    List<User> findAll();

}
