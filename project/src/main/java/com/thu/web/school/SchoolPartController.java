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

    public  static final String tuanwei="tuanwei";
    public static final String xiaoban="xiaoban";
    public static final String zongban="zongban";
    private final String roleTW = "tuanwei";
    private final String roleZB = "zongban";
    private final String roleXB = "xiaoban";
    private final String roleALL = "all";
//    @RequestMapping(value = "/test/login",method = RequestMethod.POST)
//    public  String Login(@RequestBody)

    public static final String Error_Msg="{'success':false,'msg':'Invalid Token'}";
    public static final String Erro_Parse="{'success':false,'msg':'Invalid Question_id'}";
    public static final String Erro_Responder="{'success':false,'msg':'Invalid Responder'}";
    public static final String Erro_Response="{'success':false,'msg':'Invalid Responce'}";
    public static final String Erro_Role="{'success':false,'msg':'Invalid Role'}";
    public static final String Erro_User="{'success':false,'msg':'Invalid User'}";
    public static final String Erro_DDL="{'success':false,'msg':'Invalid DeadLine'}";
    public static final String Erro_TIMESTAMP1="{'success':false,'msg':'Invalid TIMESTAMP1'}";
    public static final String Erro_TIMESTAMP2="{'success':false,'msg':'Invalid TIMESTAMP2'}";
    public static final String Erro_TIMESTAMP3="{'success':false,'msg':'Invalid TIMESTAMP3'}";


    //判断是否是主责部分
    private boolean checkMain(String role){
        if (role.equals(roleTW)||role.equals(roleXB)||role.equals(roleZB))
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
    @RequestMapping(value = "/questions/main/get_all/{token}",method = RequestMethod.POST)
    public String getMainQuestions(@RequestParam(name = "start") int start,@RequestParam(name="number") int number ,@PathVariable String token){

//        token_role.containsKey()
        if(!CheckToken(token))
            return Error_Msg;
        String role=getRole(token);
        if(role==null)
            return Erro_Role;
        List<Question> questions=null;
        if(role.equals(tuanwei)){
            questions= questionService.getAllQuestions();          //questionRepository.getQuestions();
        }else if(role.equals(xiaoban)||role.equals(zongban)){
            questions=questionService.getAllQuestionsForRole(roleService.findByRole(role));  //questionRepository.getQuestionsbyRole(roleReposiroty.findRole(role));
        }else{
            //questions=questionService.getQuestionForRelatedRole(roleService.findByRole(role));    //questionRepository.getQuestionbyRela(roleReposiroty.findRole(role));
            return Erro_Role;
        }
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
            result.put("created_location",question.getLikes());
            ja.add(result);
        }
        return ja.toString();



//        User hah = userRepository.findUser("hah");
//        return "[{'question_id':'question_id', 'created_time':'created_time', 'timestamp1':'timestamp1', 'timestamp2':'timestamp2', 'timestamp3':'timestamp3' 'status':'status', 'resp_role':'resp_role',"+
//            "'is_common':false, 'content':'content', 'delay_days':3, 'delay_reason':'delay_reason'," +
//            "'is_common_top':false, 'reclassify_reason':'reclassify_reason', 'created_location':'created_location', 'likes':20}]";
    }

    //主责部门获取具体问题
