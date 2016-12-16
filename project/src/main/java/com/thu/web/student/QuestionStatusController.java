package com.thu.web.student;

import com.thu.domain.Question;
import com.thu.domain.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by source on 12/9/16.
 */

@Controller
public class QuestionStatusController {


    @Autowired
    QuestionRepository questionRepository;

    private String getStatus(Long questionId)
    {

        Question question = questionRepository.findByQuestionId(questionId);

        String status = null;
        switch (question.getStatus())
        {
            case UNAPPROVED: status = "/upapproved"; break;
            case UNCLASSIFIED: status = "未分类"; break;
            case UNSOLVED: status = "未解决"; break;
        }

        status = "classified";
        status = "replied";
        status = "append";
        return status;
    }

    @GetMapping(value = "/question/status")
    public String getNewestStatus(@RequestParam(value = "question_id") Long quetionId)
    {
        String status = getStatus(quetionId);
        return "redirect:/question/" + status + "?question_id=" + quetionId;
    }

    // 问题的若干状态
    @GetMapping("/question/classified")
    public String getClassified(@RequestParam(value = "question_id") Long quetionId, Model model)
    {
        String status = getStatus(quetionId);
        if(! "classified".equals(status))
        {
            return "redirect:/question/" + status + "?question_id=" + quetionId;
        }

        model.addAttribute("question", questionRepository.findByQuestionId(quetionId));

        return "classify_renew";
    }

    @GetMapping("/question/replied")
    public String getReplydetail(@RequestParam(value = "question_id") Long quetionId, Model model)
    {
        String status = getStatus(quetionId);
        if(! "replied".equals(status))
        {
            return "redirect:/question/" + status + "?question_id=" + quetionId;
        }

        model.addAttribute("question", questionRepository.findByQuestionId(quetionId));
        return "reply_detail";
    }

    @GetMapping("/question/append")
    public String getAppend(@RequestParam(value = "question_id") Long quetionId, Model model)
    {
        String status = getStatus(quetionId);
        if(! "append".equals(status))
        {
            return "redirect:/question/" + status + "?question_id=" + quetionId;
        }
        model.addAttribute("question", questionRepository.findByQuestionId(quetionId));

        return "append";
    }


}
