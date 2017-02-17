package com.thu.web.school;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by andrew on 2016/12/5.
 */
public class TokenMap {
    public static Map<String, String> uname_token = new HashMap();

    public String getToken(String uname){
        if (uname_token.containsKey(uname)) {
            return uname_token.get(uname);
        }else{
            return "";
        }
    }

    public boolean removeUname(String uname){
        if (uname_token.containsKey(uname)) {
            uname_token.remove(uname);
            return true;
        }
        else return false;
    }

    public void setToken(String uname, String token){
        uname_token.put(uname, token);
    }

    public void clear(){
        uname_token.clear();
    }

    public boolean isEmpty(){
        return uname_token.isEmpty();
    }

    public int getLen(){
        return uname_token.size();
    }
}
