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

## Status Meaning

- 0-待审核

问题提出的初始状态，校团委下放后进入1-待分类，直接分类后进入2-待解决，直接回复后进入3-解决中，拒绝后进入7-无效。

- 1-待分类

等待校办或总办进行分类，校办下放给总办不改变状态，分类后进入2-待解决。

- 2-待解决

等待相关部门回复处理，回复后进入3-解决中，申请再分类、延期后分别进入4和5。

- 3-解决中

已回复完毕，等待学生评价，此时可以修改回复或者再添加回复，但不能申请再分类和延期。

- 4-申请重分类

等待校团委处理，如果同意再分类则进入1-待分类，可以进行和之前一样的分类操作，同意分类前相关部门不能操作可以查看。

- 5-申请延期

等待校团委处理，如果同意延期则返回2-待解决并延长期限，这之前相关部门不能操作。

- 6-已解决

学生已回复，负责部门可以视情况追加回复。

- 7-无效

无效的问题学生端不显示。

## API of School Part:

### Post
- Get `'role':'display_name'` pair. one for main-repsonsible, one for related.

```
/init/get_displayname
send: none
receive: [[{'role':'role1','label':'displayname1'}, ...{}], [{}...{}]
```

- Get permission to login from backend.

```
/auth/login
send: {'uname':'', 'passwd':''}
receive: {'token':'', 'role':''}
```

- logout.

```
/auth/logout
send: none
receive: {'success':bool, 'msg':''}
```

- Get personal info from **token**.

```
/info/get
send: none
receive: {'display_name':'', 'uname':'', 'resp_person':'', 'fixed_phone':'', 'mobile_phone':''}
```

- Set personal info.

```
/info/set
send: {'uname':'', 'resp_person':'', 'fixed_phone':'', 'mobile_phone':'', 'passwd':''}
receive: {'success':bool, 'msg':''}
```

- Get contact table content, only TuanWei can access uname&passwd.

```
/contact/get
send: none
receive: [{'display_name':'', 'uname':'', 'resp_person':'',
    'fixed_phone':'', 'mobile_phone':''}]
```

- Set contact table content, only TuanWei can modify.

```
/contact/set
send: {'display_name':'', 'uname':'', 'resp_person':'', 'fixed_phone':'',
    'mobile_phone':'', 'passwd':'', 'is_new':bool}
receive: {'success':bool, 'msg':''}
```

- Delete contact table content, only TuanWei can modify.

```
/contact/del
send: {'uname': 'tuanwei'}
receive: {'success':bool, 'msg':''}
```

- Get questions list. (multi parts?)

```
/questions/get_all
send: {'start':int, 'number':int}
receive: [{'question_id':'', 'created_time':'', 'created_location':'',
    'timestamp1':'', 'timestamp2':'', 'timestamp3':'', 'status':int,
    'resp_role':[''], 'resp_role_name':[''], 'pic_path':['',''],
    'title':'', 'content':'', 'pic_path':['',''], 'deadline':'',
    'is_common':bool, 'is_common_top':bool, 'likes':int,
    'reclassify_reason':'', 'delay_days':number, 'delay_reason':'',
    'opinion':'', 'responses':[{'id':'', 'content':'', 'time':''}],
    'student_score':1/-1/0, 'student_comment':'',
}]
```

*sort and make reclassify&delay on the top. some pairs should be hidden if not exist.*

- example for each status:

