package com.mj.xr.servlet;

import com.mj.xr.bean.Education;
import com.mj.xr.bean.User;
import com.mj.xr.service.EducationService;
import com.mj.xr.service.UserService;
import com.mj.xr.service.WebsiteService;
import com.mj.xr.service.impl.EducationServiceImpl;
import com.mj.xr.service.impl.UserServiceImpl;
import com.mj.xr.service.impl.WebsiteServiceImpl;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/education/*")
public class EducationServlet extends BaseServlet<Education> {
    private final UserService userService = new UserServiceImpl();
    private final WebsiteService websiteService = new WebsiteServiceImpl();

    //获取数据库数据展示
    public void admin(HttpServletRequest request, HttpServletResponse response) throws Exception{
        List<Education> educations = service.list();
        if(educations != null){
            //转发
            request.setAttribute("educations", educations);
            forward(request, response, "admin/education.jsp");
        }else{
            //转发到错误信息页面
            forwardError(request, response ,"教育信息获取失败");
        }
    }

    //保存客户端传递的数据
    public void save(HttpServletRequest request, HttpServletResponse response) throws Exception{
        Education education = new Education();
        BeanUtils.populate(education, request.getParameterMap());
        if(service.save(education)){ //持久化成功
            //重定向
            redirect(request, response, "education/admin");
        }else{ //持久化失败
            //转发到错误页面
            forwardError(request, response, "教育信息保存失败");
        }
    }
    //批量删除记录(和删除一条记录合并在一起）
    public void remove(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String[] idStrs = request.getParameterValues("id");
        List<Integer> ids = new ArrayList<>();
        for(String idStr : idStrs){
            ids.add(Integer.valueOf(idStr));
        }
        //删除逻辑
        if(service.remove(ids)){ //删除成功
            //重定向
            redirect(request, response, "education/admin");
        }else { //删除失败
            //转发到错误页面
            forwardError(request, response, "教育信息删除失败");
        }
    }

    //获取数据展示到前台
    public void front(HttpServletRequest request, HttpServletResponse response) throws Exception{
        request.setAttribute("user", userService.list().get(0));
        request.setAttribute("website", websiteService.list().get(0).getFooter());
        request.setAttribute("educations", service.list());
        forward(request, response, "front/education.jsp");
    }

}
