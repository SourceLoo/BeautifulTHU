package com.thu.web;

import com.thu.domain.Student;
import org.springframework.web.bind.annotation.*;

/**
 * Created by source on 12/1/16.
 */

@RestController
@RequestMapping(value = "/students") // 响应root/students/xxx路径
public class StudentController {

    /* 返回的对象作为json字符串形式 */
    @GetMapping(value = "/way1/{studentId}") // 响应root/students/way1/2016 其中2016作为参数传入
    public Student getStudent(@PathVariable Integer studentId)
    {
        System.out.println("前台传入的用户id是：" + studentId);

        // 实际流程是，从前台拿到id后，从数据库查询访问，拿到此用户所有信息，返回后台。

        Student st = new Student("2016", "luyq");
        return  st;

    }

    //@GetMapping(value = "") //响应路径为students/way2?studentId=, 注意不要添加/ 下面这种更好
    @GetMapping(value = "/way2")
    public Student getStudent2(@RequestParam(name = "studentId", required = false, defaultValue = "0") Integer studentId) // 或者(@RequestParam Integer studentId)
    {
        System.out.println("前台传入的用户id是：" + studentId);

        // 实际流程是，从前台拿到id后，从数据库查询访问，拿到此用户所有信息，返回后台。
        return  new Student("2017", "luyq");

    }
}
