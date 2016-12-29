package com.thu.web.student;

import com.thu.domain.Question;
import com.thu.domain.QuestionRepository;
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
    HttpSession session;


    @GetMapping(value = "/question")
    public String getHomeQuestion(@RequestParam(value = "question_id") Long quetionId, Model model)
    {
        Question question = questionRepository.findByQuestionId(quetionId);

        Long userId = (Long) session.getAttribute("userId");
        userId = 1L;
        if(question == null || question.getUser().getId() != userId)
        {
            return "redirect:/student/home";
        }

        model.addAttribute("question", question);
        return "student/myquestion";
    }
}