//    {'opinion':'', 'pic_path':['',''],
//        'responses':[{'response_id':'', 'response_content':''}]}
    @RequestMapping(value = "/questions/main/get_detail/{token}",method = RequestMethod.POST)
    public String getMainQueDetail(@RequestParam(name="question_id") String q_id,@PathVariable String token){
        if(!CheckToken(token))
            return Error_Msg;
        Long qid=Long.parseLong("-1");
        try{
            qid= Long.parseLong(q_id);
        }catch (Exception e){
            return Erro_Parse;
        }
        if(!checkMain(getRole(token)))
            return Erro_Role;
//        String role=getRole(token);
//        List<Question> questions=null;
        Question question= questionService.getQuestionDetail(qid);               //questionRepository.getQuestionbyId(qid);
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

    //主责部门直接回复
    // {'success':bool, 'msg':''}
    @RequestMapping(value = "/questions/main/response/{token}",method = RequestMethod.POST)
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
        if(!role.equals(tuanwei))
            return Erro_Role;
//        if(role==null)
//            return Erro_Role;
        String username=getUsername(token);
        if(username==null)
            return Erro_User;
        User responder= userService.findUser(username);           //userRepository.findUserbyName(username);
        if(responder==null)
            return Erro_Responder;
        Response respon= responseService.respond(content,responder);//                 responseRepository.insertResponse(content,responder,new Date());
        if(respon==null)
            return Erro_Response;
        //将回复插入到问题中
        Boolean insertResponse= questionService.responsibleDeptRespond(qid,respon);      //questionRepository.responsebyMain(qid,respon,respon.getRespondTime());
        if(insertResponse) {
            Boolean updateRole= roleService.updateNumber(tuanwei,null,null,null,Long.parseLong("1"));     //updateDirectRespondNumber(tuanwei,Long.parseLong("1"));
            if(!updateRole)
                return Erro_Role;

            return "{'success':true, 'msg':'main response ok'}";
        }
        else
            return "{'success':false, 'msg':'main response failure'}";
    }

    //主责部门直接拒绝
    //{'success':bool, 'msg':''}
    @RequestMapping(value = "/questions/main/reject/{token}",method = RequestMethod.POST)
    public String MainReject(@RequestParam("question_id") String q_id,@RequestParam("response_content") String content,@PathVariable String token) {
        if(!CheckToken(token))
            return Error_Msg;
        Long qid=Long.parseLong("-1");
        try{
            qid= Long.parseLong(q_id);
        }catch (Exception e){
            return Erro_Parse;
        }
        Boolean reject_ok=   questionService.responsibleDeptReject(qid,content);                      //questionRepository.rejectbyMain(qid,content,new Date());
        if(reject_ok)
            return "{'success':true, 'msg':'main reject ok'}";
        else
            return "{'success':false, 'msg':'main reject failure'}";
    }

    //主责部门转发问题
    //{'success':bool, 'msg':''}
    @RequestMapping(value = "/questions/main/forward/{token}",method = RequestMethod.POST)
    public String MainForward(@RequestParam("question_id")String q_id,@RequestParam("forward") String for_role,@PathVariable String token) {
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
            Boolean setTimeStamp1=questionService.updateTimestamp(qid,LocalDateTime.now(),null,null);
            if(!setTimeStamp1)
                return Erro_TIMESTAMP1;

            return "{'success':true, 'msg':'main transfer ok'}";
        }
        else
            return "{'success':false, 'msg':'main transfer failure'}";
    }


