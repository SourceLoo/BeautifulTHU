To老师端

```java
//当user的问题状态改变时，执行下面语句，将问题加入未读中
questionService.modifyUnreadQuestions(user, quetionId, true);
```



To sunji

```json
1. 上传问题后，跳转至第一视角的问题列表页面

2. 第一视角的问题列表页面，去掉所有的筛选条件

3. 第三视角的问题列表的筛选条件失效，js出错了应该。

4. 
/student/login?ticket=xxx # 获取服务端签发的token， 成功获取后跳转至第三视角 问题列表，否则跳转认证auth
receive:
{
   "success":false, // 判断是否有此字段，无则表示正常返回token
   "msg":"用户名密码不正确",
   "token": xxx, 
}

5.
/student/auth?token=xxx # 从localstorage 获取token发送，可空。

6.
测试方法
先get
/student/login?ticket=123
得到token后，再get
/student/auth?token={token}
即可登录成功
```



login

```
1. 需要首先走个申请手续，这个上次说过哈
2. 需要在这边的数据库注册你们的域名
3. 你们的登录页面要跳转到 
https://id.tsinghua.edu.cn/do/off/ui/auth/login/form/{AppIDMD5}/{seq}
其中的两个参数会分配给你们
4. 你们的系统需要有个url处理类似 /login?ticket={ticket} 的请求。发服务端-服务端https请求到
https://id.tsinghua.edu.cn/thuser/authapi/checkticket/{AppID}/{ticket}/{UserIpAddr}
成功的话，服务端会返回用户信息，类似
code=0:zjh=2011980001:yhm=lqx:xm=刘启新:yhlb=J0000:dw=计算中心:email=lqx@mail.com

student/auth
```





done

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
/student/question/list # 返回页面：第三视角问题列表
/student/home # 返回页面：第一视角问题列表

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