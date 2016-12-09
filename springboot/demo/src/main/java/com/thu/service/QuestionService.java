package com.thu.service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.thu.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by JasonLee on 16/12/5.
 */
@Service
public class QuestionService {
    @Autowired
    private QuestionRepository questionRepository;

    public List<Question> getAllQuestions() {
        List<Sort.Order> orders = new ArrayList<Sort.Order>();
        orders.add(new Sort.Order(Sort.Direction.DESC, "isCommonTop"));
        orders.add(new Sort.Order(Sort.Direction.DESC, "isCommon"));
        orders.add(new Sort.Order(Sort.Direction.DESC, "createdTime"));
        Sort sort = new Sort(orders);
        return questionRepository.findAll(sort);
    }

    public List<Question> getAllQuestionsForRole(Role role) {
        List<Sort.Order> orders = new ArrayList<Sort.Order>();
        orders.add(new Sort.Order(Sort.Direction.DESC, "isCommonTop"));
        orders.add(new Sort.Order(Sort.Direction.DESC, "isCommon"));
        orders.add(new Sort.Order(Sort.Direction.DESC, "createdTime"));
        Sort sort = new Sort(orders);
        return questionRepository.findByTransferRole(role, sort);
    }

    public Question getQuestionDetail(Long id) {
        Question question = questionRepository.findByQuestionId(id);
        if (question == null) {
            return null;
        }
        question.setRead(true);
        return questionRepository.save(question);
    }

    @Transactional
    public boolean responsibleDeptRespond(Long id, Response response) {
        Question question = questionRepository.findByQuestionId(id);
        if (question == null) {
            return false;
        }
        try {
            question.addResponse(response);
            question.setStatus(Status.SOLVED);
            questionRepository.save(question);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean responsibleDeptReject(Long id, String rejectReason) {
        Question question = questionRepository.findByQuestionId(id);
        if (question == null) {
            return false;
        }
        question.setStatus(Status.UNAPPROVED);
        question.setRejectReason(rejectReason);
        try {
            questionRepository.save(question);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean transferQuestion(Long id, Role to) {
        Question question = questionRepository.findByQuestionId(id);
        if (question == null) {
            return false;
        }
        question.setStatus(Status.UNSOLVED);
        question.setTransferRole(to);
        question.setRead(false);
        try {
            questionRepository.save(question);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Transactional
    public boolean classifyQuestion(Long id, Role leaderRole, List<Role> otherRoles, Date ddl, String instruction) {
        Question question = questionRepository.findByQuestionId(id);
        if (question == null) {
            return false;
        }
        question.setLeaderRole(leaderRole);
        question.setOtherRoles(otherRoles);
        try {
            questionRepository.save(question);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public List<Question> getQuestionForRelatedRole(Role role) {
        return questionRepository.findByLeaderRoleOrOtherRolesContains(role, role);
    }

    public boolean applyReclassifyQuestion(Long id, String reclassifyReason, Role transferRole) {
        Question question = questionRepository.findByQuestionId(id);
        if (question == null) {
            return false;
        }
        question.setStatus(Status.RECLASSIFY);
        question.setReclassifyReason(reclassifyReason);
        question.setTransferRole(transferRole);
        question.setRead(false);
        try {
            questionRepository.save(question);
            return true;
        } catch (Exception e) {
           return false;
        }
    }

    public boolean hasLearderRole(Long questionId) {
        Question question = questionRepository.findByQuestionId(questionId);
        return question != null && question.getLeaderRole() != null;
    }

    public boolean setDelay(Long questionId, String delayReason, Integer delayDays) {
        Question question = questionRepository.findByQuestionId(questionId);
        if (question == null) {
            return false;
        }
        question.setDelayReason(delayReason);
        question.setDelayDays(delayDays);
        try {
            questionRepository.save(question);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean setCommon(Long questionId, boolean isCommon) {
        Question question = questionRepository.findByQuestionId(questionId);
        if (question == null) {
            return false;
        }
        question.setCommon(isCommon);
        try {
            questionRepository.save(question);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean setTop(Long questionId, boolean isTop) {
        Question question = questionRepository.findByQuestionId(questionId);
        if (question == null) {
            return false;
        }
        question.setCommonTop(isTop);
        try {
            questionRepository.save(question);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Page<Question> findByState(Integer pageNum, Integer pageSize, Status state, String orderType, String searchKey, boolean isCommon)
    {
        // TODO: orderType validation:
        QQuestion question = QQuestion.question;
        BooleanBuilder booleanBuilder = new BooleanBuilder(question.status.eq(state));

        if (isCommon) {
            booleanBuilder.and(question.isCommon.eq(Boolean.TRUE));
//            predicate = question.status.eq(state).and();
        }

        if (!searchKey.isEmpty()) {
            Predicate searchPredict = question.title.contains(searchKey).or(question.content.contains(searchKey));
            booleanBuilder.and(searchPredict);
        }
        Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, orderType));
        Pageable pageable = new PageRequest(pageNum, pageSize, sort);
        return questionRepository.findAll(booleanBuilder.getValue(), pageable);
    }

    List<Question> findByDepart(Integer pageNum, Integer pageSize, String Depart, String orderType, String searchKey, boolean isCommon)
    {
        //判断是否是QAA
        // 按牵头部分查询，按orderType排序{提问时间 或 附议数量}，在问题内容与标题内匹配searchKey 允许空
        return  null;
    }

    List<Question> findMyQuestions(Integer pageNum, Integer pageSize, Long userId, String orderType, String searchKey, boolean isCommon)
    {
        //判断是否是QAA
        // 查询当前用户所有提问，按orderType排序{提问时间 或 附议数量}，在问题内容与标题内匹配searchKey 允许空
        return  null;
    }

    List<Question> findAll(Integer pageNum, Integer pageSize, String orderType, String searchKey, boolean isCommon)
    {
        // 判断是否是QAA
        // 查询所有问题，按orderType排序{提问时间 或 附议数量}，在问题内容与标题内匹配searchKey 允许空
        return  null;
    }


    Question findById(Long questionId)
    {
        // 得到问题
        return null;
    }

//    public List<Question> lala() {
//        return questionRepository.lala();
//    }

//    protected QuestionService() {}
}
