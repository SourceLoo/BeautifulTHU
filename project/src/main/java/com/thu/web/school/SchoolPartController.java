package com.thu.web.school;//school;

import com.thu.domain.*;
import com.thu.service.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import io.jsonwebtoken.Claims;

/**
 * Created by hetor on 16/12/2.
 */
@RestController
//如果都有误，那么就返回{'success':bool, 'msg':''}
public class SchoolPartController {


    @Autowired
    Jwt jwtService;
    @Autowired
    ResponseService responseService;
    @Autowired
    UserService userService;
    @Autowired
    RoleService roleService;
    @Autowired
    QuestionService questionService;

    TokenMap tokenMap = new TokenMap();

    public static final String xiaoban="xiaoban";
    public static final String zongban="zongban";
    private final String roleZB = "zongban";
    private final String roleXB = "xiaoban";
    private final String roleALL = "all";
//    @RequestMapping(value = "/test/login",method = RequestMethod.POST)
//    public  String Login(@RequestBody)

    public static final String Error_Msg="{\"success\":false,\"msg\":\"Invalid Token\"}";
    public static final String Erro_Parse="{\"success\":false,\"msg\":\"Invalid Question_id\"}";
    public static final String Erro_Responder="{\"success\":false,\"msg\":\"Invalid Responder\"}";
    public static final String Erro_Response="{\"success\":false,\"msg\":\"Invalid Responce\"}";
    public static final String Erro_Role="{\"success\":false,\"msg\":\"Invalid Role\"}";
    public static final String Erro_User="{\"success\":false,\"msg\":\"Invalid TUser\"}";
    public static final String Erro_DDL="{\"success\":false,\"msg\":\"Invalid DeadLine\"}";
    public static final String Erro_TIMESTAMP1="{\"success\":false,\"msg\":\"Invalid TIMESTAMP1\"}";
    public static final String Erro_TIMESTAMP2="{\"success\":false,\"msg\":\"Invalid TIMESTAMP2\"}";
    public static final String Erro_TIMESTAMP3="{\"success\":false,\"msg\":\"Invalid TIMESTAMP3\"}";
    public static final String Erro_Status="{\"success\":false,\"msg\":\"Wrong Question Status\"}";

    //判断是否是主责部分
    private boolean checkMain(String role){

        if (role.equals(roleXB)||role.equals(roleZB))
            return true;
        else
            return false;
    }




    private  boolean checkPermission(String token){
        String tokenRole, tokenName;
        try{
            Claims claims = jwtService.parseToken(token);
            tokenRole = claims.get("role").toString();
            tokenName = claims.get("uname").toString();
        }catch (Exception e){
            return false;
        }

        String checkToken = tokenMap.getToken(tokenName);
        if (checkToken == ""||checkToken==null||!checkToken.equals(token)){
            return false;
        }

        return true;
    }


    public boolean CheckToken(String token){



        return checkPermission(token);
    }
    public String getRole(String token){
        if(!CheckToken(token))
            return null;
        String tokenRole=null;
        try{
            Claims claims = jwtService.parseToken(token);
            tokenRole = claims.get("role").toString();
//            tokenName = claims.get("uname").toString();
        }
        catch (Exception e){
            return null;
        }
        return tokenRole;

    }
    public String getUsername(String token){
//        public static String getRole(String token){
        if(!CheckToken(token))
            return null;
        String tokenName=null;
        try{
            Claims claims = jwtService.parseToken(token);
//                tokenRole = claims.get("role").toString();
            tokenName = claims.get("uname").toString();
        }
        catch (Exception e){
            return null;
        }
        return tokenName;

//        }
    }


    //主责部门获取问题列表
//  return:  [{'question_id':'', 'created_time':'', 'timestamp1':'', 'timestamp2':'',
//            'timestamp3':'' 'status':'', 'resp_role':'',
//            'is_common':bool, 'content':'', 'delay_days':number, 'delay_reason':'',
//            'is_common_top':bool, 'reclassify_reason':'', 'created_location':'', 'likes':int}]
//    public static Map<String,String> token_role;
    public String Convert(Object o){
        if(o!=null)
            return o.toString();
        return null;
    }

