package com.thu.web.student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Service
public class LoginFilter extends OncePerRequestFilter {

    @Autowired
    HttpSession session;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
//        String uri = httpServletRequest.getRequestURI();
//        if(uri.contains("student/login")){    //登陆页面不拦截
//            filterChain.doFilter(httpServletRequest,httpServletResponse);
//            return;
//        }
//        if(uri.contains("student/question")){  //其它admin页面拦截
//            Integer userId = (Integer)session.getAttribute("studentId");
//            if(null == userId){
//                httpServletResponse.sendRedirect(httpServletRequest.getContextPath()+"student/login");
//                return;
//            }
//        }
        filterChain.doFilter(httpServletRequest,httpServletResponse);
    }
}