```
var questions_list = [{
    'question_id': '0',
    'created_time': 'Dec 11 2016 14:04:23',
    'created_location': 'FIT楼3-125',
    'status': 0, //UNAPPROVED
    'resp_role': ['tuanwei'],
    'resp_role_name': ['校团委'],
    'title': '问题0',
    'content': '这是一个问题',
    'pic_path': ['', ''],
    'is_common': false,
    'is_common_top': false,
    'likes': 5,
}, {
    'question_id': '1',
    'created_time': 'Dec 11 2016 14:04:23',
    'created_location': '六教6C300',
    'timestamp1': '',
    'status': 1, //UNCLASSIFIED
    'resp_role': ['xiaoban'],
    'resp_role_name': ['校办'],
    'title': '问题1',
    'content': '这是一个问题',
    'pic_path': ['', ''],
    'is_common': false,
    'is_common_top': false,
    'likes': 15,
}, {
    'question_id': '2',
    'created_time': 'Dec 11 2016 14:04:23',
    'created_location': '六教6C300',
    'timestamp1': '',
    'timestamp2': '',
    'status': 1, //UNCLASSIFIED
    'resp_role': ['zongban'],
    'resp_role_name': ['总办'],
    'title': '问题2',
    'content': '这是一个问题',
    'pic_path': ['', ''],
    'is_common': false,
    'is_common_top': false,
    'likes': 15,
}, {
    'question_id': '3',
    'created_time': 'Dec 11 2016 14:04:23',
    'created_location': '大礼堂',
    'timestamp1': '',
    'timestamp2': '',
    'timestamp3': '',
    'status': 2, //UNSOLVED
    'resp_role': ['related3', 'related1'],
    'resp_role_name': ['相关部门3', '相关部门1'],
    'title': '问题3',
    'content': '这是一个问题',
    'pic_path': ['', ''],
    'deadline': '12/22/2016',
    'is_common': false,
    'is_common_top': false,
    'likes': 25,
    'opinion': '意见opinion',
}, {
    'question_id': '4',
    'created_time': 'Dec 11 2016 14:04:23',
    'created_location': '大礼堂',
    'timestamp1': '',
    'timestamp2': '',
    'timestamp3': '',
    'status': 3, //SOLVING
    'resp_role': ['related3'],
    'resp_role_name': ['相关部门3'],
    'title': '问题4',
    'content': '这是一个问题',
    'pic_path': ['', ''],
    'deadline': '12/22/2016',
    'is_common': false,
    'is_common_top': false,
    'likes': 25,
    'opinion': '意见opinion',
    'responses': [{ 'id': '1', 'content': '回复回复1' }, ...]
}, {
    'question_id': '5',
    'created_time': 'Dec 11 2016 14:04:23',
    'created_location': '大礼堂',
    'timestamp1': '',
    'timestamp2': '',
    'timestamp3': '',
    'status': 4, //RECLASSIFY
    'resp_role': ['tuanwei', 'related3'],
    'resp_role_name': ['校团委', '相关部门3'],
    'title': '问题5',
    'content': '这是一个问题',
    'pic_path': ['', ''],
    'deadline': '12/22/2016',
    'is_common': true,
    'is_common_top': false,
    'reclassify_reason': '申请重新分类的理由，因为所以，科学道理',
    'likes': 25,
    'opinion': '意见opinion',
}, {
    'question_id': '6',
    'created_time': 'Dec 11 2016 14:04:23',
    'created_location': '大礼堂',
    'timestamp1': '',
    'timestamp2': '',
    'timestamp3': '',
    'status': 5, //DELAY
    'resp_role': ['tuanwei', 'related3'],
    'resp_role_name': ['校团委', '相关部门3'],
    'title': '问题6',
    'content': '这是一个问题',
    'pic_path': ['', ''],
    'deadline': '12/22/2016',
    'delay_days': 5,
    'delay_reason': '申请延期的理由，因为所以，科学道理',
    'is_common': true,
    'is_common_top': false,
    'likes': 25,
    'opinion': '意见opinion',
}, {
    'question_id': '7',
    'created_time': 'Dec 11 2016 14:04:23',
    'created_location': '大礼堂',
    'timestamp1': '',
    'timestamp2': '',
    'timestamp3': '',
    'status': 6, //SOLVED
    'resp_role': ['related3'],
    'resp_role_name': ['相关部门3'],
    'title': '问题7',
    'content': '这是一个问题',
    'pic_path': ['', ''],
    'is_common': true,
    'is_common_top': true,
    'likes': 25,
    'opinion': '意见opinion',
    'responses': [{
        'id': '1',
        'content': '回复回复1'
    }, {
        'id': '2',
        'content': '回复回复2'
    }],
    'student_score': 1,
    'student_comment': '满意满意满意'
}, {
    'question_id': '8',
    'created_time': 'Dec 11 2016 14:04:23',
    'created_location': '大礼堂',
    'timestamp1': '',
    'timestamp2': '',
    'timestamp3': '',
    'status': 7, //INVALID
    'resp_role': ['tuanwei'],
    'resp_role_name': ['校团委'],
    'title': '问题8',
    'content': '这是一个问题',
    'pic_path': ['', ''],
    'is_common': false,
    'is_common_top': false,
    'likes': 25,
}];
```