    @RequestMapping(value = "/questions/get_all/{token:.+}",method = RequestMethod.POST)
    public String getMainQuestions(@RequestParam(name = "start") int start,@RequestParam(name="number") int number ,@PathVariable String token){

//        System.out.println(token);
//        if(true)
//            return "ok";
//        token_role.containsKey()
        if(!CheckToken(token))
            return Error_Msg;
        String role=getRole(token);
        if(role==null)
            return Erro_Role;
        List<Question> questions=new ArrayList<>();
        if(role.equals(xiaoban)){
            questions= questionService.getAllQuestions();          //questionRepository.getQuestions();
        }else if(role.equals(zongban)){
            questions=questionService.getAllQuestionsForRole(roleService.findByRole(role));  //questionRepository.getQuestionsbyRole(roleReposiroty.findRole(role));
        }else{
            questions=questionService.getQuestionForRelatedRole(roleService.findByRole(role));    //questionRepository.getQuestionbyRela(roleReposiroty.findRole(role));
//            return Erro_Role;
        }
        JSONArray ja=new JSONArray();

        for(int i=start;i<start+number&&i<questions.size();i++){
            Question question=questions.get(i);
            JSONObject result= new JSONObject();
            result.put("question_id",this.Convert(question.getQuestionId()));
            result.put("created_time",this.Convert(question.getCreatedTime()));//.toString());
            result.put("created_location",question.getCreatedLocation());
            result.put("timestamp1",this.Convert(question.getTimestamp1()));//.toString());
            result.put("timestamp2",this.Convert(question.getTimestamp2()));//.toString());
            result.put("timestamp3",this.Convert(question.getTimestamp3()));//.toString());
            result.put("status",question.getStatus().ordinal());


//            result.put("")
//            result.put("resp_role_name",question.getLeaderRole().getDisplayName());
            List<String> pic_name=new ArrayList<>();
            List<Pic> pics=question.getPics();
            if(pics!=null) {
                for (Pic pic : pics) {
                    pic_name.add(pic.getPath());
                }
            }
            result.put("pic_path",pic_name);

            List<String> role_role=new ArrayList<>();
            List<String> role_res_name=new ArrayList<>();

            //TODO:修改判定逻辑，使用transferRole
//            if(question.getTransferRole()==null||question.getStatus() ==Status.UNAPPROVED||(question.getDelayDays()!=null &&question.getDelayDays()>0 && question.getStatus() ==Status.DELAY)||question.getStatus()==Status.RECLASSIFY)
//            {
//                role_role.add(xiaoban);
//                role_res_name.add(roleService.findByRole(xiaoban).getDisplayName());
//            }else if ((role.equals(zongban)||role.equals(xiaoban))&&question.getStatus()==Status.UNCLASSIFIED){
//                Role forward_role=question.getTransferRole();
//                if(forward_role.getRole().equals(role)) {
//                    role_res_name.add(forward_role.getDisplayName());
//                    role_role.add(forward_role.getRole());
//                }
//            }
            Role transferRole = question.getTransferRole();
            if(transferRole != null)
            {
                role_role.add(transferRole.getRole());
                role_res_name.add(transferRole.getDisplayName());
            }

            Role lead_role=question.getLeaderRole();
            if(lead_role!=null){
                role_res_name.add(lead_role.getDisplayName());
                role_role.add(lead_role.getRole());
            }
//            role_role.add(lead_role.);
            List<Role> other_roles=question.getOtherRoles();
            if(other_roles.size() != 0){
                for(Role oother:other_roles){
                    role_res_name.add(oother.getDisplayName());
                    role_role.add(oother.getRole());
                }
            }



            result.put("resp_role",role_role);
            result.put("resp_role_name",role_res_name);

            result.put("title",question.getTitle());
            result.put("content",question.getContent());
            result.put("deadline",this.Convert(question.getDdl()));//.toString());

            result.put("likes",question.getLikes());
            result.put("opinion",question.getInstruction());
            result.put("is_common",question.getCommon());
            result.put("content",question.getContent());
            result.put("delay_days",question.getDelayDays());
            result.put("delay_reason",question.getDelayReason());
            result.put("is_common_top",question.getCommonTop());
            result.put("reclassify_reason",question.getReclassifyReason());
            result.put("created_location",question.getCreatedLocation());
            result.put("created_location",question.getLikes());
            EvaluationType type_eval=question.getEvaluationType();
            int score=0;
            if(type_eval==EvaluationType.UNSATISFIED)
                score=-1;
            else if(type_eval==EvaluationType.SATISFIED)
                score=1;
            result.put("student_score",score);
            result.put("student_comment",question.getEvaluationDetail());
            JSONArray ja_response=new JSONArray();
            List<Response> null_responses=question.getResponses();
            if(null_responses!=null) {
                for (Response response : null_responses) {
                    JSONObject jo = new JSONObject();
                    jo.put("response_id", response.getResponseId());
                    jo.put("response_content", response.getResponseContent());
                    jo.put("time", response.getRespondTime().toLocalDate());
                    ja_response.add(jo);
                }
            }
            result.put("responses",ja_response);
            ja.add(result);
        }
        return ja.toString();



//        TUser hah = userRepository.findUser("hah");
//        return "[{'question_id':'question_id', 'created_time':'created_time', 'timestamp1':'timestamp1', 'timestamp2':'timestamp2', 'timestamp3':'timestamp3' 'status':'status', 'resp_role':'resp_role',"+
//            "'is_common':false, 'content':'content', 'delay_days':3, 'delay_reason':'delay_reason'," +
//            "'is_common_top':false, 'reclassify_reason':'reclassify_reason', 'created_location':'created_location', 'likes':20}]";
    }

