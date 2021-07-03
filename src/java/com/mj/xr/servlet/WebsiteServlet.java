package com.mj.xr.servlet;

import com.mj.xr.bean.Website;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 *  处理website业务逻辑
 */
@WebServlet("/website/*")
public class WebsiteServlet extends BaseServlet<Website> {


    //website后台列表展示
    public void admin(HttpServletRequest request, HttpServletResponse response) throws Exception{
        List<Website> websites = service.list();
        Website website = (websites != null && !websites.isEmpty()) ? websites.get(0) : null;
        request.setAttribute("website", website);
        //转发
        forward(request, response, "admin/website.jsp");
    }

    //website后台添加、更新
    public void save(HttpServletRequest request, HttpServletResponse response) throws Exception{
        Website website = new Website();
        BeanUtils.populate(website, request.getParameterMap()); //参数->bean
        //持久化bean
        if(service.save(website)){ //持久化成功
            redirect(request, response, "website/admin");
        }else{ //持久化失败
            forwardError(request, response, "网站信息保存失败");
        }
    }
}
