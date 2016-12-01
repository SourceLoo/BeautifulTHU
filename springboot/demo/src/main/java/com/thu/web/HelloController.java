package com.thu.web;

import com.thu.domain.Student;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by source on 11/26/16.
 */
@Controller
public class HelloController {

    @RequestMapping("/")
    @ResponseBody
    public String index() {
        return "Index page!";
    }

    //@GetMapping("/hello") 等价于 @RequestMapping("/hello", method = RequestMethod.GET)
    @GetMapping("/hello")
    @ResponseBody
    public String hello() {
        return "Hello World~";
    }


    // 这个没有@ResponseBody 会在默认目录src/main/resources/templates下寻找blog模板
    @GetMapping("/about/blog")
    public String blog() {
        return "about/blog";
    }

}
