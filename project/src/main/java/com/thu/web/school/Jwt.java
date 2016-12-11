package com.thu.web.school;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by andrew on 2016/12/11.
 */

@Component
public class Jwt {
    private String secrectKey = "secrectkey";

    public String creatToken(String uname, String role) throws Exception{
        String token = Jwts.builder().setSubject(uname).claim("role",role).claim("uname",uname)
                .setIssuedAt(new Date()).signWith(SignatureAlgorithm.HS256,secrectKey).compact();
        Claims claims = Jwts.parser().setSigningKey(secrectKey).parseClaimsJws(token).getBody();
        System.out.println(claims);
        return token;
    }

    public Claims parseToken(String token) throws  Exception{
        Claims claims = Jwts.parser().setSigningKey(secrectKey).parseClaimsJws(token).getBody();
        return claims;
    }
}