- main response

```
/questions/main/response
send: {'question_id':'', 'response_content':''}
receive: {'success':bool, 'msg':''}
```

- main reject

```
/questions/main/reject
send: {'question_id':''}
receive: {'success':bool, 'msg':''}
```

- main forward

```
/questions/main/forward
send: {'question_id':'', 'forward':'role'}
receive: {'success':bool, 'msg':''}
```

- main reclassify

```
/quesitons/main/reclassify
send: {'question_id':'', 'agree':bool}
receive: {'success':bool, 'msg':''}
```


- main delay

```
/questions/main/delay
send: {'question_id':'', 'agree':bool}
receive: {'success':bool, 'msg':''}
```

*back-end verify status.*

- main classify

```
/quesitons/main/classify
send: {'question_id':'', 'leader_role':'', 'other_roles':[], 'deadline':'', 'opinion':''}
receive: {'success':bool, 'msg':''}
```

*back-end verify status.*

- related apply for reclassification

```
/quesitons/related/reclassify
send: {'question_id':'', 'reclassify_reason':''}
receive: {'success':bool, 'msg':''}
```

*back-end verify status & question category.*

- related apply for delay

```
/quesitons/related/delay
send: {'question_id':'', 'delay_reason':'', 'delay_days':int}
receive: {'success':bool, 'msg':''}
```

*back-end verify status.*

- related response

```
/quesitons/related/response
send: {'question_id':'', 'response_content':''}
receive: {'success':bool, 'msg':''}
```

- related modify response

```
/quesitons/related/modify_response
send: {'question_id':'', 'response_id':'', 'response_content':''}
receive: {'success':bool, 'msg':''}
```

- TuanWei, QA, get all questions, same as above
- TuanWei, QA, set question as common

```
/qa/add
send: {'question_id':''}
receive: {'success':bool, 'msg':''}

/qa/del
send: {'question_id':''}
receive: {'success':bool, 'msg':''}

/qa/top
send: {'question_id':''}
receive: {'success':bool, 'msg':''}

/qa/notop
send: {'question_id':''}
receive: {'success':bool, 'msg':''}
```
*back-end verify question property*

- data statistics

```
/statistics/get
send: none
receive:
    var statistics_list = [{
        'role': 'tuanwei',
        'display_name': '校团委',
        'question_num': 10,
        'intime_num': 5,
        'outdate_num': 5,
        'reject_num': 1,
        'response_num': 2,
    }, {
        'role': 'xiaoban',
        'display_name': '校办',
        'question_num': 10,
        'intime_num': 5,
        'outdate_num': 5,
    }, {
        'role': 'zongban',
        'display_name': '总办',
        'question_num': 10,
        'intime_num': 5,
        'outdate_num': 5,
    }, {
        'role': 'related3',
        'display_name': '相关部门3',
        'question_num': 10,
        'intime_num': 5,
        'outdate_num': 5,
    }, ...];
```

## API of Student Part:

### Get

- Getting all the question that this user can view by page part.

```json
GET: /question/all/?page_size={PAGE_SIZE}&page_num={PAGE_NUM}&state_condition={STATE_CONDITION}&depart_condition={DEPART_CONDITION}&order_type={ORDER_TYPE}&keywords={KEYWORDS}
{
  "question_list" : [
    {
        "question_id" : "QUESTION_ID",
        "question_title" : "QUESTION_TITLE",
        "question_content" : "QUESTION_CONTENT",
        "question_location" : "QUESTION_LOCATION",
        "like_num" : "like number"
    }
    ]
}
```


