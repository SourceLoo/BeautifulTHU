package com.thu.service;

import com.querydsl.core.BooleanBuilder;
import com.thu.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.Instant;
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
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleService roleService;

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

            // modified by luyq 第一次回复 设置为解决中
            if(question.getResponses().size() == 1)
            {
                question.setStatus(Status.SOLVING);
            }

            questionRepository.save(question);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean responsibleDeptReject(Long id, String rejectReason) {
        Question question = questionRepository.findByQuestionId(id);
        if (question == null) {
            return false;
        }

        // luyq
        question.setStatus(Status.INVALID);
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
        question.setStatus(Status.UNCLASSIFIED);
        question.setTransferRole(to);
        question.setRead(false);
        try {
            questionRepository.save(question);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    //ReclassifyQuestion
    public boolean ReclassifyQuestion(Long id,boolean agree,Role xiaoban){
        Question question=questionRepository.findByQuestionId(id);
        if(question==null)
            return false;
        if(!agree){
            question.setStatus(Status.UNSOLVED);
            question.setTransferRole(null);
        }else{
            question.setStatus(Status.UNCLASSIFIED);
            question.setLeaderRole(null);
            question.setOtherRoles(null);
            question.setTransferRole(xiaoban);

        }
        try {
            questionRepository.save(question);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    //DelayQuestion
    public boolean DelayQuestion(Long id,boolean agree){
        Question question=questionRepository.findByQuestionId(id);
        if(question==null)
            return false;


        try {
            if(agree){
                int delay_days=question.getDelayDays();
                if(delay_days<=0)
                    return false;
                question.setDdl(question.getDdl().plusDays(delay_days));
            }
            question.setStatus(Status.UNSOLVED);
            question.setTransferRole(null);
            questionRepository.save(question);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Transactional
    public boolean classifyQuestion(Long id, Role leaderRole, List<Role> otherRoles, LocalDateTime ddl, String instruction) {
        Question question = questionRepository.findByQuestionId(id);
        if (question == null) {
            return false;
        }
        question.setLeaderRole(leaderRole);
        question.setOtherRoles(otherRoles);
        question.setTransferRole(null);
        question.setStatus(Status.UNSOLVED);
        question.setDdl(ddl);
        question.setInstruction(instruction);
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

    public boolean setDelay(Long questionId, String delayReason, Integer delayDays,Role tuan) {
        Question question = questionRepository.findByQuestionId(questionId);
        if (question == null) {
            return false;
        }
        question.setDelayReason(delayReason);
        question.setDelayDays(delayDays);
        question.setTransferRole(tuan);
        question.setStatus(Status.DELAY);
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


    private Pageable makeBuilderAndPageable(BooleanBuilder booleanBuilder, String searchKey, boolean isCommon, int pageNum, int pageSize, List<String> orders) {
        QQuestion question = QQuestion.question;
        if (isCommon) {
            booleanBuilder.and(question.isCommon.eq(Boolean.TRUE));
        }
        if (!searchKey.isEmpty()) {
            booleanBuilder.and(question.title.contains(searchKey).or(question.content.contains(searchKey)));
        }
        // TODO: 16/12/10 orders validation
        //Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, orders)); modified by luyq
        Sort sort = new Sort(Sort.Direction.DESC, orders);
        System.out.println(orders);
        return new PageRequest(pageNum, pageSize, sort);
    }

    // add by luyq
    public Page<Question> filterQuestions(Integer pageNum, Integer pageSize, List<Status> statuses, String depart, String searchKey, boolean isCommon, List<String> orders)
    {
        QQuestion question = QQuestion.question;
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if(statuses != null){
            for (Status status: statuses)
            {
                booleanBuilder.or(question.status.eq(status));
            }
        }
        if(depart != null){
            booleanBuilder.and(question.leaderRole.displayName.eq(depart));
        }
        if (searchKey != null) {
            booleanBuilder.and(question.title.contains(searchKey).or(question.content.contains(searchKey)));
        }
        if (isCommon) {
            booleanBuilder.and(question.isCommon.eq(Boolean.TRUE));
        }
        System.out.println(booleanBuilder);
        System.out.println(pageNum);
        System.out.println(pageSize);

        // orders 定不为空
        Sort sort = new Sort(Sort.Direction.DESC, orders);
        // System.out.println(orders);
        Pageable pageable = new PageRequest(pageNum, pageSize, sort);

        return questionRepository.findAll(booleanBuilder.getValue(), pageable);
    }

    // add by luyq
    public Page<Question> findMyQuestions(Integer pageNum, Integer pageSize, Long userId, List<String> orders)
    {
        QQuestion question = QQuestion.question;
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        booleanBuilder.and(question.TUser.id.eq(userId));

        System.out.println(booleanBuilder);
        System.out.println(pageNum);
        System.out.println(pageSize);

        // orders 定不为空
        Sort sort = new Sort(Sort.Direction.DESC, orders);
        Pageable pageable = new PageRequest(pageNum, pageSize, sort);
        return questionRepository.findAll(booleanBuilder.getValue(), pageable);
    }

    public Page<Question> findByState(Integer pageNum, Integer pageSize, Status state, List<String> orders, String searchKey, boolean isCommon)
    {
        QQuestion question = QQuestion.question;
        BooleanBuilder booleanBuilder = new BooleanBuilder(question.status.eq(state));
        Pageable pageable = makeBuilderAndPageable(booleanBuilder, searchKey, isCommon, pageNum, pageSize, orders);
        return questionRepository.findAll(booleanBuilder.getValue(), pageable);
    }

    public Page<Question> findByDepart(Integer pageNum, Integer pageSize, String Depart, List<String> orders, String searchKey, boolean isCommon)
    {
        QQuestion question = QQuestion.question;
        BooleanBuilder booleanBuilder = new BooleanBuilder(question.leaderRole.role.eq(Depart));
        Pageable pageable = makeBuilderAndPageable(booleanBuilder, searchKey, isCommon, pageNum, pageSize, orders);
        return questionRepository.findAll(booleanBuilder.getValue(), pageable);
    }

    public Page<Question> findAll(Integer pageNum, Integer pageSize, List<String> orders, String searchKey, boolean isCommon)
    {
        QQuestion question = QQuestion.question;
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        Pageable pageable = makeBuilderAndPageable(booleanBuilder, searchKey, isCommon, pageNum, pageSize, orders);
        return questionRepository.findAll(booleanBuilder.getValue(), pageable);
    }


    public Question findById(Long questionId)
    {
        return questionRepository.findByQuestionId(questionId);
    }

//    public List<Question> lala() {
//        return questionRepository.lala();
//    }

    @Transactional
    public boolean saveQuestion(TUser TUser, String title, String content, String location, List<String> paths) {
        Question question = new Question(title, content, TUser, location, paths);
        try {
            question.setTransferRole(roleService.findByRole("xiaoban"));
            questionRepository.save(question);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean saveStudentResponse(Long questionId, EvaluationType evaluationType, String evaluationDetail)
    {
        Question question = findById(questionId);
        if (question == null || question.getEvaluationType() != null) {
            return false;
        }

        // add by luyq
        question.setStatus(Status.SOLVED);

        question.setEvaluationType(evaluationType);
        question.setEvaluationDetail(evaluationDetail);
        try {
            questionRepository.save(question);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Transactional
    public boolean modifyQuestionLike(TUser TUser, Long questionId, boolean op)
    {
        Question question = findById(questionId);
        if (question == null) {
            return false;
        }
        if (op) {
            if (TUser.getLikedQuestions().contains(question)) {
                return false;
            }
            question.incrementLikes();
            TUser.getLikedQuestions().add(question);
        }
        else {
            if (!TUser.getLikedQuestions().contains(question)) {
                return false;
            }
            question.decrementLikes();
            TUser.getLikedQuestions().remove(question);
        }
        try {
            questionRepository.save(question);
            userRepository.save(TUser);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // add by luyq 当op为真，增加user未读的question
    @Transactional
    public boolean modifyUnreadQuestions(Long questionId, boolean op)
    {
        Question question = findById(questionId);
        if (question == null) {
            return false;
        }
        TUser TUser = question.getTUser();
        if (op) {
            if (TUser.getUnreadQuestions().contains(question)) {
                return false;
            }
            TUser.getUnreadQuestions().add(question);
        }
        else {
            if (!TUser.getUnreadQuestions().contains(question)) {
                return false;
            }
            TUser.getUnreadQuestions().remove(question);
        }
        try {
            userRepository.save(TUser);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    //@Transactional
    //public boolean mergeDelay(Long questionId) {
        //Question question = findById(questionId);
        //if (question == null || question.getDdl() == null || question.getDelayDays() == null) {
            //return false;
        //}
        //question.setDdl(question.getDdl().plusDays(question.getDelayDays()));
        //question.setDelayDays(0);
        //try {
            //questionRepository.save(question);
            //return true;
        //} catch (Exception e) {
            //return false;
        //}
    //}

    @Transactional
    public boolean updateTimestamp(Long questionId, LocalDateTime timestamp1, LocalDateTime timestamp2, LocalDateTime timestamp3) {
        Question question = findById(questionId);
        if (question == null) {
            return false;
        }
        if (timestamp1 != null) {
            question.setTimestamp1(timestamp1);
        }
        if (timestamp2 != null) {
            question.setTimestamp2(timestamp2);
        }
        if (timestamp3 != null) {
            question.setTimestamp3(timestamp3);
        }
        try {
            questionRepository.save(question);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}

