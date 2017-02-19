package com.thu.web.school;

import com.thu.domain.Role;
import com.thu.domain.TUser;
import com.thu.domain.UserRepository;
import io.jsonwebtoken.Claims;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.thu.service.QuestionService;
import com.thu.service.ResponseService;
import com.thu.service.RoleService;
import com.thu.service.UserService;

import javax.websocket.server.PathParam;
import java.util.List;


@RestController
public class SchoolController{
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private final String loginErrorMsg = "{\"success\":false,\"msg\":\"Invalid Name Or Passwd\"}";
    private final String invalidInputMsg = "{\"success\":false,\"msg\":\"Invalid Input\"}";
    private final String invalidTokenMsg = "{\"success\":false,\"msg\":\"Invalid Token\"}";
    private final String userNotLogMsg = "{\"success\":false,\"msg\":\"TUser Not Log In\"}";
    private final String errorMsg = "{\"success\":false,\"msg\":\"Fail\"}";
    private final String successMsg = "{\"success\":true,\"msg\":\"Done\"}";
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

    //TODO: compare String or Role
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
        boolean flag = true;
        for (Role role:mainRoleList){
            resultMain = new JSONObject();

            resultMain.put("role",role.getRole());
            resultMain.put("label",role.getDisplayName());
            if (flag){
                ansMain = ansMain + resultMain.toString();
                flag =false;
            }
            else ansMain = ansMain + ',' + resultMain.toString();
        }
        flag = true;
        for (Role role:relatedRoleList){
            resultRelated = new JSONObject();
            resultRelated.put("role",role.getRole());
            resultRelated.put("label", role.getDisplayName());
            if (flag){
                flag = false;
                ansRelated = ansRelated + resultRelated.toString();
            }else
                ansRelated = ansRelated + ',' + resultRelated.toString();
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
    public String login( @RequestParam("uname") String uname,
                        @RequestParam("passwd") String passwd
            ) throws JSONException{
        //System.out.println(uname);
        //String passwd = "111";
        if (uname.equals("") || ! userService.containsUname(uname))
            return loginErrorMsg;
        TUser usr = userService.findUser(uname);

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
        return result.toString();
    }

    @RequestMapping(value = "/auth/login/{token:.+}", method = RequestMethod.POST)
    @ResponseBody
    public String login( @PathVariable String token) throws JSONException {
        if (!checkPermissionWithoutName(token, roleALL)){
            return invalidTokenMsg;
        }
        return successMsg;
    }

    /*  done
    *  every one can log out
    *  log out
    *  no param
    *  return {role, resp_person, fixed_phone, mobile_phone}
    * */
    @RequestMapping(value="/auth/logout/{token:.+}",method =RequestMethod.POST)
    @ResponseBody
    public String logout(@PathVariable String token)throws JSONException{
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
            TUser usr = userService.findUser(tokenName);
            result.put("role", usr.getRole().getRole());
            result.put("resp_person", usr.getRole().getRespPerson());
            result.put("fixed_phone", usr.getFixedNumber());
            result.put("mobile_phone", usr.getMobileNumber());
            return result.toString();
        }
        else
            return userNotLogMsg;
    }