    //主责部门直接回复
    // {'success':bool, 'msg':''}
    @RequestMapping(value = "/questions/main/response/{token:.+}",method = RequestMethod.POST)
    public String MainResponse(@RequestParam(name = "question_id") String q_id,@RequestParam("response_content") String content,@PathVariable String token){
        if(!CheckToken(token))
            return Error_Msg;
        Long qid=Long.parseLong("-1");
        try{
            qid= Long.parseLong(q_id);
        }catch (Exception e){
            return Erro_Parse;
        }
        //产生一条回复
        String role=getRole(token);
        if(!role.equals(xiaoban))
            return Erro_Role;
//        if(role==null)
//            return Erro_Role;
        String username=getUsername(token);
        if(username==null)
            return Erro_User;
        TUser responder= userService.findUser(username);           //userRepository.findUserbyName(username);
        if(responder==null)
            return Erro_Responder;
        Response respon= responseService.respond(content,responder);//                 responseRepository.insertResponse(content,responder,new Date());
        if(respon==null)
            return Erro_Response;
        //将回复插入到问题中
        Boolean insertResponse= questionService.responsibleDeptRespond(qid,respon);      //questionRepository.responsebyMain(qid,respon,respon.getRespondTime());
        if(insertResponse) {
            Boolean updateRole= roleService.updateNumber(xiaoban,null,null,null,Long.parseLong("1"));
            if(!updateRole)
                return Erro_Role;

            return "{\"success\":true, \"msg\":\"main response ok\"}";
        }
        else
            return "{\"success\":false, \"msg\":\"main response failure\"}";
    }

    //主责部门直接拒绝
    //{'success':bool, 'msg':''}
    @RequestMapping(value = "/questions/main/reject/{token:.+}",method = RequestMethod.POST)
    public String MainReject(@RequestParam("question_id") String q_id,@PathVariable String token) {
        if(!CheckToken(token))
            return Error_Msg;
        Long qid=Long.parseLong("-1");
        try{
            qid= Long.parseLong(q_id);
        }catch (Exception e){
            return Erro_Parse;
        }
        String content = "问题未通过审核";
        Boolean reject_ok=   questionService.responsibleDeptReject(qid,content);                      //questionRepository.rejectbyMain(qid,content,new Date());
        if(reject_ok)
            return "{\"success\":true, \"msg\":\"main reject ok\"}";
        else
            return "{\"success\":false, \"msg\":\"main reject failure\"}";
    }

    //主责部门转发问题
    //{'success':bool, 'msg':''}
    @RequestMapping(value = "/questions/main/forward/{token:.+}",method = RequestMethod.POST)
    public String MainForward(@RequestParam("question_id")String q_id,@RequestParam("role") String for_role,@PathVariable String token) {
        if(!CheckToken(token))
            return Error_Msg;

        Long qid=Long.parseLong("-1");
        try{
            qid= Long.parseLong(q_id);
        }catch (Exception e){
            return Erro_Parse;
        }
        String role=getRole(token);
        if(role==null)
            return Erro_Role;
        Role forword_role=   roleService.findByRole(for_role);        //roleReposiroty.findRole(for_role);
        if(forword_role==null)
            return Erro_Role;
        Boolean transfer_ok=  questionService.transferQuestion(qid,forword_role);            //questionRepository.transferbyMain(qid,forword_role,role,new Date());
        if(transfer_ok) {
            //更新forword_role表的信息
            //更新timestamp1
            if(role.equals(xiaoban)) {
                Boolean setTimeStamp1 = questionService.updateTimestamp(qid, LocalDateTime.now(), null, null);
                if (!setTimeStamp1)
                    return Erro_TIMESTAMP1;
            }else{
                return Erro_Role;
            }

            return "{\"success\":true, \"msg\":\"main transfer ok\"}";
        }
        else
            return "{\"success\":false, \"msg\":\"main transfer failure\"}";
    }


//    @RequestMapping(value = "/questions/main/delay",method = RequestMethod.POST)
//    public String MainDelay(@RequestParam("question_id") String q_id){
//        return "";
//    }

