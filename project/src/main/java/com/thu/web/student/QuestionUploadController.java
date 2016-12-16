package com.thu.web.student;

import com.thu.domain.*;
import com.thu.service.QuestionService;
import com.thu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by source on 12/9/16.
 */

@RestController
public class QuestionUploadController {

    //获得property文件的变量
    @Autowired
    private Environment env;

    @Autowired
    private HttpSession session;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionService questionService;


    @PostMapping(value = "/question/upload")
    public ResponseEntity<?> uploadQuestion(
            @RequestParam("uploadfiles") MultipartFile[] uploadfiles,
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam(name="location", required=false, defaultValue="清华大学") String location,
            HttpServletRequest request)
    {
        List<String> paths = new ArrayList<>();

        // 拷贝到本地
        for (MultipartFile uploadfile : uploadfiles)
        {
            try
            {
                String directory = env.getProperty("image.localpath");
                String originalFilename = uploadfile.getOriginalFilename();
                String fileName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + "_" + Math.abs(new Random().nextInt())+originalFilename.substring(originalFilename.lastIndexOf("."));


                String filepath = Paths.get(directory, fileName).toString();

                // Save the file locally
                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(filepath)));
                stream.write(uploadfile.getBytes());
                stream.close();

                System.out.println(originalFilename + "\t" + filepath);

                paths.add(env.getProperty("image.webpath") + "/" + fileName);

            }
            catch (Exception e)
            {
                System.out.println(e.getMessage());
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

        }


        Long userId = (Long) session.getAttribute("userId");
        userId = new Long(1);
        User user = userRepository.findById(userId);

        //public boolean insertQuestion(String title, String content, User user, String createdLocation, Date createdTime, List<Pic> pics) {

        System.out.println(title);
        System.out.println(content);
        System.out.println(location);
        questionService.saveQuestion(user, title, content, location, paths);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
