# BeautifulTHU

## API of School Part:

### Post
- Get `'role':'display_name'` pair. one for main-repsonsible, one for related.

    /init/get_displayname
    send: none
    receive: [{'role1':'displayname1', ...}, {...}]

- Get permission to login from backend.

    /auth/login
    send: {'uname':'', 'passwd':''}
    receive: {'role':'', 'resp_person':'', 'fixed_phone':'', 'mobile_phone':''}

- Set personal info.

    /info/set
    send: {'resp_person':'', 'fixed_phone':'', 'mobile_phone':'', 'passwd':''}
    receive: {'sucess':bool}

*none for not modify*

- Get contact table content, only TuanWei can access uname&passwd.

    /contact/get
    send: none
    receive: [{'role':'', 'uname':'', 'resp_person':'', 
        'fixed_phone':'', 'mobile_phone':'', 'passwd':''}]

- Set contact table content, only TuanWei can modify.

    /contact/set
    send: {'role':'', 'uname':'', 'resp_person':'', 'fixed_phone':'', 
        'mobile_phone':'', 'passwd':'', 'is_new':bool}
    receive: {'sucess':bool}

- Get questions list. (multi parts?)

    /questions/main/get_all
    send: none
    receive: [{'question_id':'', 'created_time':'', 'status':'', 'resp_role':'', 
        'is_common':bool, 'content':'', 'delay_days':number, 'delay_reason':'', 
        'is_common_top':bool, 'reclassify_reason':''}]

*sort and make delay&reclassify on the top.*

- Get question detail

    /question/main/get_detail
    send: {'question_id':''}
    receive: {'created_location':'', 'pic_path':['','']}

- main delay

    /question/main/delay
    send: {'question_id':''}
    receive: {'sucess':bool}

*back-end verify status.*

- main classify

    /quesiton/main/classify
    send: {'question_id':'', 'leader_role':'', 'other_roles':[], 'deadline':''}
    receive: {'sucess':bool}

*back-end verify status.*

- Get questions list. (multi parts?)

    /questions/related/get_all
    send: none
    receive: [{'question_id':'', 'created_time':'', 'deadline':'', 
        'created_location':'', 'status':'', 'content':'', 'likes':number, 
        'is_read':bool, 'is_common':bool, 'is_common_top':bool}]

*sort by deadline?.*

- Get question detail

    /question/related/get_detail
    send: {'question_id':''}
    receive: {'message':'', 'pic_path':['',''], 
        'responses':[{'response_id':'', 'response_content':''}]}

*set 'is_read' true.*

- related apply for reclassification

    /quesiton/related/reclassify
    send: {'question_id':'', 'reclassify_reason':''}
    receive: {'sucess':bool}

*back-end verify status.*

- related apply for delay

    /quesiton/related/delay
    send: {'question_id':'', 'delay_reason':'', 'delay_days':''}
    receive: {'sucess':bool}

*back-end verify status.*

- related response

    /quesiton/related/response
    send: {'question_id':'', 'response':''}
    receive: {'sucess':bool}

- related modify response

    /quesiton/related/modify_response
    send: {'question_id':'', 'response_id':'', 'response_content':''}
    receive: {'sucess':bool}

- TuanWei, QA, get all questions, same as above
- TuanWei, QA, set question as common

    /qa/add
    send: {'question_id':''}
    receive: {'sucess':bool}

    /qa/del
    send: {'question_id':''}
    receive: {'sucess':bool}

    /qa/top
    send: {'question_id':''}
    receive: {'sucess':bool}
*back-end verify question property*

- TODO: data statistics

    /statistics/get
    send: none
    receive: {'item1':number, ...}

## API of Student Part:

### Get

- Getting all the question that this user can view by part.

```json
/question/all/PART_NUM
{
  "questionUrlList" : ["/question/QUESTION_ID_1","/question/QUESTION_ID_2"]
}
```

- Getting detail of certain question.

```json
/question/QUESTION_ID
{
  "id" : "",
  "title" : "",
  "photo" : "",
  "text" : "",
  "creatorUrl" : "",
  "leaderDepartment" : "",
  "statu" : "",
  "createTime" : "",
  "location" : "",
  "likeNum" : ""
}
```

### Post

- Get permission to login from backend.

    /auth/login

- Upload the several pictures and a suggest

    /question/upload

- Like certain question

    /like/question/QUESTION_ID

- Like certain response

    /like/response/QUESTION_ID

- Dislike certain question

    /dislike/question/RESPONSE_ID

- Dislike certain response

    /dislike/response/RESPONSE_ID

##分工：

李元丙：数据操作权限，逻辑判断函数；

袁海涛：*分类问题*、下放问题、回复和追加回复

赵一峰：数据统计、审核、时间逻辑、QA

鲁源泉：学生端后台（获取进度、写入评价、上传图片）

孙佶：学生端前端，逻辑、界面

陈振寰：部门端前端，逻辑、界面

## 数据库表

### 用户表
（部门可以多个账号，一个账号只能同时在线一个）
用户id；用户名；密码；手机号；邮箱；学号；

### 角色表（**非牵头小于等于无关**）
名字；权限等级；通讯录修改；问题拒绝；问题回复；问题转交；问题分类；延长ddl；回答审核；收到问题总数；按时完成数；超时完成数；拒绝问题数；直接回复数；
名字包括：学生，三个，其他部门；

### 问题表
（审核+分类时间超时如何处理？校办和总办各自拥有1h）
问题id；标题；图片；内容；创建者id；问题类别（牵头部门）；状态（待审核、待分类、待解决、已解决、无效）；学生评价；问题创建位置；
问题创建时间；点赞数；ddl；转交时间1；转交时间2；是否是常见问题；是否是常见问题置顶；

### 非牵头部门表
问题id；一个部门id；

### 问题点赞表
用户id；一个问题id；

### 回复点赞表
用户id；一个回复id；

### 回复表（多个回复对应一个问题）
回复id；回复内容；问题id；回复时间；点赞数；回复者id；

