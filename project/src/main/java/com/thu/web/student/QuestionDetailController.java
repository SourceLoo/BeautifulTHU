package com.thu.web.student;

import com.thu.domain.Question;
import com.thu.domain.QuestionRepository;
import com.thu.domain.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * Created by source on 12/16/16.
 */

@Controller
@RequestMapping("/student")
public class QuestionDetailController {

    @Autowired
    private QuestionRepository questionRepository;

    @GetMapping(value = "/question")
    public Object getAQuestion(@RequestParam("question_id") Long questionId, Model model, HttpServletRequest request)
    {
        Question question = questionRepository.findByQuestionId(questionId);
        model.addAttribute(question);

        Status status = question.getStatus();
        String statusStr = null;
        System.out.println(question.getStatus());
//        Map<String, List<Status>> statusMap =
        for (String key : othersController.statusMap.keySet())
        {
            if(othersController.statusMap.get(key).contains(status))
            {
                statusStr = key;
                break;
            }
        }
        if(statusStr == null)
        {
            return "error";
        }

        model.addAttribute("status", statusStr);

        String leaderRoleName = "待分配";
        if(question.getLeaderRole() != null)
        {
            leaderRoleName = question.getLeaderRole().getDisplayName();
        }
        model.addAttribute("leaderRoleName", leaderRoleName);

        model.addAttribute("question", question);
        return "student/question_detail";
    }
}
