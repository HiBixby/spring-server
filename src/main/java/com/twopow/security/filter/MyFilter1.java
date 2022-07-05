package com.twopow.security.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class MyFilter1 implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req=(HttpServletRequest) request;
        HttpServletResponse res=(HttpServletResponse) response;

        //토큰:2powTeam
        if (req.getMethod().equals("POST")){
            System.out.println("POST 요청됨");
            String headerAuth = req.getHeader("Authorization");
            System.out.println(headerAuth);

            if(headerAuth.equals("2powTeam")){
                chain.doFilter(req,res);
            }
            else{
                PrintWriter out = res.getWriter();
                out.println("인증안됨");
            }
        }

        System.out.println("필터1");
        //chain.doFilter(request,response);//다시 체인에 넘겨줘야함.
    }
}
