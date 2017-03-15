package com.thu.web.student;

import com.thu.domain.Role;
import com.thu.service.RoleService;
import com.thu.service.UserService;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.MessageDigest;
/**
 * Created by source on 1/8/17.
 */

@Controller
@RequestMapping(value = "/student")
public class LoginController {

    @Autowired
    HttpSession session;

    //获得property文件的变量
    @Autowired
    private Environment env;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserService userService;

    @Autowired
    private JWTService jwtService;

    private final String errorMsg = "{\"success\":false,\"msg\":\"用户名密码不正确\"}";

    //后勤部门MD5验证
    private static String MD5(String sourceStr) {
        String result = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(sourceStr.getBytes("utf-8"));
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            result = buf.toString();
        } catch (Exception e) {}
        return result;
    }

    @GetMapping(value = "init")
    public Object initAuth()
    {
        return "student/init";
    }

    @RequestMapping(value = "auth",method = RequestMethod.POST)
    @ResponseBody
    public Object doAuth(@RequestParam(name = "token", required = false, defaultValue = "") String token)
    {
        Long userId = jwtService.getUserId(token);

        if (userId != null) // 本地有缓存 成功认证
        {
            session.setAttribute("userId", userId);
            System.out.println(session.getAttribute("userId"));
            return "{\"success\":true}";
        }
        else // 没有则跳转idTsinghua 重新认证
        {
            String authUrl = env.getProperty("login.authurl")
                    + "/" + MD5(env.getProperty("login.AppID")) + "/" + env.getProperty("login.seq") + "?/student/login";
            return "{\"success\":false, \"path\":\"" + authUrl + "\"}";
        }

        //return "redirect:https://www.baidu.com/";D
    }

    @GetMapping(value = "login")
    public Object saveToken(@RequestParam(name = "ticket") String ticket)
    {

        String ticketUrl = env.getProperty("login.ticket")
                + "/" + env.getProperty("login.AppID") + "/" + ticket + "/" + request.getLocalAddr();
        System.out.println(ticketUrl);


        //实例化httpclient 与 get方法
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet(ticketUrl);

        //请求结果
        CloseableHttpResponse response = null;
        String content = "";
        try
        {
            //执行get方法
            response = httpclient.execute(httpget);
            if (response.getStatusLine().getStatusCode() == 200) {
                content = EntityUtils.toString(response.getEntity(), "utf-8");
                System.out.println(content);
            }
        }
        catch (ClientProtocolException e) {
            e.printStackTrace();
            return errorMsg;
        }
        catch (IOException e) {
            e.printStackTrace();
            return errorMsg;
        }

        String uname = "";
        String token = "";
        Role role = roleService.findByRole("student");
        String email = "";
        String idNumber = "";


        //content = "code=0:zjh=2011980001:yhm=stu:xm=test_stu:yhlb=J0000:dw=计算中心:email=lqx@mail.com";
        String [] arr = content.split(":");
        for(String pairs : arr)
        {
            String [] strings = pairs.split("=");
            switch (strings[0])
            {
                case "zjh": idNumber = strings[1]; break;
                case "xm": uname = strings[1]; break;
                case "email": email = strings[1]; break;
            }
        }

        try {
            token = jwtService.creatToken(idNumber);
        } catch (Exception e) {
            e.printStackTrace();
            return errorMsg;
        }

        System.out.println(uname);
        System.out.println(token);
        System.out.println(role);
        System.out.println(email);
        System.out.println(idNumber);

        userService.saveStudent(uname, token, role, email, idNumber);

        // 登录成功
        Long userId = jwtService.getUserId(token);
        session.setAttribute("userId", userId);

        System.out.println("login: userId=" + userId);

        //String info = "登录成功，获得token" + result.toString() +
                //"<h1>您是第" +  userId.toString() + "位用户<h1/>" +
                //"<h1><a href='/student/question'>进入主页</a></h1>";

        return "redirect:/student/init?" + token;
    }
}
