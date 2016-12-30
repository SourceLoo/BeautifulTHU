接口

```json
POST
/student/question/evaluate?question_id=xxx # 返回json：评估问题
/student/question/upload?question_id=xxx # 返回json：上传问题
receive:
{"success":false|true,"msg":"xxx"}
对返回的json判断


/student/home/status # 个人主页 增加unread
receive:
{
   "unread": (0|1)
}

    
/student/home # 个人主页 问题列表 去除所有筛选条件
student/homepage # 后台返回页面，在此页面请求/student/home/question/all

/student/home/question/all 
receive:
{
  "question_list" : [
    {
        "question_id" : "QUESTION_ID",
        "question_title" : "QUESTION_TITLE",
        "question_content" : "QUESTION_CONTENT",
        "question_location" : "QUESTION_LOCATION",
        "like_num" : "like number",
      
        "liked" : (0|1),
      
      	"unread":(0|1)
    }
    ]
}


/student/home/question?question_id=xxx # 个人主页 的问题详情地址


状态图片：
受理 - 分类 - 答复
三张，每张点亮一个
```





备注

```json
/student/question/all # 返回json：第三视角问题列表 QuestionList
/student/home/question/all # 返回json：第一视角问题列表 MyQuestionList

/student/question?question_id=xxx # 后台渲染：第三视角问题 QuestionDetail
/student/home/question?question_id=xxx # 后台渲染：第一视角问题 MyQuestionDetail


MainController
/student/question/list # 返回页面：首页
/student/home # 返回页面：个人主页

/student/question/upload # 返回页面：创建问题
/student/question/upload_success # 返回页面：成功创建
/student/question/evaluate_success # 返回页面：成功评价


/student/question/like?question_id=xxx # 返回json：点赞 ModifyLikesController
/student/response/like?question_id=xxx # 返回json：点赞

/student/getDept/all # 返回json：
/student/getStatus/all # 返回json：

POST
/student/question/evaluate?question_id=xxx # 返回json：评估问题
/student/question/upload?question_id=xxx # 返回json：上传问题
```