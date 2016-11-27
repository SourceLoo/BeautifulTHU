# BeautifulTHU
##用户表
（部门可以多个账号，一个账号只能同时在线一个）
用户id；用户名；密码；手机号；邮箱；学号；

##角色表（**非牵头小于等于无关**）
名字；权限等级；通讯录修改；问题拒绝；问题回复；问题转交；问题分类；延长ddl；回答审核；收到问题总数；按时完成数；超时完成数；拒绝问题数；？？？直接回复数；
名字包括：学生，三个，其他部门；

（附议=点赞？追加回复）
##问题表
（审核+分类时间超时如何处理？是否保存超时的历史？校办和总办共享还是各自拥有1h？）
问题id；标题；图片；内容；创建者id；问题类别（牵头部门）；状态（待分类、待解决、已解决、无效）；学生评价；
问题创建时间；点赞数；ddl；？？？转交时间1；？？？转交时间2；

##非牵头部门表
问题id；一个部门id；

##问题点赞表
用户id；一个问题id；

##回复点赞表
用户id；一个回复id；

##回复表（多个回复对应一个问题）
回复id；回复内容；问题id；回复时间；点赞数；回复者id；

##常见问题表
问题id；

##分工：
李元丙：数据操作权限，逻辑判断函数；

袁海涛：*分类问题*、下放问题、回复和追加回复

赵一峰：数据统计、审核、时间逻辑、QA

鲁源泉：学生端后台（获取进度、写入评价、上传图片）

孙佶：学生端前端，逻辑、界面

陈振寰：部门端前端，逻辑、界面

# API:
## For Students:
### Get
/question/all/PART_NUM : Getting all the question that this user can view by part.
/response/QUESTION_ID : Getting response of certain question.

### Post
/auth/login : Get permission to login from backend.
/question/upload : Upload the several pictures and a suggest
/like/question/QUESTION_ID : Like certain question
/like/response/QUESTION_ID : Like certain question
/dislike/question/RESPONSE_ID : Dislike certain question
/dislike/response/RESPONSE_ID : Dislike certain question