    //主责部门的对问题分类
    //{'success':bool, 'msg':''}
    @RequestMapping(value = "/questions/main/classify/{token:.+}",method = RequestMethod.POST)
    public String MainClassify(@RequestParam("question_id") String q_id,
                               @RequestParam("leader_role") String lead_role,
                               @RequestParam("other_roles") String other_roles,
                               @RequestParam("deadline") String ddl,
                               @RequestParam("opinion") String opinion,@PathVariable String token)
	{
        if(!CheckToken(token))
            return Error_Msg;
        Long qid=Long.parseLong("-1");
        try{
            qid= Long.parseLong(q_id);
        }catch (Exception e){
            return Erro_Parse;
        }
        String role=getRole(token);
        if(role==null)
            return Erro_Role;
        Role leader= roleService.findByRole(lead_role);         //roleReposiroty.findRole(lead_role);
        if(leader==null)
            return Erro_Role;
        List<Role> others = new ArrayList<>();
        if (other_roles != "") {
            String[] _other_roles = other_roles.split(",");
            for(String role_str:_other_roles){
                Role oth= roleService.findByRole(role_str) ;   //.findRole(role_str);
                if(oth==null)
                    return Erro_Role;
                others.add(oth);
            }
        }
//        SimpleDateFormat sdf  =   new  SimpleDateFormat( "yyyy-MM-dd" );
//        Date date= new Date();
//        try {
//            date= sdf.parse(ddl);
//        } catch (ParseException e) {
//            return Erro_DDL;
//        }
        String[] timeArray=ddl.split("-");
        int[] times=new int[6];
        if(timeArray.length<3||timeArray.length>6)
            return Erro_DDL;
        else{
            for(int i=0;i<6;i++){
                if(i<timeArray.length){
                    try {
                        times[i] = Integer.parseInt(timeArray[i]);
                    }catch (Exception e){
                        return Erro_Parse;
                    }
                }else
                    times[i]=0;
            }
        }

        LocalDateTime ldt= LocalDateTime.now();
        try {
           ldt= LocalDateTime.of(times[0], times[1], times[2], times[3], times[4], times[5]);
        }catch (Exception e){
            return Erro_DDL+";"+Erro_Parse;
        }

        Boolean classfy_ok=  questionService.classifyQuestion(qid,leader,others,ldt,opinion);     // (qid,leader,others,date,opinion,role);           //questionRepository.classifybyMain(qid,leader,others,date,opinion,role,new Date());
        Boolean setTimeStamp=Boolean.FALSE;
        if(role.equals(xiaoban)){
            setTimeStamp=questionService.updateTimestamp(qid,LocalDateTime.now(),null,null);
            if(!setTimeStamp)
                return Erro_TIMESTAMP1;
        }else if(role.equals(zongban)){
            setTimeStamp=questionService.updateTimestamp(qid,null,LocalDateTime.now(),null);
            if(!setTimeStamp)
                return Erro_TIMESTAMP2;
        }else{
            return Erro_Role;
        }

        if(classfy_ok && setTimeStamp) {
            Boolean updateRole= roleService.updateNumber(lead_role,Long.parseLong("1"),null,null,null); //roleReposiroty.updateReceivedNumber(lead_role,Long.parseLong("1"));
            if(!updateRole)
                return Erro_Role;
            return "{\"success\":true, \"msg\":\"main classify ok\"}";
        }
        else
            return "{\"success\":false, \"msg\":\"main classify failure\"}";
    }

