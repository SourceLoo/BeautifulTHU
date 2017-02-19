package com.thu.data;

import com.thu.domain.*;
import com.thu.service.QuestionService;
import com.thu.service.ResponseService;
import com.thu.service.RoleService;
import com.thu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class Initialization implements CommandLineRunner {


    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ResponseRepositiry responseRepositiry;

    @Autowired
    private QuestionRepository questionRepository;

    private final QuestionService questionService;
    private final ResponseService responseService;
    private final RoleService roleService;
    private final UserService userService;


    @Autowired
    public Initialization(QuestionService questionService, ResponseService responseService,
                          RoleService roleService, UserService userService) {
        this.questionService = questionService;
        this.responseService = responseService;
        this.roleService = roleService;
        this.userService = userService;

    }

    @Override
    public void run(String... strings) throws Exception {

        roleRepository.save(new Role("student", "学生", "studentResp"));
        roleRepository.save(new Role("xiaoban", "校办", "老师"));
        roleRepository.save(new Role("zongban", "总办", "老师"));
        roleRepository.save(new Role("other1", "相关部门1", "老师"));
        roleRepository.save(new Role("other2", "相关部门2", "老师"));

        // add this line to update display_names
        roleService.init();

        //userService.insertUser("tuanwei", "mobile", "fix", "000", "000@qq.com", roleService.findByRole("tuanwei"), "passwd");
        userService.insertUser("xiaoban", "mobile", "fix", "001", "001@qq.com", roleService.findByRole("xiaoban"), "passwd");
        userService.insertUser("zongban", "mobile", "fix", "002", "002@qq.com", roleService.findByRole("zongban"), "passwd");
        userService.insertUser("other1", "mobile", "fix", "003", "003@qq.com", roleService.findByRole("other1"), "passwd");
        userService.insertUser("other2", "mobile", "fix", "004", "004@qq.com", roleService.findByRole("other2"), "passwd");

    }

}
