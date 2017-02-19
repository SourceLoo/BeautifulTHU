package com.thu.web.student;

import com.thu.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by source on 12/16/16.
 */

@Controller
@RequestMapping("/student")
public class QuestionDetailController {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HttpSession session;

    @GetMapping(value = "/question")
    public Object getAQuestion(@RequestParam("question_id") Long questionId, Model model, HttpServletRequest request)
    {
        Question question = questionRepository.findByQuestionId(questionId);
        model.addAttribute(question);

        Status status = question.getStatus();
        String statusStr = null;
        System.out.println(question.getStatus());
        for (String key : GetStatusAndDepartController.statusMap.keySet())
        {
            if(GetStatusAndDepartController.statusMap.get(key).contains(status))
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
        if(question.getTransferRole() != null)
        {
            leaderRoleName = question.getTransferRole().getDisplayName();
        }
        else if(question.getLeaderRole() != null)
        {
            leaderRoleName = question.getLeaderRole().getDisplayName();
        }
        model.addAttribute("leaderRoleName", leaderRoleName);

        model.addAttribute("question", question);

        Long userId = (Long) session.getAttribute("userId");
        TUser TUser = userRepository.findById(userId);

        List<UserResponse> userResponses = new ArrayList<>();
        for(Response response: question.getResponses())
        {
            String liked = TUser.getLikedRespones().contains(response) ? "已赞" : "赞";
            UserResponse userResponse = new UserResponse(liked, response);
            userResponses.add(userResponse);
        }

        model.addAttribute("userResponses", userResponses);

        return "student/question_detail";
    }
}

class UserResponse
{
    private String liked;
    private Response response;

    public UserResponse(String liked, Response response) {
        this.liked = liked;
        this.response = response;
    }

    public String getLiked() {
        return liked;
    }

    public void setLiked(String liked) {
        this.liked = liked;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }
}