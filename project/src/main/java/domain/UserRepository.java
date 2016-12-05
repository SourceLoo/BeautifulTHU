package domain;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;

/**
 * Created by JasonLee on 16/12/1.
 */
@Repository
@Table(name="User")
@Qualifier("UserRepository")
public interface UserRepository extends JpaRepository<User, Long>, QueryDslPredicateExecutor<User> {
//    User findByName(String name);
//    public User findByNameAndAge(String name, Integer age);
    @Query("select u from User u where u.uname=:name")
            User findUser(@Param("name") String name);
}
