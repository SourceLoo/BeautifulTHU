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
public class TokenFilter extends OncePerRequestFilter {

    @Autowired
    HttpSession session;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String uri = httpServletRequest.getRequestURI();

        System.out.println("当前过滤的url是" + uri);

        if(uri.contains("student/auth") || uri.contains("student/login")) //登陆页面不拦截
        {

            System.out.println("filter:student/auth|login");
            filterChain.doFilter(httpServletRequest,httpServletResponse);
            return;
        }
        if(uri.contains("student/")) //其它admin页面拦截
        {
            System.out.println("filter:student/");
            Long userId = (Long) session.getAttribute("userId");
            if(null == userId)
            {
                httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/student/auth");
                return;
            }
        }
        filterChain.doFilter(httpServletRequest,httpServletResponse);
    }
}