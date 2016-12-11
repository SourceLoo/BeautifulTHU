package com.thu.domain;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by JasonLee on 16/12/5.
 */
public interface QuestionRepository extends JpaRepository<Question, Long>, QueryDslPredicateExecutor<Question> {
//    @Query("from Question q where q.transferRole = :role")
    List<Question> findByTransferRole(Role role, Sort sort);
//    @Query("from Question q order by q.isCommonTop desc, q.isCommon desc, q.createdTime desc")
//    List<Question> lala();
    Question findByQuestionId(Long id);
    List<Question> findByLeaderRoleOrOtherRolesContains(Role leaderRole, Role otherRole);
}
