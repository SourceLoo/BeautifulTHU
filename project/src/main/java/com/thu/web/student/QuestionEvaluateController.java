package com.thu.web.student;

import com.thu.domain.EvaluationType;
import com.thu.domain.Role;
import com.thu.domain.RoleRepository;
import com.thu.domain.UserRepository;
import com.thu.service.QuestionService;
import com.thu.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by source on 12/9/16.
 */

@Controller
@RequestMapping("/student")
public class QuestionEvaluateController {

    private final String errorMsg = "{\"success\":false,\"msg\":\"重复提交\"}";
    private final String successMsg = "{\"success\":true,\"msg\":\"Done\"}";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private RoleRepository roleRepository;

    @PostMapping(value = "/question/evaluate")
    @ResponseBody
    public String evaluate(
            @RequestParam("question_id") Long questionId,
            @RequestParam("evaluation") String evaluation,
            @RequestParam("detail") String detail,
            HttpServletRequest request)
    {
        EvaluationType evaluationType = null;

        Role role = questionService.findById(questionId).getLeaderRole();

        switch (evaluation)
        {
            case "无评价": evaluationType = EvaluationType.NOEVALUATION; break;

            case "满意":
                evaluationType = EvaluationType.SATISFIED;
                role.setGoodNumber(role.getGoodNumber() + 1);
                break;
            case "不满意":
                evaluationType = EvaluationType.UNSATISFIED;
                role.setBadNumber(role.getBadNumber() + 1);
                break;

            default: evaluationType = null;
        }

        if(evaluationType == null)
        {
            return errorMsg;
        }

        roleRepository.save(role);

        if(questionService.saveStudentResponse(questionId, evaluationType, detail) == false)
        {
            return errorMsg;
        }

        System.out.println(questionId);
        System.out.println(evaluation);
        System.out.println(detail);


        return successMsg;
    }

}
