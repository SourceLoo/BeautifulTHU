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

        //roleRepository.save(new Role("xuesheng", "学生", "xueshengResp"));
        //roleRepository.save(new Role("zongban", "综办", "老师甲"));
        //roleRepository.save(new Role("xiaoban", "校办", "老师乙"));
        //roleRepository.save(new Role("tuanwei", "团委", "老师丙"));

        //userService.insertUser("学生甲", "mobile", "fix", "984", "110@qq.com", roleService.findByRole("xuesheng"), "passwd");
        //userService.insertUser("学生乙", "mobile", "fix", "984", "110@qq.com", roleService.findByRole("xuesheng"), "passwd");
        //userService.insertUser("老师甲", "mobile", "fix", "000", "000@qq.com", roleService.findByRole("zongban"), "passwd");
        //userService.insertUser("老师乙", "mobile", "fix", "001", "001@qq.com", roleService.findByRole("xiaoban"), "passwd");
        //userService.insertUser("老师丙", "mobile", "fix", "002", "002@qq.com", roleService.findByRole("tuanwei"), "passwd");

        //User s1 = userRepository.findById(new Long(1));
        //User s2 = userRepository.findById(2L);
        //User t1 = userRepository.findById(new Long(3));
        //User t2 = userRepository.findById(new Long(4));
        //User t3 = userRepository.findById(new Long(5));

        //List<String> paths = new ArrayList<>();
        //paths.add("/upload/image/20161214194625_138940119.jpg");
        //paths.add("/upload/image/20161214194625_622259729.png");
        //paths.add("/upload/image/20161214194625_1617113213.jpg");


        //Question question;

        //questionService.saveQuestion(s1, "这是标题1", "这是内容1\n\n测试1", "这是清华大学", paths);
        //questionService.saveQuestion(s1, "这是标题2", "这是内容2\n测试2", "这是清华大学", paths.subList(0,2));
        //questionService.saveQuestion(s1, "这是标题3", "这是内容3\n测试3", "这是清华大学", paths.subList(0,1));
        //questionService.saveQuestion(s2, "这是标题4", "这是内容4\n换行", "这是清华大学", paths.subList(0,1));
        //questionService.saveQuestion(s2, "这是标题5", "这是内容5", "这是清华大学", paths.subList(0,1));
        //questionService.saveQuestion(s2, "这是标题6", "这是内容6\n测试", "这是清华大学", paths.subList(0,1));

        //question = questionService.findById(1L);
        //question.setLeaderRole(roleService.findByRole("zongban"));
        //question.setLikes(4L);
        //questionRepository.save(question);



        //question = questionService.findById(2L);
        //question.setLeaderRole(roleService.findByRole("zongban"));
        //question.setLikes(2L);
        //questionRepository.save(question);

        //question = questionService.findById(3L);
        //question.setLeaderRole(roleService.findByRole("xiaoban"));
        //question.setCommon(true);
        //question.setDdl(LocalDateTime.now().plusDays(2));
        //question.setLikes(6L);
        //questionRepository.save(question);

        //question = questionService.findById(4L);
        //question.setLeaderRole(roleService.findByRole("xiaoban"));
        //question.setDdl(LocalDateTime.now().plusDays(4));
        //question.setLikes(4L);
        //questionRepository.save(question);


        //question = questionService.findById(5L);
        //question.setCommon(true);
        //question.setLeaderRole(roleService.findByRole("tuanwei"));
        //question.setLikes(3L);
        //questionRepository.save(question);


        //question = questionService.findById(6L);
        //question.setCommon(true);
        //question.setLeaderRole(roleService.findByRole("tuanwei"));
        //question.setLikes(0L);
        //questionRepository.save(question);

        //Response response1= new Response("老师甲的回复\n\n回复第2行", t1);
        ////responseRepositiry.save(response1);

        //Response response2= new Response("老师乙的回复", t2);
        ////responseRepositiry.save(response2);

        //questionService.responsibleDeptRespond(new Long(1), response1);
        //questionService.responsibleDeptRespond(new Long(1), response2);
        //questionService.responsibleDeptRespond(new Long(2), response1);
    }

}
