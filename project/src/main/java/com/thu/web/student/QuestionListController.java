package com.thu.web.student;

import com.thu.domain.*;
import com.thu.service.QuestionService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by source on 12/6/16.
 */

@Controller
@RequestMapping("/student")
public class QuestionListController {

    // 得到当前会话
    @Autowired
    HttpSession session;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private QuestionService questionService;

    @GetMapping(value = "/question/all")
    @ResponseBody
    public Object getQuestions(@RequestParam(name = "page_num", required = false, defaultValue = "0") Integer pageNum,
                               @RequestParam(name = "page_size", required = false, defaultValue = "4") Integer pageSize,
                               @RequestParam(name = "state_condition", required = false) String state,
                               @RequestParam(name = "depart_condition", required = false) String depart,
                               @RequestParam(name = "order_type" , required = false, defaultValue = "createdTime") String orderType,
                               @RequestParam(name = "keywords" , required = false) String searchKey,
                               @RequestParam(name = "isCommon" , required = false, defaultValue = "false") boolean isCommon)
    {
        //        status = null;
        //        depart = null;
        //        searchKey = null;
        List<Status> statuses = null;


        if("".equals(state) || "all".equals(state))
        {
            statuses = GetStatusAndDepartController.visibleStatus;
        }
        else
        {
            statuses = GetStatusAndDepartController.statusMap.get(state);
        }
        if("".equals(depart) || "all".equals(depart))
        {
            depart = null;
        }
        // createdTime likes 创建时间即创建的ID
        List<String> orders = new ArrayList<>();
        if("likenum".equals(orderType))
        {
            orders.add("likes");
        }
        orders.add("questionId");



        System.out.println("statuses="+statuses);
        // Status: UNAPPROVED, UNCLASSIFIED, UNSOLVED, SOLVING, RECLASSIFY, DELAY, SOLVED, INVALID

        System.out.println("depart="+depart);
        // Role.role

        System.out.println("orderType="+orders);

        System.out.println("searchKey="+searchKey);
        // all

        System.out.println("isCommon="+isCommon);



        Page<Question> questionPage = null;
        // 注意 缺失值 则设置为null


        questionPage = questionService.filterQuestions(pageNum, pageSize, statuses, depart, searchKey, isCommon, orders);

        List<Question> questions = questionPage.getContent();


        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        jsonObject.put("question_list", jsonArray);


        Long userId = (Long) session.getAttribute("userId");
        TUser TUser = userRepository.findById(userId);

        for (Question question : questions)
        {
            JSONObject tmp = new JSONObject();
            tmp.put("question_id", question.getQuestionId());
            tmp.put("question_title", question.getTitle());
            tmp.put("question_content", question.getContent());
            tmp.put("question_location", question.getCreatedLocation());
            tmp.put("like_num", question.getLikes());

            tmp.put("liked", TUser.getLikedQuestions().contains(question) ? 1 : 0);

            // 问题作者，问题创建时间
            tmp.put("author", question.getTUser().getUname());
            tmp.put("createdTime", question.getCreatedTime().toLocalDate());

            jsonArray.put(tmp);
        }

        System.out.println(jsonObject.toString());

        return jsonObject.toString();
    }


    @GetMapping("/home/status")
    @ResponseBody
    public String getHomeStatus()
    {

        JSONObject jsonObject = new JSONObject();
        Long userId = (Long) session.getAttribute("userId");
        TUser TUser = userRepository.findById(userId);
        if (TUser == null) {
            System.out.println("userId not found!");
        }

        jsonObject.put("unread", TUser.getUnreadQuestions().isEmpty() ? 0 : 1);
        System.out.println(jsonObject.toString());

        return jsonObject.toString();
    }
}
