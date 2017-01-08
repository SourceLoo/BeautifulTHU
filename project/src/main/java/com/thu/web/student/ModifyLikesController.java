package com.thu.web.student;

import com.thu.domain.ResponseRepositiry;
import com.thu.domain.User;
import com.thu.domain.UserRepository;
import com.thu.service.QuestionService;
import com.thu.service.ResponseService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Created by source on 12/21/16.
 */

@Controller
@RequestMapping("/student")
public class ModifyLikesController {


    @Autowired
    private HttpSession session;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private ResponseService responseService;

    @Autowired
    private ResponseRepositiry responseRepositiry;

    @PostMapping(value = "/question/like")
    @ResponseBody
    public String modifyQuestionLikes(
            @RequestParam("question_id") Long questionId,
            @RequestParam("liked") boolean like
    )
    {
        // like 为真，则点赞；否则取消点赞
        Long userId = (Long) session.getAttribute("userId");
        userId = new Long(1);
        User user = userRepository.findById(userId);

        questionService.modifyQuestionLike(user, questionId, like);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("likenum", questionService.findById(questionId).getLikes());
        return jsonObject.toString();

    }

    @PostMapping(value = "/response/like")
    @ResponseBody
    public String modifyResponseLikes(
            @RequestParam("response_id") Long responseId,
            @RequestParam("liked") boolean like
    )
    {
        // like 为真，则点赞；否则取消点赞
        Long userId = (Long) session.getAttribute("userId");
        userId = new Long(1);
        User user = userRepository.findById(userId);

        responseService.modifyResponseLike(user, responseId, like);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("likenum", responseRepositiry.findByResponseId(responseId).getLikes());
        return jsonObject.toString();
    }
}
