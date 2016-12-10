package com.thu.web.student;

import com.thu.domain.Question;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by source on 12/9/16.
 */

@RestController
public class QuestionUpdateController {



    @GetMapping(value = "/new_classify/{QUESTION_ID}")
    public Object getClassify(@PathVariable("QUESTION_ID") Long questionId)
    {
        System.out.println(questionId);
        Question q = new Question("这是标题", "这是内容");
        //q.setQuestionId(questionId);

        return q;
    }

    @GetMapping(value = "/new_response/{QUESTION_ID}")
    public Object getResponse(@PathVariable("QUESTION_ID") Long questionId)
    {
        System.out.println(questionId);
        Question q = new Question("这是标题", "这是内容");
        //q.setQuestionId(questionId);

        return q;
    }

}
