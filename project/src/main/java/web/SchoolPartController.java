package web;

import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by hetor on 16/12/2.
 */
@RestController
public class SchoolPartController {




    @RequestMapping(value = "/test/login",method = RequestMethod.POST)
    public  String Login(@RequestBody)

    //主责部门获取问题列表
//  return:  [{'question_id':'', 'created_time':'', 'timestamp1':'', 'timestamp2':'',
//            'timestamp3':'' 'status':'', 'resp_role':'',
//            'is_common':bool, 'content':'', 'delay_days':number, 'delay_reason':'',
//            'is_common_top':bool, 'reclassify_reason':'', 'created_location':'', 'likes':int}]
    @RequestMapping(value = "/questions/main/get_all",method = RequestMethod.POST)
    public String getMainQuestions(@RequestParam(name = "start") int start,@RequestParam(name="number") int number ){
        return "";

    }

    //主责部门获取具体问题
//    {'opinion':'', 'pic_path':['',''],
//        'responses':[{'response_id':'', 'response_content':''}]}
    @RequestMapping(value = "/questions/main/get_detail",method = RequestMethod.POST)
    public String getMainQueDetail(@RequestParam(name="question_id") String q_id){
        return "sfsf";
    }

    //主责部门直接回复
    // {'sucess':bool, 'msg':''}
    @RequestMapping(value = "/questions/main/response",method = RequestMethod.POST)
    public String MainResponse(@RequestParam(name = "question_id") String q_id,@RequestParam("response_content") String content){
        return "";
    }

    //主责部门直接拒绝
    //{'sucess':bool, 'msg':''}
    @RequestMapping(value = "/questions/main/reject",method = RequestMethod.POST)
    public String MainReject(@RequestParam("question_id") String q_id,@RequestParam("response_content") String content) {
     return "";
    }


//    @RequestMapping(value = "/questions/main/delay",method = RequestMethod.POST)
//    public String MainDelay(@RequestParam("question_id") String q_id){
//        return "";
//    }

    //主责部门的对问题分类
    //{'sucess':bool, 'msg':''}
    @RequestMapping(value = "/quesitons/main/classify",method = RequestMethod.POST)
    public String MainClassify(@RequestParam("question_id") String q_id,
                               @RequestParam("leader_role") String lead_role,
                               @RequestParam("other_roles") List<String> other_roles,
                               @RequestParam("deadline") String ddl,
                               @RequestParam("opinion") String opinion){
        return "";
    }

    //相关部门获取问题列表
//    [{'question_id':'', 'created_time':'', 'deadline':'',
//            'created_location':'', 'status':'', 'content':'', 'likes':number,
//            'is_read':bool, 'is_common':bool, 'is_common_top':bool}]
    @RequestMapping(value = "/questions/related/get_all",method = RequestMethod.POST)
    public String getRelateQuestions(@RequestParam(name = "start") int start,@RequestParam(name="number") int number ){
        return "";
    }

    //相关部门获取具体信息
//    {'opinion':'', 'pic_path':['',''],
//        'responses':[{'response_id':'', 'response_content':''}]}
    @RequestMapping(value = "/questions/related/get_detail",method = RequestMethod.POST)
    public String getRelaQueDetail(@RequestParam(name="question_id") String q_id){
        return "";
    }

    //相关部门申请重分类
//    {'sucess':bool, 'msg':''}
    @RequestMapping(value = "/quesitons/related/reclassify",method = RequestMethod.POST)
    public String RelaReclassify(@RequestParam("question_id") String q_id,@RequestParam("reclassify_reason") String reason){
        return "";
    }

//    @RequestMapping(value = "/quesitons/related/delay",method = RequestMethod.POST)
//    public String RelaDelay(@RequestParam("question_id") String q_id,@RequestParam("delay_reason") String reason,@RequestParam("delay_days") int days){
//        return "";
//    }

    //相关部门的回复（包括追加回复）
//    {'sucess':bool, 'msg':''}
    @RequestMapping(value = "/quesitons/related/response",method = RequestMethod.POST)
    public String RalaResponse(@RequestParam("question_id") String q_id,@RequestParam("response_content") String r_content){
        return "";
    }

    //相关部门修改回复
//    {'sucess':bool, 'msg':''}
    @RequestMapping(value = "/quesitons/related/modify_response",method = RequestMethod.POST)
    public String ModifyResponse(@RequestParam("question_id") String q_id,@RequestParam("response_id") String r_id,@RequestParam("response_content") String r_content){
        return "";
    }

}
