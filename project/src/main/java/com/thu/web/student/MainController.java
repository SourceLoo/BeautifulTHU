package com.thu.web.student;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by source on 12/2/16.
 */

@Controller
public class MainController {

    @GetMapping(value = "/lyqtest")
    public String getMyTest(HttpServletRequest request)
    {
        // System.out.println(request.getSession().getServletContext().getRealPath("/image"));
        return "lyqtest";
    }

    @GetMapping(value = "/status")
    public String getNewestStatus()
    {
        return "redirect:/lyqtest";
    }

    // 问题的若干状态
    @GetMapping("/classify_renew")
    public String getClassify()
    {
        return "classify_renew";
    }

    @GetMapping("/reply_detail")
    public String getReplydetail()
    {
        return "reply_detail";
    }

    @GetMapping("/append")
    public String getAppend() {
        return "append";
    }


    @GetMapping("/question")
    public String getQuestion()
    {
        return "question";
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