    @RequestMapping(value ="/info/get/{token:.+}", method = RequestMethod.POST)
    @ResponseBody
    public String getInfo(@PathVariable String token) throws JSONException{
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
        TUser usr = userService.findUser(tokenName);
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
    @RequestMapping(value ="/info/set/{token:.+}", method = RequestMethod.POST)
    @ResponseBody
    public String setInfo(@PathVariable String token,
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
        System.out.println(resp_person);
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
    @RequestMapping(value = "/contact/del/{token:.+}", method = RequestMethod.POST)
    @ResponseBody
    public String delContact(@PathVariable String token,
                             @PathParam("uname") String uname) throws JSONException{
        if (!checkPermissionWithoutName(token, roleXB))
            return invalidTokenMsg;
        if (!userService.containsUname(uname)){
            return invalidInputMsg;
        }
        TUser usr = userService.findUser(uname);

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
    * NOT only xiaoban get contact
    * no param
    * return [{role, uname, resp_person, fixed_phone, mobile_phone},{}]
    * */

    @RequestMapping(value = "/contact/get/{token:.+}", method = RequestMethod.POST)
    @ResponseBody
    public String getContact(@PathVariable String token) throws JSONException{
        // By chenzhenhuan 2017.2.17
        boolean is_xiaoban = checkPermissionWithoutName(token, roleXB);
        List<TUser> TUserList = userService.findAll();
        String result = "[";
        boolean flag = true;
        for (TUser usr: TUserList) {
            if (usr.getRole().getRole().equals("student")) continue;
            JSONObject mem = new JSONObject();
            mem.put("display_name", usr.getRole().getDisplayName());
            if (is_xiaoban) mem.put("uname", usr.getUname());
            mem.put("resp_person", usr.getRole().getRespPerson());
            mem.put("fixed_phone", usr.getFixedNumber());
            mem.put("mobile_phone", usr.getMobileNumber());
            //System.out.println(mem.toString());
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

    @RequestMapping(value = "/contact/get", method = RequestMethod.POST)
    @ResponseBody
    public String getContact() throws JSONException{
        List<TUser> userList = userService.findAll();
        String result = "[";
        boolean flag = true;
        for (TUser usr:userList) {
            if (usr.getRole().getRole().equals("student")) continue;
            JSONObject mem = new JSONObject();
            mem.put("display_name", usr.getRole().getDisplayName());
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
    * only xiaoban set contact
    * param role, uname, resp_person, fixed_phone, mobile_phone, passwd, is_new
    * department(uname = role)
    * return success, msg
    * */
   @RequestMapping(value = "/contact/set/{token:.+}", method = RequestMethod.POST)
    @ResponseBody
    public String setContact(@PathVariable String token,
                             @RequestParam("display_name") String displayname,
                             @RequestParam("uname") String uname,
                             @RequestParam("resp_person") String resp_person,
                             @RequestParam("fixed_phone") String fixed_phone,
                             @RequestParam("mobile_phone") String mobile_phone,
                             @RequestParam("passwd") String passwd,
                             @RequestParam("is_new") boolean is_new) throws JSONException{
        if (!checkPermissionWithoutName( token, roleXB))
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
    * only xiaoban can set
    * param question_id
    * return {success, msg}
    *
    @RequestMapping(value = "/questions/main/delay/{token:.+}", method = RequestMethod.POST)
    @ResponseBody
    public String setDelay(@PathVariable String token,
                            @RequestParam("question_id") String question_id) throws JSONException{
        if (!checkPermissionWithoutName(token, roleXB))
            return invalidTokenMsg;
        boolean success = questionService.mergeDelay(Long.parseLong(question_id));
        if (success){
            return successMsg;
        }else {
            return errorMsg;
        }
    }
*/

    /*
    * only related department can set delay
    * param question_id, delay_reason, delay_days
    * return {success , msg}
    * */
    @RequestMapping(value = "/questions/related/delay/{token:.+}", method = RequestMethod.POST)
    @ResponseBody
    public String applyDelay(@PathVariable String token,
                              @RequestParam("question_id") String question_id,
                             @RequestParam("delay_reason") String delay_reason,
                             @RequestParam("delay_days") int delay_days) throws JSONException{
        if (! questionService.hasLearderRole(Long.parseLong(question_id)))
            return invalidInputMsg;
        Role leaderRole = questionService.getQuestionDetail(Long.parseLong(question_id)).getLeaderRole();
        if (!checkPermissionWithoutName( token, leaderRole.getRole())){
            return invalidTokenMsg;
        }
        Role tuan= roleService.findByRole(roleXB);
        boolean success = questionService.setDelay(Long.parseLong(question_id),delay_reason,delay_days,tuan);
        if (success){
            return successMsg;
        } else{
            return errorMsg;
        }
    }

    /*
    *  Only xiaoban can add QA
    *  param question_id
    *  return {success, msg}
    * */
    @RequestMapping(value = "/qa/add/{token:.+}", method = RequestMethod.POST)
    @ResponseBody
    public String addQA(@PathVariable String token,
                        @RequestParam("question_id") String question_id) throws JSONException{
        if (!checkPermissionWithoutName(token, roleXB)){
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
    *  Only xiaoban can del QA
    *  param question_id
    *  return {success, msg}
    * * */
   @RequestMapping(value = "/qa/del/{token:.+}", method = RequestMethod.POST)
    @ResponseBody
    public String delQA(@PathVariable String token,
                        @RequestParam("question_id") String question_id) throws JSONException{
        if (!checkPermissionWithoutName(token, roleXB)){
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
    * only xiaoban can set
    * param question_id
    * return {success, msg}
    * **/

    @RequestMapping(value = "/qa/top/{token:.+}", method = RequestMethod.POST)
    @ResponseBody
    public String setTopQA(@PathVariable String token,
                            @RequestParam("question_id") String question_id) throws JSONException{
        if (!checkPermissionWithoutName( token, roleXB)){
            return invalidTokenMsg;
        }
        boolean success = questionService.setTop(Long.parseLong(question_id), true);
        if (success){
            return successMsg;
        }else{
            return errorMsg;
        }
    }


    @RequestMapping(value = "/qa/notop/{token:.+}", method = RequestMethod.POST)
    @ResponseBody
    public String setNoTopQA(@PathVariable String token,
                            @RequestParam("question_id") String question_id) throws JSONException{
        if (!checkPermissionWithoutName( token, roleXB)){
            return invalidTokenMsg;
        }
        boolean success = questionService.setTop(Long.parseLong(question_id), false);
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
   @RequestMapping(value = "/statistics/get/{token:.+}", method = RequestMethod.POST)
    @ResponseBody
    public String getStatistics(@PathVariable String token) throws JSONException{
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
            //System.out.println(rol.getRole());
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
