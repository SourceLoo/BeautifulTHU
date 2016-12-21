package com.thu.web.student;

import com.thu.domain.User;
import com.thu.domain.UserRepository;
import com.thu.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by source on 12/9/16.
 */

@Controller
public class QuestionEvaluateController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionService questionService;


    @PostMapping(value = "/evaluation/")
    //public ResponseEntity<?> evaluate(
    public String evaluate(
            @RequestParam("question_id") Long questionId,
            @RequestParam("evaluation") String evaluation,
            @RequestParam("detail") String detail,
            HttpServletRequest request)
    {
        System.out.println(questionId);
        System.out.println(evaluation);
        System.out.println(detail);


        //return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        //return new ResponseEntity<>(HttpStatus.OK);
        return "redirect:/evaluate_success/";
    }

}
