package com.thu.web.student;

import com.thu.domain.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by source on 12/8/16.
 */
@RestController
@RequestMapping(value = "/regular_question")
public class QAAsController {


    // 得到当前会话
    @Autowired
    HttpSession session;

    @GetMapping(value = "/")
    public Object getAQuestion(@RequestParam("question_id") Long questionId)
    {
        // sql: 按questionId 得到question对象


        System.out.println(questionId);
        Question q = new Question("这是标题", "这是内容");
        //q.setQuestionId(questionId);

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
        Integer pageSize = 10;
        //Integer userId = (Integer)session.getAttribute("userId");

        /* sql:
                获得第pageNum页的10个问题列表
         */

        //System.out.println(pageNum + '\t' + filterType + '\t' + orderType);

        List<Question> questions = new ArrayList<>();
        questions.add(new Question("标题1", "标题1"));
        questions.add(new Question("标题2", "内容2"));

        return questions;
    }
}


