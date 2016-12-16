package com.thu.web.student;

import com.thu.domain.Question;
import com.thu.domain.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by source on 12/16/16.
 */

@Controller
public class QuestionDetailController {

    @Autowired
    private QuestionRepository questionRepository;

    @GetMapping(value = "/question/")
    public Object getAQuestion(@RequestParam("question_id") Long questionId, Model model, HttpServletRequest request)
    {
        Question question = questionRepository.findByQuestionId(questionId);
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

        String leaderRoleName = "未分配";
        if(question.getLeaderRole() != null)
        {
            leaderRoleName = question.getLeaderRole().getDisplayName();
        }
        model.addAttribute("leaderRoleName", leaderRoleName);

        model.addAttribute("question", question);
        return "question_detail";
    }
}
