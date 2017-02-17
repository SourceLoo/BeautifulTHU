package com.thu.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created by source on 12/2/16.
 */

@Controller
public class StudentHomeController {

    @GetMapping(value = "/")
    public String getMyTest()
    {
        return "redirect:/student/question/list";
    }
}
