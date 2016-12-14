package com.thu.data;

import com.thu.domain.*;
import com.thu.service.QuestionService;
import com.thu.service.ResponseService;
import com.thu.service.RoleService;
import com.thu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class Initialization implements CommandLineRunner {


    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ResponseRepositiry responseRepositiry;

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

        roleRepository.save(new Role("xuesheng", "xueshengname", "xueshengResp"));
        roleRepository.save(new Role("zongban", "zongbanname", "xiaozhang"));

        userService.insertUser("lyq", "mobile", "fix", "984", "110@qq.com", roleService.findByRole("xuesheng"), "passwd");
        userService.insertUser("xz", "mobile", "fix", "000", "000@qq.com", roleService.findByRole("zongban"), "passwd");

        User lyq = userRepository.findById(new Long(1));
        User xz = userRepository.findById(new Long(2));

        List<String> paths = new ArrayList<>();
        paths.add("/upload/image/20161214194625_138940119.jpg");
        paths.add("/upload/image/20161214194625_622259729.png");
        paths.add("/upload/image/20161214194625_1617113213.jpg");

        questionService.saveQuestion(lyq, "这是标题1", "这是内容1\n\n啦 啦啦", "这是清华大学", paths);
        questionService.saveQuestion(lyq, "这是标题2", "这是内容2", "这是清华大学", paths.subList(0,2));
        questionService.saveQuestion(lyq, "这是标题3", "这是内容3", "这是清华大学", paths.subList(0,1));

        questionService.saveQuestion(lyq, "这是标题4", "这是内容4", "这是清华大学", paths.subList(0,1));
        questionService.saveQuestion(lyq, "这是标题5", "这是内容5", "这是清华大学", paths.subList(0,1));
        questionService.saveQuestion(lyq, "这是标题6", "这是内容6", "这是清华大学", paths.subList(0,1));

        System.out.println(questionService.findById(1L).getCreatedTime().toLocalDate());

        Response response1= new Response("校长回复1", xz);
        //responseRepositiry.save(response1);

        Response response2= new Response("校长回复2", xz);
        //responseRepositiry.save(response2);

        questionService.responsibleDeptRespond(new Long(1), response1);
        questionService.responsibleDeptRespond(new Long(1), response2);
    }

}
