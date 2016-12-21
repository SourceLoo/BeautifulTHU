package com.thu.web.student;

import com.thu.domain.User;
import com.thu.domain.UserRepository;
import com.thu.service.QuestionService;
import com.thu.service.ResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @PostMapping(value = "/question/like")
    public ResponseEntity<?> modifyQuestionLikes(
            @RequestParam("question_id") Long questionId,
            @RequestParam("like") boolean like
    )
    {
        //if(!"true".equals(like) & !"fa")

        // like 为真，则点赞；否则取消点赞
        Long userId = (Long) session.getAttribute("userId");
        userId = new Long(1);
        User user = userRepository.findById(userId);

        if(questionService.modifyQuestionLike(user, questionId, like))
            return new ResponseEntity<>(HttpStatus.OK);
        else
            return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping(value = "/response/like")
    public ResponseEntity<?> modifyResponseLikes(
            @RequestParam("response_id") Long responseId,
            @RequestParam("like") boolean like
    )
    {
        // like 为真，则点赞；否则取消点赞
        Long userId = (Long) session.getAttribute("userId");
        userId = new Long(1);
        User user = userRepository.findById(userId);

        if(responseService.modifyResponseLike(user, responseId, like))
            return new ResponseEntity<>(HttpStatus.OK);
        else
            return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }
}
