package com.thu.web.student;

import com.thu.domain.Role;
import com.thu.domain.User;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

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


    @GetMapping(value = "auth")
    public Object doAuth(@RequestParam(name = "token", required = false, defaultValue = "") String token)
    {
        boolean flag = jwtService.checkToken(token);

        if (flag) // 本地有缓存 成功认证
        {
            session.setAttribute("userId", 1L);
            return "redirect:/student/question/list";
        }
        else // 没有则跳转idTsinghua 重新认证
        {
            String authUrl = env.getProperty("login.authurl")
                    + "/" + env.getProperty("login.AppID") + "/" + env.getProperty("login.seq");
            return "redirect:" + authUrl;
        }

        //return "redirect:https://www.baidu.com/";D
    }

    @GetMapping(value = "login")
    @ResponseBody
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
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        String uname = "";
        String token = "";
        Role role = roleService.findByRole("xuesheng");
        String email = "";
        String idNumber = "";

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
        }

        userService.saveStudent(uname, token, role, email, idNumber);

        JSONObject result = new JSONObject();
        result.put("token", token);
        return result.toString();
    }
}
