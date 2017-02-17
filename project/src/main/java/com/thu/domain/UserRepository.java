package com.thu.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import java.util.List;

/**
 * Created by JasonLee on 16/12/1.
 */
public interface UserRepository extends JpaRepository<TUser, Long>, QueryDslPredicateExecutor<TUser> {
//    TUser findByName(String name);
//    TUser findByNameAndAge(String name, Integer age);
//    @Query("select u from TUser u where u.mobileNumber=:number")
//    TUser findUser(@Param("number") String number);
//    Boolean existsByUname(String uname)
    TUser findByUname(String name);
    TUser findById(Long id);
    List<TUser> findByRole(Role role);
    long countByUname(String name);
    List<TUser> findAll();

    //add by luyq
    TUser findByIdNumber(String idNumber);
}
