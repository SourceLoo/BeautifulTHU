package com.thu.web.school;

import com.thu.domain.Role;
import com.thu.domain.User;
import com.thu.domain.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import net.sf.json.JSONException;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.thu.service.QuestionService;
import com.thu.service.ResponseService;
import com.thu.service.RoleService;
import com.thu.service.UserService;
import com.thu.web.school.Jwt;

import javax.websocket.server.PathParam;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;


@RestController
public class SchoolController{
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private final String loginErrorMsg = "{'success':false,'msg':'Invalid Name Or Passwd'}";
    private final String invalidInputMsg = "{'success':false,'msg':'Invalid Input'}";
    private final String invalidTokenMsg = "{'success':false,'msg':'Invalid Token'}";
    private final String userNotLogMsg = "{'success':false,'msg':'User Not Log In'}";
    private final String errorMsg = "{'success':false,'msg':'Fail'}";
    private final String successMsg = "{'success':true,'msg':'Done'}";
    //private final ArrayList<String> roles_TW = new ArrayList(Arrays.asList("tuanwei"));
    private final String roleTW = "tuanwei";
    private final String roleZB = "zongban";
    private final String roleXB = "xiaoban";
    private final String roleALL = "all";

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

    @Autowired
    UserRepository userRepository;

    TokenMap tokenMap = new TokenMap();

    private boolean checkPermission(String uname, String token, String role){
        String tokenRole, tokenName;
        try{
            Claims claims = jwtService.parseToken(token);
            tokenRole = claims.get("role").toString();
            tokenName = claims.get("uname").toString();
        }
        catch (Exception e){
            return false;
        }
        if (!tokenName.equals(uname)){
            return false;
        } else{
            String checkToken = tokenMap.getToken(uname);
            if (checkToken.equals("") || !checkToken.equals(token)){
                return false;
            }
        }
        if (!role.equals(roleALL) && !role.equals(tokenRole))
            return false;
        return true;
    }

    private boolean checkPermissionWithoutName(String token, String role){
        String tokenName, tokenRole;
        try{
            Claims claims = jwtService.parseToken(token);
            tokenRole = claims.get("role").toString();
            tokenName = claims.get("uname").toString();
        }
        catch (Exception e){
            return false;
        }
        String checkToken = tokenMap.getToken(tokenName);
        if (checkToken.equals("") || !checkToken.equals(token) || (!role.equals(roleALL) && !tokenRole.equals(role))){
            return false;
        }
        return true;
    }


    /* done
    *
    *
    * */
    @RequestMapping(value = "/init/get_displayname", method = RequestMethod.POST)
    @ResponseBody
    public String getDisplayname() throws JSONException{

        List<Role> mainRoleList = roleService.findMain();
        List<Role> relatedRoleList = roleService.findRelated();


        String ansMain = "";
        String ansRelated = "";
        JSONObject resultMain = new JSONObject();
        JSONObject resultRelated = new JSONObject();
        for (Role role:mainRoleList){
            resultMain = new JSONObject();

            resultMain.put("role",role.getRole());
            resultMain.put("label",role.getDisplayName());
            ansMain = ansMain + resultMain.toString();

        }
        for (Role role:relatedRoleList){
            resultRelated = new JSONObject();
            resultRelated.put("role",role.getRole());
            resultRelated.put("label", role.getDisplayName());
            ansRelated = ansRelated + resultRelated.toString();
            //resultRelated.put(role.getRole(), role.getDisplayName());
        }
        return "[[" + ansMain +"],["+ ansRelated +"]]";
    }

