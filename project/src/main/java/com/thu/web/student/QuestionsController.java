package com.thu.web.student;

import com.thu.domain.Question;
import com.thu.domain.QuestionRepository;
import com.thu.domain.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by source on 12/6/16.
 */

@RestController
@RequestMapping(value = "/question")
public class QuestionsController {


    // 得到当前会话
    @Autowired
    HttpSession session;

    @Autowired
    private QuestionRepository questionRepository;

    @GetMapping(value = "/")
    public Object getAQuestion(@RequestParam("question_id") Long questionId)
    {
        // sql: 按questionId 得到question对象


        System.out.println(questionId);
        Question q = new Question("这是标题", "这是内容");
        //q.setQuestionId(questionId);

//        Role role = new Role();
//        role.setDisplayName("环保部门");
//        q.setLeaderRole(role);
        //q.setResponses();

        return q;
    }

    @GetMapping(value = "/all/")
    public Object getQuestions(@RequestParam("page_num") Integer pageNum,
                               @RequestParam("state_condition") String state,
                               @RequestParam("depart_condition") String depart,
                               @RequestParam("order_type") String orderType,
                               @RequestParam("keywords") String searchKey)
    {
        // pageSize 由后台自定义
        Integer pageSize = 1;
        Integer userId = (Integer)session.getAttribute("userId");

        /* sql:

                获得第pageNum页的10个问题列表
         */

        //System.out.println(pageNum + '\t' + filterType + '\t' + orderType);

        Page<Question> questionPage = questionRepository.findAll(new PageRequest(pageNum, pageSize));

        //List<Question> questions = questionRepository.findAll();

        List<Question> questions = questionPage.getContent();
        System.out.println("begin");
        for (Question question : questions)
        {
            System.out.println(question);
            question.setLeaderRole(null);
            question.setOtherRoles(null);
            //question.setPics(null);
            question.setUser(null);
        }
        System.out.println("done");

        //List<Question> questions = new ArrayList<>();
        //questions.add(new Question("标题1", "标题1"));
        //questions.add(new Question("标题2", "内容2"));

        return questions;
    }
}
