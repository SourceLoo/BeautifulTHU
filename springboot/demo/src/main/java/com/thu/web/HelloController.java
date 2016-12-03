package com.thu.web;

import com.thu.domain.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by source on 11/26/16.
 */
@RestController
public class HelloController {

    //@GetMapping("/hello") 等价于 @RequestMapping("/hello", method = RequestMethod.GET)
    @GetMapping("/hello")
    public String hello() {
        return "Say Hello again.";
    }

    @RequestMapping("/")
    public String index() {
        return "Say Hello.";
    }

}