    /*  done
    * Get Permission to login from backend
    * param uname, passwd
    * return {token, role, resp_person, fixed_phone, mobile_phone}
    * */
    @RequestMapping(value = "/auth/login", method = RequestMethod.POST)
    @ResponseBody
    public String login(@RequestParam("uname") String uname,
                        @RequestParam("passwd") String passwd) throws JSONException{

        if (uname.equals("") || ! userService.containsUname(uname))
            return loginErrorMsg;
        User usr = userService.findUser(uname);

        if (!usr.checkPasswd(passwd))
            return loginErrorMsg;
        String rol = usr.getRole().getRole();
        String token;
        try {
            token = jwtService.creatToken(uname, rol);
        }
        catch (Exception e){
            return errorMsg;
        }
        tokenMap.setToken(uname, token);
        JSONObject result = new JSONObject();
        result.put("token", token);
        result.put("role", usr.getRole().getRole());
        result.put("resp_person", usr.getRole().getRespPerson());
        result.put("mobile_phone", usr.getMobileNumber());
        return result.toString();
    }

    /*  done
    *  every one can log out
    *  log out
    *  no param
    *  return {role, resp_person, fixed_phone, mobile_phone}
    * */
    @RequestMapping(value="/auth/logout",method =RequestMethod.POST)
    @ResponseBody
    public String logout(@RequestParam("token") String token)throws JSONException{
        System.out.println(token);
        if (!checkPermissionWithoutName(token, roleALL)){
            return invalidTokenMsg;
        }
        System.out.println("token");
        String tokenName;
        try{
            Claims claims = jwtService.parseToken(token);
            tokenName = claims.get("uname").toString();
        }
        catch (Exception e){
            return invalidTokenMsg;
        }
        System.out.println(tokenName);
        boolean success = tokenMap.removeUname(tokenName);
        if (success){
            JSONObject result = new JSONObject();
            User usr = userService.findUser(tokenName);
            result.put("role", usr.getRole().getRole());
            result.put("resp_person", usr.getRole().getRespPerson());
            result.put("fixed_phone", usr.getFixedNumber());
            result.put("mobile_phone", usr.getMobileNumber());
            return result.toString();
        }
        else
            return userNotLogMsg;
    }

    @RequestMapping(value ="/info/get", method = RequestMethod.POST)
    @ResponseBody
    public String getInfo(@RequestParam("token") String token) throws JSONException{
        String tokenName , tokenRole;
        if (!checkPermissionWithoutName(token, roleALL)){
            return invalidTokenMsg;
        }
        try{
            Claims claims = jwtService.parseToken(token);
            tokenName = claims.get("uname").toString();
            tokenRole = claims.get("role").toString();
        }
        catch (Exception e){
            return invalidTokenMsg;
        }
        JSONObject result = new JSONObject();
        User usr = userService.findUser(tokenName);
        result.put("uname", tokenName);
        result.put("resp_person", usr.getRole().getRespPerson());
        result.put("display_name", usr.getRole().getDisplayName());
        result.put("fixed_phone", usr.getFixedNumber());
        result.put("mobile_phone", usr.getMobileNumber());
        return result.toString();
    }

    /* done
    * set department information by himself
    * param uname, resp_person, fixed_phone, mobile_phone, passwd
    * return {success, msg}
    * */
    @RequestMapping(value ="/info/set", method = RequestMethod.POST)
    @ResponseBody
    public String setInfo(@RequestParam("token") String token,
                    @RequestParam("uname") String uname,
                   @RequestParam("resp_person") String resp_person,
                   @RequestParam("fixed_phone") String fixed_phone,
                   @RequestParam("mobile_phone") String mobile_phone,
                   @RequestParam("passwd") String passwd) throws JSONException {
        if (!checkPermission(uname, token, uname))
            return invalidTokenMsg;
        if (!userService.containsUname(uname)){
            return invalidInputMsg;
        }
        Role rol = userService.findUser(uname).getRole();
        if (null == rol){
            return invalidInputMsg;
        }
        boolean success = roleService.updateRole(rol.getRole(),resp_person, rol.getDisplayName());
        if (!success){
            return invalidInputMsg;
        }
        rol = roleService.findByRole(rol.getRole());
        success &= userService.updateUser(uname,mobile_phone,fixed_phone,rol,passwd);
        if (success) {
            return successMsg;
        }
        else {
            return errorMsg;
        }
    }
    /*
    *  done
     *  */
    @RequestMapping(value = "/contact/del", method = RequestMethod.POST)
    @ResponseBody
    public String delContact(@PathParam("token") String token,
                             @PathParam("uname") String uname) throws JSONException{
        if (!checkPermissionWithoutName( token, roleTW))
            return invalidTokenMsg;
        if (!userService.containsUname(uname)){
            return invalidInputMsg;
        }
        User usr = userService.findUser(uname);

        usr.setFixedNumber("");
        usr.setMobileNumber("");
        usr.getRole().setRespPerson("");
        try {
            userRepository.save(usr);
            return successMsg;
        } catch (Exception e) {
            return errorMsg;
        }
    }


