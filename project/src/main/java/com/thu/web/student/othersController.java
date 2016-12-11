package com.thu.web.student;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by source on 12/9/16.
 */
@RestController
public class othersController {

    @GetMapping("/getDept/all")
    public Object getAllDepts()
    {

        return null;
    }
}
