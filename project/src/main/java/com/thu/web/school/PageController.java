package com.thu.web.school;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/school")
public class PageController {

    @GetMapping("/manage_console")
    public String getMain()
    {
        return "school/manage_console";
    }

    @GetMapping("/manage_console/{foo:.+}")
    public String getMain2()
    {
        return "redirect:/school/manage_console";
    }

}
