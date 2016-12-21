package com.thu.web.student;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by source on 12/2/16.
 */

@Controller
@RequestMapping("/student/question")
public class MainController {

//    @GetMapping(value = "/lyqtest")
//    public String getMyTest(HttpServletRequest request)
//    {
//        // System.out.println(request.getSession().getServletContext().getRealPath("/image"));
//        return "lyqtest";
//    }

    @GetMapping("/list")
    public String getQuestion()
    {
        return "student/question";
    }

    @GetMapping("/upload")
    public String getUpload()
    {
        return "student/upload";
    }

    @GetMapping("/upload_success")
    public String getUpload_success()
    {
        return "student/upload_success";
    }

    @GetMapping("/evaluate_success")
    public String getEvaluate_success()
    {
        return  "student/evaluate_success";
    }


}
