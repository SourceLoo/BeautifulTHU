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
        roleRepository.save(new Role("tuanwei", "团委", "老师1"));
        roleRepository.save(new Role("xiaoban", "校办", "老师2"));
        roleRepository.save(new Role("zongban", "总办", "老师3"));
        roleRepository.save(new Role("other1", "相关部门1", "老师4"));
        roleRepository.save(new Role("other2", "相关部门2", "老师5"));

        //userService.insertUser("tuanwei", "mobile", "fix", "000", "000@qq.com", roleService.findByRole("tuanwei"), "passwd");
        userService.insertUser("xiaoban", "mobile", "fix", "001", "001@qq.com", roleService.findByRole("xiaoban"), "passwd");
        userService.insertUser("zongban", "mobile", "fix", "002", "002@qq.com", roleService.findByRole("zongban"), "passwd");
        userService.insertUser("other1", "mobile", "fix", "003", "003@qq.com", roleService.findByRole("other1"), "passwd");
        userService.insertUser("other2", "mobile", "fix", "004", "004@qq.com", roleService.findByRole("other2"), "passwd");

        //TUser s1 = userRepository.findById(1L);
        //TUser s2 = userRepository.findById(2L);
        //TUser t1 = userRepository.findById(3L);
        //TUser t2 = userRepository.findById(4L);
        //TUser t3 = userRepository.findById(5L);

        //List<String> paths = new ArrayList<>();
        //paths.add("/upload/image/20161214194625_138940119.jpg");
        //paths.add("/upload/image/20161214194625_622259729.png");
        //paths.add("/upload/image/20161214194625_1617113213.jpg");


        //Response response1= new Response("老师甲的回复\n\n回复第2行", t1);
        //Response response2= new Response("老师乙的回复", t2);

        //Question question;

        //questionService.saveQuestion(s1, "这是标题1", "这是内容1\n\n测试1", "这是清华大学", paths);
        //questionService.saveQuestion(s1, "这是标题2", "这是内容2\n测试2", "这是清华大学", paths.subList(0,2));
        //questionService.saveQuestion(s1, "这是标题3", "这是内容3\n测试3", "这是清华大学", paths.subList(0,1));
        //questionService.saveQuestion(s2, "这是标题4", "这是内容4\n换行", "这是清华大学", paths.subList(0,1));
        //questionService.saveQuestion(s2, "这是标题5", "这是内容5", "这是清华大学", paths.subList(0,1));
        //questionService.saveQuestion(s2, "这是标题6", "这是内容6\n测试", "这是清华大学", paths.subList(0,1));

        //question = questionService.findById(1L);
        //System.out.println(roleService.findByRole("zongban"));
        //question.setLeaderRole(roleService.findByRole("zongban"));
        //question.setLikes(4L);
        //questionRepository.save(question);



        //question = questionService.findById(2L);
        //question.setLeaderRole(roleService.findByRole("zongban"));
        //question.setLikes(2L);
        //question.setStatus(Status.UNCLASSIFIED);
        //questionRepository.save(question);

        //question = questionService.findById(3L);
        //question.setLeaderRole(roleService.findByRole("xiaoban"));
        //question.setCommon(true);
        //question.setDdl(LocalDateTime.now().plusDays(2));
        //question.setLikes(6L);
        //question.setStatus(Status.UNSOLVED);
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
        //question.setStatus(Status.INVALID);
        //question.setRejectReason("问题不在处理范围");
        //questionRepository.save(question);

        //questionService.responsibleDeptRespond(new Long(4), response1); // 解决中
        //questionService.responsibleDeptRespond(new Long(5), response1); // 解决中
        //questionService.responsibleDeptRespond(new Long(5), response2);

    }

}
