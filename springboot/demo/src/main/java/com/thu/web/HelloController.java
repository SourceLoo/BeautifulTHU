package com.thu.web;

import com.thu.domain.Student;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

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


    // 这个没有@ResponseBody 看 return值，会在默认目录src/main/resources/templates下寻找blog模板
    // 增加model。返回到模板时，model会一起返回。

    @GetMapping("/blogs/{id}")
    public String blog(@PathVariable String id, Model model) {

        model.addAttribute("title", "博文ID是：" + id);
        model.addAttribute("createdTime", new Date().toString());
        model.addAttribute("content", "我的第一篇博文！");

        return "blogs/index";
    }

}