    /* done
    * only TuanWei get contact
    * no param
    * return [{role, uname, resp_person, fixed_phone, mobile_phone},{}]
    * */

   @RequestMapping(value = "/contact/get", method = RequestMethod.POST)
    @ResponseBody
    public String getContact(@RequestParam("token") String token) throws JSONException{
       if (!checkPermissionWithoutName(token, roleTW))
           return invalidTokenMsg;
        List<User> userList = userService.findAll();
        String result = "[";
        boolean flag = true;
        for (User usr:userList) {
            JSONObject mem = new JSONObject();
            mem.put("display_name", usr.getRole().getDisplayName());
            mem.put("uname", usr.getUname());
            mem.put("resp_person", usr.getRole().getRespPerson());
            mem.put("fixed_phone", usr.getFixedNumber());
            mem.put("mobile_phone", usr.getMobileNumber());
            if (flag) {
                flag = false;
                result = result + mem.toString();
            }else{
                result = result + ',' + mem.toString();
            }
        }
        result = result + "]";
        return result;
    }


    /*  done
    * only Tuanwei set contact
    * param role, uname, resp_person, fixed_phone, mobile_phone, passwd, is_new
    * department(uname = role)
    * return success, msg
    * */
   @RequestMapping(value = "/contact/set", method = RequestMethod.POST)
    @ResponseBody
    public String setContact(@RequestParam("token") String token,
                             @RequestParam("display_name") String displayname,
                             @RequestParam("uname") String uname,
                             @RequestParam("resp_person") String resp_person,
                             @RequestParam("fixed_phone") String fixed_phone,
                             @RequestParam("mobile_phone") String mobile_phone,
                             @RequestParam("passwd") String passwd,
                             @RequestParam("is_new") boolean is_new) throws JSONException{
        if (!checkPermissionWithoutName( token, roleTW))
            return invalidTokenMsg;

        boolean success;
        if (is_new){
            success = roleService.insertRole(uname, resp_person, displayname);
            Role rol = roleService.findByRole(uname);
            success &= userService.insertUser(uname, mobile_phone, fixed_phone,"","", rol, passwd);
        } else{
            success = roleService.updateRole(uname, resp_person , displayname);
            Role rol = roleService.findByRole(uname);
            success &= userService.updateUser(uname, mobile_phone, fixed_phone, rol, passwd);
        }
        if (success)
            return successMsg;
        else
            return errorMsg;
    }


    /*
    * only tuanwei can set
    * param question_id
    * return {success, msg}
    * */
    @RequestMapping(value = "/questions/main/delay", method = RequestMethod.POST)
    @ResponseBody
    public String setDelay(@RequestParam("token") String token,
                            @RequestParam("question_id") String question_id) throws JSONException{
        if (!checkPermissionWithoutName(token, roleTW))
            return invalidTokenMsg;
        boolean success = questionService.mergeDelay(Long.parseLong(question_id));
        if (success){
            return successMsg;
        }else {
            return errorMsg;
        }
    }


