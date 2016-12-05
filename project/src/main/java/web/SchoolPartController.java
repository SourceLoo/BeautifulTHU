package web;

import domain.User;
import domain.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Created by hetor on 16/12/2.
 */
@RestController
//如果都有误，那么就返回{'success':bool, 'msg':''}
public class SchoolPartController {

  @Autowired
  UserRepository userRepository;


//    @RequestMapping(value = "/test/login",method = RequestMethod.POST)
//    public  String Login(@RequestBody)

    public static String Error_Msg="{'success':false,'msg':'Invalid Token'}";
    public static boolean CheckToken(String token){
//        if(token_role.containsKey(token))
//            return true;
        return true;
    }

    //主责部门获取问题列表
//  return:  [{'question_id':'', 'created_time':'', 'timestamp1':'', 'timestamp2':'',
//            'timestamp3':'' 'status':'', 'resp_role':'',
//            'is_common':bool, 'content':'', 'delay_days':number, 'delay_reason':'',
//            'is_common_top':bool, 'reclassify_reason':'', 'created_location':'', 'likes':int}]
    public static Map<String,String> token_role;
    @RequestMapping(value = "/questions/main/get_all/{token}",method = RequestMethod.POST)
    public String getMainQuestions(@RequestParam(name = "start") int start,@RequestParam(name="number") int number ,@PathVariable String token){

//        token_role.containsKey()
        if(!CheckToken(token))
            return Error_Msg;

        
//        User hah = userRepository.findUser("hah");
        return "[{'question_id':'question_id', 'created_time':'created_time', 'timestamp1':'timestamp1', 'timestamp2':'timestamp2', 'timestamp3':'timestamp3' 'status':'status', 'resp_role':'resp_role',"+
            "'is_common':false, 'content':'content', 'delay_days':3, 'delay_reason':'delay_reason'," +
            "'is_common_top':false, 'reclassify_reason':'reclassify_reason', 'created_location':'created_location', 'likes':20}]";
    }

    //主责部门获取具体问题
//    {'opinion':'', 'pic_path':['',''],
//        'responses':[{'response_id':'', 'response_content':''}]}
    @RequestMapping(value = "/questions/main/get_detail/{token}",method = RequestMethod.POST)
    public String getMainQueDetail(@RequestParam(name="question_id") String q_id,@PathVariable String token){
        if(!CheckToken(token))
            return Error_Msg;
        return "{'opinion':'opinion', 'pic_path':['pic_path_0','pic_path_1'],"+
        "'responses':[{'response_id':'response_id', 'response_content':'response_content'}]}";
    }

    //主责部门直接回复
    // {'success':bool, 'msg':''}
    @RequestMapping(value = "/questions/main/response/{token}",method = RequestMethod.POST)
    public String MainResponse(@RequestParam(name = "question_id") String q_id,@RequestParam("response_content") String content,@PathVariable String token){
        if(!CheckToken(token))
            return Error_Msg;
        return "{'success':true, 'msg':'main response ok'}";
    }

    //主责部门直接拒绝
    //{'success':bool, 'msg':''}
    @RequestMapping(value = "/questions/main/reject/{token}",method = RequestMethod.POST)
    public String MainReject(@RequestParam("question_id") String q_id,@RequestParam("response_content") String content,@PathVariable String token) {
        if(!CheckToken(token))
            return Error_Msg;
        return "{'success':true, 'msg':'main reject ok'}";
    }

    //主责部门转发问题
    //{'success':bool, 'msg':''}
    @RequestMapping(value = "/questions/main/forward/{token}",method = RequestMethod.POST)
    public String MainForward(@RequestParam("question_id")String q_id,@RequestParam("forward") String for_role,@PathVariable String token) {
        if(!CheckToken(token))
            return Error_Msg;
        return "{'success':true, 'msg':'main transfer ok'}";
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
        return "{'success':true, 'msg':'main classify ok'}";
    }

    //相关部门获取问题列表
//    [{'question_id':'', 'created_time':'', 'deadline':'',
//            'created_location':'', 'status':'', 'content':'', 'likes':number,
//            'is_read':bool, 'is_common':bool, 'is_common_top':bool}]
    @RequestMapping(value = "/questions/related/get_all/{token}",method = RequestMethod.POST)
    public String getRelateQuestions(@RequestParam(name = "start") int start,@RequestParam(name="number") int number ,@PathVariable String token){
        if(!CheckToken(token))
            return Error_Msg;
        return "[{'question_id':'question_id', 'created_time':'created_time', 'deadline':'deadline',"+
            "'created_location':'created_location', 'status':'', 'content':'content', 'likes':23,"+
            "'is_read':false, 'is_common':false, 'is_common_top':false}]";
    }

    //相关部门获取具体信息
//    {'opinion':'', 'pic_path':['',''],
//        'responses':[{'response_id':'', 'response_content':''}]}
    @RequestMapping(value = "/questions/related/get_detail/{token}",method = RequestMethod.POST)
    public String getRelaQueDetail(@RequestParam(name="question_id") String q_id,@PathVariable String token){
        if(!CheckToken(token))
            return Error_Msg;
        return "{'opinion':'opinion', 'pic_path':['pic_path_0','pic_path_1'],"+
        "'responses':[{'response_id':'response_id', 'response_content':'response_content'}]}";
    }

    //相关部门申请重分类
//    {'success':bool, 'msg':''}
    @RequestMapping(value = "/quesitons/related/reclassify/{token}",method = RequestMethod.POST)
    public String RelaReclassify(@RequestParam("question_id") String q_id,@RequestParam("reclassify_reason") String reason,@PathVariable String token){
        if(!CheckToken(token))
            return Error_Msg;
        return "{'success':true, 'msg':'require have been send successfully'}";
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
        return "{'success':true, 'msg':'respond successfully'}";
    }

    //相关部门修改回复
//    {'success':bool, 'msg':''}
    @RequestMapping(value = "/quesitons/related/modify_response/{token}",method = RequestMethod.POST)
    public String ModifyResponse(@RequestParam("question_id") String q_id,@RequestParam("response_id") String r_id,@RequestParam("response_content") String r_content,@PathVariable String token){
        if(!CheckToken(token))
            return Error_Msg;

        return "{'success':true, 'msg':'modify response successfully'}";

    }

}