- Getting detail of certain question.

```json
GET: /question/?question_id={QUESTION_ID}
{
  "id" : "QUESTION_ID",
  "title" : "QUESTION_TITLE",
  "photo" : [
    {
        url : "IMG_URL"
    }
  ],
  "question_title" : "QUESTION_TITLE",
  "question_content" : "QUESTION_CONTENT",
  "creator_id" : "CREATOR_ID",
  "leader_department" : "DEPT",
  "status" : "STATUS",
  "reply_content" : "REPLY_CONTENT"
}
```

- Getting all the Q&A question that this user can view by page part.

```json
GET: /regular_question/all/?page_size={PAGE_SIZE}&page_num={PAGE_NUM}&state_condition={STATE_CONDITION}&depart_condition={DEPART_CONDITION}&order_type={ORDER_TYPE}&keywords={KEYWORDS}
{
  "question_list" : [
    {
        "question_id" : "QUESTION_ID",
        "question_title" : "QUESTION_TITLE",
        "question_content" : "QUESTION_CONTENT",
        "question_location" : "QUESTION_LOCATION",
        "like_num" : "like number"
    }
    ]
}
```

- Getting progress of certain classified question.

```json
Get: /new_classify/{QUESTION_ID}
{
    "leader_department": "DEPT",
    "upperbound_month": "MONTH",
    "upperbound_day": "DAY"
}
```

- Getting newly response of certain question.

```json
Get: /new_response/{QUESTION_ID}
{
    "leader_department": "DEPT",
    "question_title": "QUESTION_TITLE",
    "question_content": "QUESTION_CONTENT",
    "response_content": "RESPONSE_CONTENT",
    "response_id": "RESPONSE_ID"
}
```

- Getting append response of certain question.

```json
Get: /append_response/{QUESTION_ID}
{
    "leader_department": "DEPT",
    "question_title": "QUESTION_TITLE",
    "question_content": "QUESTION_CONTENT",
    "response_content": "RESPONSE_CONTENT",
    "response_id": "RESPONSE_ID"
}
```

- Getting all the responses of certain question.

```json
Get: /append_response/{QUESTION_ID}
{
    "leader_department": "DEPT",
    "question_title": "QUESTION_TITLE",
    "question_content": "QUESTION_CONTENT",
    "response_list" : [{
        "response_content": "RESPONSE_CONTENT",
        "response_id": "RESPONSE_ID"
    }]
}
```

- Getting all the departments.

```json
Get: /getDept/all
{
    "dept_list" : [{
        "name" : "DEPT_NAME",
        "id" : "DEPT_ID"
    }]
}
```

### Post

- Get permission to login from backend.

```json
POST: /auth/login
```

- Upload the several pictures

```json
POST: /question/upload_img
```

- Upload a question

```json
POST: /question/upload
{
    question_title: "QUESTION_TITLE",
    question_content: "QUESTION_CONTENT",
    location: "LOCATION"
}
send back:
{
    question_id: "QUESTION_ID",
    upperbound_month: "UPPER_YEAR",
    upperbound_day: "UPPER_MONTH"
}
```

- Like certain question

```json
POST: /like/question/
{
    question_id: "QUESTION_ID"
}
```

- Evaluate the response

```json
POST: /evaluate/{RESPONSE_ID}
{
    response_id: "RESPONSE_ID",
    evaluation: "EVALUATION",
    detail: "DETAIL"
}
```

### DELETE
- Dislike certain question

```json
DELETE: /dislike/question
{
    question_id: "QUESTION_ID"
}
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

问题id；标题；内容；创建者id；负责部门列表（牵头部门第一个）；状态（待审核、待分类、待解决、解决中（不可再分类）、审核再分类、审核延期、已解决、无效）；学生满意度；学生评价内容；问题创建位置；问题创建时间；点赞数；ddl；处理时间1（校团委）；处理时间2（校办）；处理时间3（总办）；是否是常见问题；是否是常见问题置顶；主管部门意见；

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

