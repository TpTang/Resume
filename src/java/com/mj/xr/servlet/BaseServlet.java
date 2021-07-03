package com.mj.xr.servlet;

import com.mj.xr.service.BaseService;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.DateConverter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;


/**
 * 抽取公共代码
 */
public abstract class BaseServlet<T> extends HttpServlet {

    //自动生成Service对象
    protected BaseService<T> service = newService();
    protected BaseService<T> newService(){
        try {
            String className = getClass().getName().replace(".servlet.", ".service.impl.")
                    .replace("Servlet", "ServiceImpl");
            return (BaseService<T>) Class.forName(className).getConstructor().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            request.setCharacterEncoding("UTF-8");
            //利用反射调对应方法
            String[] cmps = request.getRequestURI().split("/");
            String methodName = cmps[cmps.length - 1];
            Method method = getClass().getMethod(methodName,
                    HttpServletRequest.class, HttpServletResponse.class);
            method.invoke(this, request, response);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            Throwable cause = e;
            while(cause.getCause() != null){
                cause = cause.getCause();
            }
            forwardError(request, response, cause.getClass().getSimpleName()+":"+cause.getMessage());
        }
    }

    //封装重定向
    protected void redirect(HttpServletRequest request, HttpServletResponse response, String path) throws ServletException, IOException{
        response.sendRedirect(request.getContextPath()+"/"+path);
    }

    //封装转发
    protected void forward(HttpServletRequest request, HttpServletResponse response, String path) throws ServletException, IOException{
        request.getRequestDispatcher("/WEB-INF/page/"+path).forward(request, response);
    }

    //封装错误信息转发
    protected void forwardError(HttpServletRequest request, HttpServletResponse response, String error) throws ServletException, IOException{
        request.setAttribute("error", error);
        forward(request, response, "error.jsp");
    }
}