    //相关部门获取问题列表
//    [{'question_id':'', 'created_time':'', 'deadline':'',
//            'created_location':'', 'status':'', 'content':'', 'likes':number,
//            'is_read':bool, 'is_common':bool, 'is_common_top':bool}]
    @RequestMapping(value = "/questions/related/get_all/{token:.+}",method = RequestMethod.POST)
    public String getRelateQuestions(@RequestParam(name = "start") int start,@RequestParam(name="number") int number ,@PathVariable String token){
        if(!CheckToken(token))
            return Error_Msg;

        String role=getRole(token);
        if(role==null)
            return Erro_Role;
        Role rela_role=roleService.findByRole(role);
        if(null==rela_role)
            return Erro_Role;
        List<Question> questions=questionService.getQuestionForRelatedRole(rela_role);         //questionRepository.getQuestionbyRela(rela_role);

        JSONArray ja=new JSONArray();

        for(int i=start;i<start+number&&i<questions.size();i++){
            Question question=questions.get(i);
            JSONObject result= new JSONObject();
            result.put("question_id",question.getQuestionId().toString());
            result.put("created_time",question.getCreatedTime().toString());
            result.put("timestamp1",question.getTimestamp1().toString());
            result.put("timestamp2",question.getTimestamp2().toString());
            result.put("timestamp3",question.getTimestamp3().toString());
            result.put("status",question.getStatus().toString());
            result.put("resp_role",question.getLeaderRole().getRole());
            result.put("is_common",question.getCommon());
            result.put("content",question.getContent());
            result.put("delay_days",question.getDelayDays());
            result.put("delay_reason",question.getDelayReason());
            result.put("is_common_top",question.getCommonTop());
            result.put("reclassify_reason",question.getReclassifyReason());
            result.put("created_location",question.getCreatedLocation());
            result.put("likes",question.getLikes());
            ja.add(result);
        }
        return ja.toString();
//        return "[{'question_id':'question_id', 'created_time':'created_time', 'deadline':'deadline',"+
//            "'created_location':'created_location', 'status':'', 'content':'content', 'likes':23,"+
//            "'is_read':false, 'is_common':false, 'is_common_top':false}]";
    }

    //相关部门获取具体信息
//    {'opinion':'', 'pic_path':['',''],
//        'responses':[{'response_id':'', 'response_content':''}]}
    @RequestMapping(value = "/questions/related/get_detail/{token:.+}",method = RequestMethod.POST)
    public String getRelaQueDetail(@RequestParam(name="question_id") String q_id,@PathVariable String token){
        if(!CheckToken(token))
            return Error_Msg;
        Long qid=Long.parseLong("-1");
        try{
            qid= Long.parseLong(q_id);
        }catch (Exception e){
            return Erro_Parse;
        }

//        String role=getRole(token);
//        List<Question> questions=null;
        Question question= questionService.getQuestionDetail(qid);//       questionRepository.getQuestionbyId(qid);
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("opinion",question.getInstruction());

        List<Pic> pics=question.getPics();
        List<String> pics_path=new ArrayList<>();
        for(Pic pic:pics){
            pics_path.add(pic.getPath());
        }
        jsonObject.put("pic_path",pics_path);
        JSONArray ja_response=new JSONArray();
        for(Response response:question.getResponses()){
            JSONObject jo=new JSONObject();
            jo.put("response_id",response.getResponseId());
            jo.put("response_content",response.getResponseContent());
            ja_response.add(jo);
        }
        jsonObject.put("responses",ja_response);
        return jsonObject.toString();
//        return "{'opinion':'opinion', 'pic_path':['pic_path_0','pic_path_1'],"+
//        "'responses':[{'response_id':'response_id', 'response_content':'response_content'}]}";
    }

    //相关部门申请重分类
//    {'success':bool, 'msg':''}
    @RequestMapping(value = "/questions/related/reclassify/{token:.+}",method = RequestMethod.POST)
    public String RelaReclassify(@RequestParam("question_id") String q_id,@RequestParam("reclassify_reason") String reason,@PathVariable String token){
        if(!CheckToken(token))
            return Error_Msg;
        Long qid=Long.parseLong("-1");
        try{
            qid= Long.parseLong(q_id);
        }catch (Exception e){
            return Erro_Parse;
        }
        Role tuan= roleService.findByRole(xiaoban);
        String cur_role=getRole(token);
        if(cur_role==null)
            return Erro_Role;
        if(null==tuan)
            return Erro_Role;
        Boolean require_reclassify= questionService.applyReclassifyQuestion(qid,reason,tuan);  //questionRepository.reclassifybyRela(qid,reason,tuan);
        if(require_reclassify) {
            Boolean updateRole =  roleService.updateNumber(cur_role,Long.parseLong("-1"),null,null,null);  //roleReposiroty.updateReceivedNumber(cur_role, Long.parseLong("-1"));
            if (!updateRole)
                return Erro_Role;
            return "{\"success\":true, \"msg\":\"require have been send successfully\"}";
        }
        else
            return "{\"success\":false, \"msg\":\"require have not been send\"}";
    }

//    @RequestMapping(value = "/questions/related/delay",method = RequestMethod.POST)
//    public String RelaDelay(@RequestParam("question_id") String q_id,@RequestParam("delay_reason") String reason,@RequestParam("delay_days") int days){
//        return "";
//    }

