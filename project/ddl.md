deadline：12.31



改动：

- html文件放入/template/student/
- 主要目录如下

```
问题列表，question.html
http://localhost:8080/student/question/list
问题上传，upload.html
http://localhost:8080/student/question/upload
问题评价 reply_detail.html
http://localhost:8080/student/question/replied?question_id=2
```



TODO：问题列表主页面。



TODO：从后台获取分类

```json
Get: /student/getStatus/all
{
    "status_list" : [{
        "name" :,
    }]
}
```



TODO：学生对问题、回复点赞

```json
POST:/student/question/like
{
  question_id:,
  like: true(点赞)|false(取消点赞)
}

POST:/student/response/like
{
  response_id:,
  like: true(点赞)|false(取消点赞)
}
```





TODO：学生对问题的评价，ajax没有获得模板引擎的question_id

```json
POST: /student/question/evaluate
{
    question_id: ,
    evaluation: "无评价"|"满意"|"不满意",
    detail: "DETAIL"
}
```


TODO：开会提及界面不友好