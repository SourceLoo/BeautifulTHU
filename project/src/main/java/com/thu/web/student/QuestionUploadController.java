package com.thu.web.student;

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
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * Created by source on 12/9/16.
 */

@RestController
public class QuestionUploadController {

    //获得property文件的变量
    @Autowired
    private Environment env;


    @PostMapping(value = "/question/upload")
    public ResponseEntity<?> uploadQuestion(
            @RequestParam("uploadfile") MultipartFile uploadfile,
            @RequestParam("question_title") String title,
            @RequestParam("question_content") String content,
            @RequestParam("location") String location)
    {

        System.out.println("Begin to upload...");
        try
        {
            String directory = env.getProperty("BeautifulTHU.uploadedImgs");
            String originalFilename = uploadfile.getOriginalFilename();
            String fileName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + "_" + new Random().nextInt()+originalFilename.substring(originalFilename.lastIndexOf("."));


            String filepath = Paths.get(directory, fileName).toString();
            System.out.println(fileName);
            System.out.println(filepath);

            // Save the file locally
            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(filepath)));
            stream.write(uploadfile.getBytes());
            stream.close();



        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
