package com.thu.web.student;

import com.thu.domain.Question;
import com.thu.domain.QuestionRepository;
import com.thu.domain.UserRepository;
import com.thu.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
 * Created by source on 12/9/16.
 */

@Controller
@RequestMapping("/student/home")
public class MyQuestionDetailController {


    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    QuestionService questionService;

    @Autowired
        UserRepository userRepository;

    @Autowired
    HttpSession session;


    @GetMapping(value = "/question")
    public String getHomeQuestion(@RequestParam(value = "question_id") Long quetionId, Model model)
    {
        Question question = questionRepository.findByQuestionId(quetionId);

        Long userId = (Long) session.getAttribute("userId");
        if(question == null || question.getTUser().getId() != userId)
        {
            return "redirect:/student/home";
        }

        model.addAttribute("question", question);

        // 用户点击某个问题，将这个问题从未读列表中删除
        questionService.modifyUnreadQuestions(quetionId, false);


        String leaderRoleName = "待分配";
        if(question.getLeaderRole() != null)
        {
            leaderRoleName = question.getLeaderRole().getDisplayName();
        }
        model.addAttribute("leaderRoleName", leaderRoleName);

        return "student/myquestion";
    }
}