    //相关部门的回复（包括追加回复）
//    {'success':bool, 'msg':''}
    @RequestMapping(value = "/questions/related/response/{token:.+}",method = RequestMethod.POST)
    public String RalaResponse(@RequestParam("question_id") String q_id,@RequestParam("response_content") String r_content,@PathVariable String token){
        if(!CheckToken(token))
            return Error_Msg;
        Long qid=Long.parseLong("-1");
        try{
            qid= Long.parseLong(q_id);
        }catch (Exception e){
            return Erro_Parse;
        }

        Status status_qu=questionService.getQuestionDetail(qid).getStatus();
        if(status_qu!=Status.UNSOLVED&&status_qu!=Status.SOLVING&&status_qu!=Status.SOLVED){
            return Erro_Status;
        }
        List<Response> has_responses= questionService.getQuestionDetail(qid).getResponses();         //questionRepository.getQuestionbyId(qid).getResponses();
        LocalDateTime ddl=questionService.getQuestionDetail(qid).getDdl();      //getQuestionDetail(qid).getDdl();
        //生成一条回复
        String username=getUsername(token);
        if(username==null)
            return Erro_User;
        String role=getRole(token);
        if(role==null)
            return Erro_Role;
        TUser responder=  userService.findUser(username);     //userRepository.findUserbyName(username);
        if(responder==null)
            return Erro_Responder;
        Response respon= responseService.respond(r_content,responder);           //responseRepository.insertResponse(r_content,responder,new Date());
        if(respon==null)
            return Erro_Response;
        //将回复插入到问题中
        Boolean insertResponse= questionService.responsibleDeptRespond(qid,respon);     //questionRepository.responsebyRela(qid,respon);
        if(insertResponse) {
            if(has_responses.size()==0) {
                if(respon.getRespondTime().compareTo(ddl)>0)
                {
                    Boolean updateRole = roleService.updateNumber(role,null,null,Long.parseLong("1"),null); //roleReposiroty.updateOvertimeNumber(role, Long.parseLong("1"));
                    if (!updateRole)
                        return Erro_Role;
                }else{
                    Boolean updateRole = roleService.updateNumber(role,null,Long.parseLong("1"),null,null);
                    if (!updateRole)
                        return Erro_Role;
                }

            }
            return "{\"success\":true, \"msg\":\"respond successfully\"}";
        }
        else
            return "{\"success\":false, \"msg\":\"respond unsuccessfully\"}";
    }

    //相关部门修改回复
//    {'success':bool, 'msg':''}
    @RequestMapping(value = "/questions/related/modify_response/{token:.+}",method = RequestMethod.POST)
    public String ModifyResponse(@RequestParam("question_id") String q_id,@RequestParam("response_id") String r_id,@RequestParam("response_content") String r_content,@PathVariable String token){
        if(!CheckToken(token))
            return Error_Msg;
        Long qid=Long.parseLong("-1");
        try{
            qid= Long.parseLong(q_id);
        }catch (Exception e){
            return Erro_Parse;
        }
        Long rid=Long.parseLong("-1");
        try{
            rid= Long.parseLong(r_id);
        }catch (Exception e){
            return Erro_Parse;
        }
        Status status_qu=questionService.getQuestionDetail(qid).getStatus();
        if(status_qu!=Status.SOLVING&&status_qu!=Status.SOLVED){
            return Erro_Status;
        }

        Boolean modify_ok=  responseService.editResponse(rid,r_content);      //responseRepository.modifyResponse(rid,r_content,new Date());
        if(modify_ok)
            return "{\"success\":true, \"msg\":\"modify response successfully\"}";
        else
            return "{\"success\":false, \"msg\":\"modify response unsuccessfully\"}";

    }

}
