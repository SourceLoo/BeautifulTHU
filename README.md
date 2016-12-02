# BeautifulTHU

## Usage

- Use spring-boot

```
cd project
./mvnw spring-boot:run
```

- Build and run mannually.

```
cd project
./mvnw clean package
java -jar target/gs-rest-service-0.1.0.jar
```
OR

```
# server.address = 0.0.0.0
$ ./start.sh
```


## API of School Part:

### Post
- Get `'role':'display_name'` pair. one for main-repsonsible, one for related.

```
/init/get_displayname
send: none
receive: [{'role1':'displayname1', ...}, {...}]
```

- Get permission to login from backend.

```
/auth/login
send: {'uname':'', 'passwd':''}
receive: {'role':'', 'resp_person':'', 'fixed_phone':'', 'mobile_phone':''}
```

- Set personal info.

```
/info/set
send: {'uname':'', 'resp_person':'', 'fixed_phone':'', 'mobile_phone':'', 'passwd':''}
receive: {'sucess':bool, 'msg':''}
```

*none for not modify*

- Get contact table content, only TuanWei can access uname&passwd.

```
/contact/get
send: none
receive: [{'role':'', 'uname':'', 'resp_person':'',
    'fixed_phone':'', 'mobile_phone':''}]
```

- Set contact table content, only TuanWei can modify.

```
/contact/set
send: {'role':'', 'uname':'', 'resp_person':'', 'fixed_phone':'',
    'mobile_phone':'', 'passwd':'', 'is_new':bool}
receive: {'sucess':bool, 'msg':''}
```

- Get questions list. (multi parts?)

```
/questions/main/get_all
send: {'start':int, 'number':int}
receive: [{'question_id':'', 'created_time':'', 'timestamp1':'', 'timestamp2':'',
    'timestamp3':'' 'status':'', 'resp_role':'',
    'is_common':bool, 'content':'', 'delay_days':number, 'delay_reason':'',
    'is_common_top':bool, 'reclassify_reason':'', 'created_location':'', 'likes':int}]
```

- Get question detail

```
/questions/related/get_detail
send: {'question_id':''}
receive: {'opinion':'', 'pic_path':['',''],
    'responses':[{'response_id':'', 'response_content':''}]}
```


*sort and make delay&reclassify on the top.*

- main response

```
/questions/main/response
send: {'question_id':'', 'response_content':''}
receive: {'sucess':bool, 'msg':''}
```

- main reject

```
/questions/main/reject
send: {'question_id':'', 'response_content':''}
receive: {'sucess':bool, 'msg':''}
```

- main forward

```
/questions/main/forward
send: {'question_id':'', 'forward':'role'}
receive: {'sucess':bool, 'msg':''}
```

- main delay

```
/questions/main/delay
send: {'question_id':''}
receive: {'sucess':bool, 'msg':''}
```

*back-end verify status.*

- main classify

```
/quesitons/main/classify
send: {'question_id':'', 'leader_role':'', 'other_roles':[], 'deadline':'', 'opinion':''}
receive: {'sucess':bool, 'msg':''}
```

*back-end verify status.*

- Get questions list. (multi parts?)

```
/questions/related/get_all
send: none
receive: [{'question_id':'', 'created_time':'', 'deadline':'',
    'created_location':'', 'status':'', 'content':'', 'likes':number,
    'is_read':bool, 'is_common':bool, 'is_common_top':bool}]
```

*sort by deadline?.*

- Get question detail

```
/questions/related/get_detail
send: {'question_id':''}
receive: {'opinion':'', 'pic_path':['',''],
    'responses':[{'response_id':'', 'response_content':''}]}
```

*set 'is_read' true.*

- related apply for reclassification

```
/quesitons/related/reclassify
send: {'question_id':'', 'reclassify_reason':''}
receive: {'sucess':bool, 'msg':''}
```

*back-end verify status & question category.*

- related apply for delay

```
/quesitons/related/delay
send: {'question_id':'', 'delay_reason':'', 'delay_days':int}
receive: {'sucess':bool, 'msg':''}
```

*back-end verify status.*

- related response

```
/quesitons/related/response
send: {'question_id':'', 'response_content':''}
receive: {'sucess':bool, 'msg':''}
```

- related modify response

```
/quesitons/related/modify_response
send: {'question_id':'', 'response_id':'', 'response_content':''}
receive: {'sucess':bool, 'msg':''}
```

- TuanWei, QA, get all questions, same as above
- TuanWei, QA, set question as common

```
/qa/add
send: {'question_id':''}
receive: {'sucess':bool, 'msg':''}

/qa/del
send: {'question_id':''}
receive: {'sucess':bool, 'msg':''}

/qa/top
send: {'question_id':''}
receive: {'sucess':bool, 'msg':''}
```
*back-end verify question property*

- TODO: data statistics

```
/statistics/get
send: none
receive: {'item1':number, ...}
```

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

```
/auth/login
```

- Upload the several pictures and a suggest

```
/question/upload
```

- Like certain question

```
/like/question/QUESTION_ID
```

- Like certain response

```
/like/response/QUESTION_ID
```

- Dislike certain question

```
/dislike/question/RESPONSE_ID
```

- Dislike certain response

```
/dislike/response/RESPONSE_ID
```

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
用户id；用户名；角色名（英文）；密码；手机号；固定电话；邮箱；学号；

### 角色表（**非牵头小于等于无关**）
角色名（英文）；显示名字（中文）；通讯录修改；问题拒绝；问题回复；问题转交；问题分类；延长ddl；问题审核；收到问题总数；按时完成数；超时完成数；拒绝问题数；直接回复数；同学满意率；同学不满意率；
名字包括：学生，三个，其他部门；

### 问题表
（审核+分类时间超时如何处理？校办和总办各自拥有1h）
问题id；标题；内容；创建者id；问题类别（牵头部门）；状态（待审核、待分类、待解决、解决中（不可再分类）、审核再分类、审核延期、已解决、无效）；学生满意度；学生评价内容；问题创建位置；
问题创建时间；点赞数；ddl；处理时间1（校团委）；处理时间2（校办）；处理时间3（总办）；是否是常见问题；是否是常见问题置顶；主管部门意见；

### 问题图片表
问题id；图片路径；

### 非牵头部门表
问题id；一个部门id；

### 问题点赞表
用户id；一个问题id；

### 回复点赞表
用户id；一个回复id；

### 回复表（多个回复对应一个问题）
回复id；回复内容；问题id；回复时间；点赞数；回复者id；

