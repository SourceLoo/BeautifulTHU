package com.thu.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by source on 12/2/16.
 */

@Controller
public class HelloController {

    @GetMapping(value = "/lyqtest")
    public String getMyTest(HttpServletRequest request)
    {
        System.out.println(request.getSession().getServletContext().getRealPath("/image"));
        return "lyqtest";
    }

    @GetMapping("/greeting")
    public String getGreeting()
    {
        return "greeting";
    }

    @GetMapping("/classify_renew")
    public String getClassify_renew()
    {
        return "classify_renew";
    }

    @GetMapping("/homepage")
    public String getHomepage()
    {
        return "homepage";
    }

    @GetMapping("/question")
    public String getQuestion()
    {
        return "question";
    }

    @GetMapping("/question_detail")
    public String getQuestiondetail()
    {
        return "question_detail";
    }


    @GetMapping("/reply_detail")
    public String getReplydetail()
    {
        return "reply_detail";
    }

    @GetMapping("/upload")
    public String getUpload()
    {
        return "upload";
    }

    @GetMapping("/upload_success")
    public String getUpload_success()
    {
        return "upload_success";
    }
}
