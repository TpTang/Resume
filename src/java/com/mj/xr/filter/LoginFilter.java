package com.mj.xr.filter;

import com.mj.xr.bean.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//拦截所有请求
@WebFilter("/*")
public class LoginFilter implements Filter {

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        //做请求之前的处理
        HttpServletRequest request = (HttpServletRequest)req;
        HttpServletResponse response = (HttpServletResponse)resp;

        String uri = request.getRequestURI(); //uri:/xr/../..
        if(uri.contains("/asset/")
                || uri.contains("/contact/save")
        ){ //优先放开的资源
            chain.doFilter(request, response);
        }else if(uri.contains("/admin")
                || uri.contains("/save")
                || uri.contains("/remove")
                || uri.contains("/user/password")
                || uri.contains("/user/updatePassword")
        ){
            //需要做登录验证的请求
            User user = (User) request.getSession().getAttribute("user");
            if(user != null){ //登录成功过
                chain.doFilter(request, response);
            }else { //没有登录成功过
                response.sendRedirect(request.getContextPath()+"/page/login.jsp");
            }
        }else {
            chain.doFilter(request, response);
        }
    }


}