    /*
    * only related department can set delay
    * param question_id, delay_reason, delay_days
    * return {success , msg}
    * */
    @RequestMapping(value = "/questions/related/delay", method = RequestMethod.POST)
    @ResponseBody
    public String applyDelay(@RequestParam("token") String token,
                              @RequestParam("question_id") String question_id,
                             @RequestParam("delay_reason") String delay_reason,
                             @RequestParam("delay_days") int delay_days) throws JSONException{
        if (! questionService.hasLearderRole(Long.parseLong(question_id)))
            return invalidInputMsg;
        Role leaderRole = questionService.getQuestionDetail(Long.parseLong(question_id)).getLeaderRole();
        if (!checkPermissionWithoutName( token, leaderRole.getRole())){
            return invalidTokenMsg;
        }
        boolean success = questionService.setDelay(Long.parseLong(question_id),delay_reason,delay_days);
        if (success){
            return successMsg;
        } else{
            return errorMsg;
        }
    }

    /*
    *  Only Tuanwei can add QA
    *  param question_id
    *  return {success, msg}
    * */
    @RequestMapping(value = "/qa/add", method = RequestMethod.POST)
    @ResponseBody
    public String addQA(@RequestParam("token") String token,
                        @RequestParam("question_id") String question_id) throws JSONException{
        if (!checkPermissionWithoutName(token, roleTW)){
            return invalidTokenMsg;
        }
        boolean success = questionService.setCommon(Long.parseLong(question_id) , true);
        if (success){
            return successMsg;
        }else{
            return errorMsg;
        }
    }

    /*
    *  Only Tuanwei can del QA
    *  param question_id
    *  return {success, msg}
    * * */
   @RequestMapping(value = "/qa/del", method = RequestMethod.POST)
    @ResponseBody
    public String delQA(@RequestParam("token") String token,
                        @RequestParam("question_id") String question_id) throws JSONException{
        if (!checkPermissionWithoutName(token, roleTW)){
            return invalidTokenMsg;
        }
        boolean success = questionService.setCommon(Long.parseLong(question_id), false);
        if (success){
            return successMsg;
        }else{
            return errorMsg;
        }
    }

    /*
    * only tuanwei can set
    * param question_id
    * return {success, msg}
    * **/


    @RequestMapping(value = "/qa/top", method = RequestMethod.POST)
    @ResponseBody
    public String setTopQA(@RequestParam("token") String token,
                            @RequestParam("question_id") String question_id) throws JSONException{
        if (!checkPermissionWithoutName( token, roleTW)){
            return invalidTokenMsg;
        }
        boolean success = questionService.setTop(Long.parseLong(question_id), true);
        if (success){
            return successMsg;
        }else{
            return errorMsg;
        }
    }

    /* done
    * only xiaoban & zongban can get
    * no param
    * return msg
    * ***/
   @RequestMapping(value = "/statistics/get", method = RequestMethod.POST)
    @ResponseBody
    public String getStatistics(@RequestParam("token") String token) throws JSONException{
        if (!checkPermissionWithoutName(token, roleXB) && !checkPermissionWithoutName(token, roleZB)){
            return invalidTokenMsg;
        }
        String tokenRole;
        try{
            Claims claims = jwtService.parseToken(token);
            tokenRole = claims.get("role").toString();
        }
        catch (final Exception e){
            return invalidTokenMsg;
        }
        Role role = roleService.findByRole(tokenRole);
        List<Role> roleList = roleService.findFellowRole(role);

        if (roleList.size() == 0){
            return "[]";
        }
        String result = "";
        boolean flag = true;
        for (Role rol: roleList){
            JSONObject mem = new JSONObject();
            System.out.println(rol.getRole());
            mem.put("role", rol.getRole());
            mem.put("display_name", rol.getDisplayName());
            mem.put("question_num", rol.getReceivedNumber().toString());
            mem.put("intime_num", rol.getOntimeNumber().toString());
            mem.put("outdate_num", rol.getOvertimeNumber().toString());
            Long num = rol.getOntimeNumber() - rol.getDirectRespondNumber();
            mem.put("reject_num", num.toString());
            mem.put("response_num", rol.getDirectRespondNumber().toString());
            if (flag){
                flag = false;
                result = mem.toString();
            }else {
                result = result + ',' + mem.toString();
            }
        }
        return "[" + result + "]";
    }
}