//    @RequestMapping(value = "/questions/main/delay",method = RequestMethod.POST)
//    public String MainDelay(@RequestParam("question_id") String q_id){
//        return "";
//    }

    //主责部门的对问题分类
    //{'success':bool, 'msg':''}
    @RequestMapping(value = "/quesitons/main/classify/{token}",method = RequestMethod.POST)
    public String MainClassify(@RequestParam("question_id") String q_id,
                               @RequestParam("leader_role") String lead_role,
                               @RequestParam("other_roles") List<String> other_roles,
                               @RequestParam("deadline") String ddl,
                               @RequestParam("opinion") String opinion,@PathVariable String token){
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
        List<Role> others=new ArrayList<>();

        for(String role_str:other_roles){
            Role oth= roleService.findByRole(role_str) ;   //.findRole(role_str);
            if(oth==null)
                return Erro_Role;
            others.add(oth);
        }
        SimpleDateFormat sdf  =   new  SimpleDateFormat( " yyyy-MM-dd HH:mm:ss " );
        Date date= new Date();
        try {
            date= sdf.parse(ddl);
        } catch (ParseException e) {
            return Erro_DDL;
        }
        Boolean classfy_ok=  questionService.classifyQuestion(qid,leader,others,date,opinion);     // (qid,leader,others,date,opinion,role);           //questionRepository.classifybyMain(qid,leader,others,date,opinion,role,new Date());
        Boolean setTimeStamp=Boolean.FALSE;
        if(role.equals(tuanwei)){
            setTimeStamp=questionService.updateTimestamp(qid,LocalDateTime.now(),null,null);
        }else if(role.equals(xiaoban)){
            setTimeStamp=questionService.updateTimestamp(qid,null,LocalDateTime.now(),null);
        }else if(role.equals(zongban)){
            setTimeStamp=questionService.updateTimestamp(qid,null,null,LocalDateTime.now());
        }else{
            return Erro_Role;
        }

        if(classfy_ok && setTimeStamp) {
            Boolean updateRole= roleService.updateNumber(lead_role,Long.parseLong("1"),null,null,null); //roleReposiroty.updateReceivedNumber(lead_role,Long.parseLong("1"));
            if(!updateRole)
                return Erro_Role;
            return "{'success':true, 'msg':'main classify ok'}";
        }
        else
            return "{'success':false, 'msg':'main classify failure'}";
    }

    //相关部门获取问题列表
//    [{'question_id':'', 'created_time':'', 'deadline':'',
//            'created_location':'', 'status':'', 'content':'', 'likes':number,
//            'is_read':bool, 'is_common':bool, 'is_common_top':bool}]
    @RequestMapping(value = "/questions/related/get_all/{token}",method = RequestMethod.POST)
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
    @RequestMapping(value = "/questions/related/get_detail/{token}",method = RequestMethod.POST)
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
    @RequestMapping(value = "/quesitons/related/reclassify/{token}",method = RequestMethod.POST)
    public String RelaReclassify(@RequestParam("question_id") String q_id,@RequestParam("reclassify_reason") String reason,@PathVariable String token){
        if(!CheckToken(token))
            return Error_Msg;
        Long qid=Long.parseLong("-1");
        try{
            qid= Long.parseLong(q_id);
        }catch (Exception e){
            return Erro_Parse;
        }
        Role tuan= roleService.findByRole(tuanwei);    //roleReposiroty.findRole(tuanwei);
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
            return "{'success':true, 'msg':'require have been send successfully'}";
        }
        else
            return "{'success':false, 'msg':'require have not been send'}";
    }

//    @RequestMapping(value = "/quesitons/related/delay",method = RequestMethod.POST)
//    public String RelaDelay(@RequestParam("question_id") String q_id,@RequestParam("delay_reason") String reason,@RequestParam("delay_days") int days){
//        return "";
//    }

    //相关部门的回复（包括追加回复）
//    {'success':bool, 'msg':''}
    @RequestMapping(value = "/quesitons/related/response/{token}",method = RequestMethod.POST)
    public String RalaResponse(@RequestParam("question_id") String q_id,@RequestParam("response_content") String r_content,@PathVariable String token){
        if(!CheckToken(token))
            return Error_Msg;
        Long qid=Long.parseLong("-1");
        try{
            qid= Long.parseLong(q_id);
        }catch (Exception e){
            return Erro_Parse;
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
        User responder=  userService.findUser(username);     //userRepository.findUserbyName(username);
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
            return "{'success':true, 'msg':'respond successfully'}";
        }
        else
            return "{'success':false, 'msg':'respond unsuccessfully'}";
    }

    //相关部门修改回复
//    {'success':bool, 'msg':''}
    @RequestMapping(value = "/quesitons/related/modify_response/{token}",method = RequestMethod.POST)
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

        Boolean modify_ok=  responseService.editResponse(rid,r_content);      //responseRepository.modifyResponse(rid,r_content,new Date());
        if(modify_ok)
            return "{'success':true, 'msg':'modify response successfully'}";
        else
            return "{'success':false, 'msg':'modify response unsuccessfully'}";

    }

}
