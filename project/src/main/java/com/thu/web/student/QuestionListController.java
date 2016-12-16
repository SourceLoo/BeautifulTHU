package com.thu.web.student;

import com.thu.domain.*;
import com.thu.service.QuestionService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by source on 12/6/16.
 */

@Controller
public class QuestionListController {

    // 得到当前会话
    @Autowired
    HttpSession session;

    @Autowired
    private QuestionService questionService;

    @GetMapping(value = "/question/all/")
    @ResponseBody
    public Object getQuestions(@RequestParam(name = "page_num") Integer pageNum,
                               @RequestParam(name = "state_condition", required = false) String state,
                               @RequestParam(name = "depart_condition", required = false) String depart,
                               @RequestParam(name = "order_type" , required = false, defaultValue = "createdTime") String orderType,
                               @RequestParam(name = "keywords" , required = false) String searchKey,
                               @RequestParam(name = "isCommon" , required = false, defaultValue = "false") boolean isCommon)
    {
        // pageSize 由后台自定义
        Integer pageSize = 2;

        System.out.println("state="+state);
        // Status: UNAPPROVED, UNCLASSIFIED, UNSOLVED, SOLVING, RECLASSIFY, DELAY, SOLVED, INVALID

        System.out.println("depart="+depart);
        // Role.role

        System.out.println("orderType="+orderType);
        // createdTime likes 创建时间即创建的ID
        List<String> orders = new ArrayList<>();
        if("likes".equals(orderType))
        {
            orders.add(orderType);
        }
        orders.add("questionId");


        System.out.println("searchKey="+searchKey);
        // all

        System.out.println("isCommon="+isCommon);

        searchKey = "内容";
        isCommon = true;
        depart = "zongban";
        Status status = Status.UNCLASSIFIED;
        Long userId = (Long) session.getAttribute("userId");
        userId = 0L;


        Page<Question> questionPage = null;
        // 注意 缺失值 则设置为null
//        status = null;
//        depart = null;
//        searchKey = null;
        isCommon = false;

        questionPage = questionService.filterQuestions(pageNum, pageSize, status, depart, searchKey, isCommon, userId, orders);

        List<Question> questions = questionPage.getContent();



        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        jsonObject.put("question_list", jsonArray);

        for (Question question : questions)
        {
            JSONObject tmp = new JSONObject();
            tmp.put("question_id", question.getQuestionId());
            tmp.put("question_title", question.getTitle());
            tmp.put("question_content", question.getContent());
            tmp.put("question_location", question.getCreatedLocation());
            tmp.put("like_num", question.getLikes());

            // 问题作者，问题创建时间
            tmp.put("author", question.getUser().getUname());
            tmp.put("createdTime", question.getCreatedTime().toLocalDate());

            jsonArray.put(tmp);
        }

        System.out.println(jsonObject.toString());

        return jsonObject.toString();
    }
}
