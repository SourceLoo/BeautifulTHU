package com.thu.web.student;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by source on 12/2/16.
 */

@Controller
@RequestMapping("/student")
public class MainController {

    @GetMapping("/question/list")
    public String getQuestion()
    {
        return "student/question";
    }

    @GetMapping("/question/upload")
    public String getUpload()
    {
        return "student/upload";
    }

    @GetMapping("/question/upload_success")
    public String getUploadSuccess()
    {
        return "student/upload_success";
    }

    @GetMapping("/question/evaluate_success")
    public String getEvaluateSuccess()
    {
        return  "student/evaluate_success";
    }

    @GetMapping("/home")
    public String getHomepage()
    {
        return  "student/homepage";
    }


}
