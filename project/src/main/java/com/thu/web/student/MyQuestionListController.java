package com.thu.web.student;

import com.thu.domain.Question;
import com.thu.domain.Status;
import com.thu.domain.User;
import com.thu.domain.UserRepository;
import com.thu.service.QuestionService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by source on 12/6/16.
 */

@Controller
@RequestMapping("/student")
public class MyQuestionListController {

    // 得到当前会话
    @Autowired
    HttpSession session;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private QuestionService questionService;

    @GetMapping(value = "/home/question/all")
    @ResponseBody
    public Object getQuestions(@RequestParam(name = "page_num", required = false, defaultValue = "0") Integer pageNum,
                               @RequestParam(name = "page_size", required = false, defaultValue = "4") Integer pageSize)
    {


        Page<Question> questionPage = null;

        // createdTime likes 创建时间即创建的ID
        List<String> orders = new ArrayList<>();
        orders.add("questionId");

        Long userId = null;
        userId = (Long) session.getAttribute("userId");

        questionPage = questionService.findMyQuestions(pageNum, pageSize, userId, orders);

        List<Question> questions = questionPage.getContent();


        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        jsonObject.put("question_list", jsonArray);

        User user = userRepository.findById(userId);


        for (Question question : questions)
        {
            JSONObject tmp = new JSONObject();
            tmp.put("question_id", question.getQuestionId());
            tmp.put("question_title", question.getTitle());
            tmp.put("question_content", question.getContent());
            tmp.put("question_location", question.getCreatedLocation());
            tmp.put("like_num", question.getLikes());

            tmp.put("liked", user.getLikedQuestions().contains(question) ? 1 : 0);

            // 问题作者，问题创建时间
            tmp.put("author", question.getUser().getUname());
            tmp.put("createdTime", question.getCreatedTime().toLocalDate());
            tmp.put("unread", user.getUnreadQuestions().contains(question) ? 1 : 0);

            jsonArray.put(tmp);
        }

        System.out.println(jsonObject.toString());

        return jsonObject.toString();
    }
}
