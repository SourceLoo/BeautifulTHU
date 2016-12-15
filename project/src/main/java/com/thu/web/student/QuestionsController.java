package com.thu.web.student;

import com.thu.domain.*;
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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by source on 12/6/16.
 */

@Controller
@RequestMapping(value = "/question")
public class QuestionsController {


    // 得到当前会话
    @Autowired
    HttpSession session;

    @Autowired
    private QuestionRepository questionRepository;

//    @Autowired
//    private UserRepository userRepository;

    @GetMapping(value = "/")
    public Object getAQuestion(@RequestParam("question_id") Long questionId, Model model, HttpServletRequest request)
    {
        //model.addAttribute("base", request.getContextPath());
        //System.out.println("base=" + request.getContextPath());

        // sql: 按questionId 得到question对象

        System.out.println(questionId);
        Question question = questionRepository.findByQuestionId(questionId);
        //question.setUser(null);
        model.addAttribute(question);

        String status = null;
        switch (question.getStatus())
        {
            case UNAPPROVED: status = "未批准"; break;
            case UNCLASSIFIED: status = "未分类"; break;
            case UNSOLVED: status = "未解决"; break;
            case SOLVING: status = "正在解决"; break;
            case RECLASSIFY: status = "重新分类"; break;
            case DELAY: status = "问题延迟"; break;
            case INVALID: status = "问题非法"; break;
            default: status = "未定义";
        }
        model.addAttribute("status", status);
        model.addAttribute("uname", question.getUser().getUname());

        model.addAttribute("pics", question.getPics());

        String leaderRole = "未分配";
        if(question.getLeaderRole() != null)
        {
            leaderRole = question.getLeaderRole().getRole();
        }
        model.addAttribute("leaderRole", leaderRole);

        model.addAttribute("responses", question.getResponses());

        //model.addAttribute()

        return "question_detail";
    }

    @GetMapping(value = "/all/")
    @ResponseBody
    public Object getQuestions(@RequestParam(name = "page_num") Integer pageNum,
                               @RequestParam(name = "state_condition", required = false) String state,
                               @RequestParam(name = "depart_condition", required = false) String depart,
                               @RequestParam(name = "order_type" , required = false) String orderType,
                               @RequestParam(name = "keywords" , required = false) String searchKey)
    {
        // pageSize 由后台自定义
        Integer pageSize = 2;
        //Integer userId = (Integer)session.getAttribute("userId");

        Page<Question> questionPage = questionRepository.findAll(new PageRequest(pageNum, pageSize));


        List<Question> questions = questionPage.getContent();
        System.out.println("begin");

        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();

        jsonObject.put("question_list", jsonArray);

        for (Question question : questions)
        {
            //System.out.println(question);
            //question.setLeaderRole(null);
            //question.setOtherRoles(null);
            //question.setPics(null);
            //question.setUser(null);
            JSONObject tmp = new JSONObject();
            tmp.put("question_id", question.getQuestionId());
            tmp.put("question_title", question.getTitle());
            tmp.put("question_content", question.getContent());
            tmp.put("question_location", question.getCreatedLocation());
            //question.setLikes(1024L);
            tmp.put("like_num", question.getLikes());

            jsonArray.put(tmp);
        }

        System.out.println("done");
        System.out.println(jsonObject.toString());

        return jsonObject.toString();
    }
}
