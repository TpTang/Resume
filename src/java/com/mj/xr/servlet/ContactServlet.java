package com.mj.xr.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mj.xr.bean.Contact;
import com.mj.xr.bean.ContactFilterParam;
import com.mj.xr.service.ContactService;
import com.mj.xr.service.UserService;
import com.mj.xr.service.WebsiteService;
import com.mj.xr.service.impl.UserServiceImpl;
import com.mj.xr.service.impl.WebsiteServiceImpl;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/contact/*")
public class ContactServlet extends BaseServlet<Contact>{
    private final UserService userService = new UserServiceImpl();
    private final WebsiteService websiteService = new WebsiteServiceImpl();

    //获取数据展示到后台
    public void admin(HttpServletRequest request, HttpServletResponse response) throws Exception{
        ContactFilterParam param = new ContactFilterParam();
        BeanUtils.populate(param, request.getParameterMap());
        request.setAttribute("result", ((ContactService)service).list(param));

        forward(request, response, "admin/contact.jsp");
    }

    //获取数据展示到前台
    public void front(HttpServletRequest request, HttpServletResponse response) throws Exception{
        request.setAttribute("user", userService.list().get(0));
        request.setAttribute("website", websiteService.list().get(0).getFooter());
        forward(request, response, "front/contact.jsp");
    }

    //保存客户留言到数据库
    public void save(HttpServletRequest request, HttpServletResponse response) throws Exception{
        //验证验证码是否正确
        String code = (String) request.getSession().getAttribute("code");
        String captcha = request.getParameter("captcha");
        if (!code.equals(captcha)){
            forwardError(request, response, "验证码错误");
        }

        Contact contact = new Contact();
        BeanUtils.populate(contact, request.getParameterMap());
        if (service.save(contact)){
            redirect(request, response, "contact/front");
        }else {
            forwardError(request, response, "保存留言失败");
        }
    }

    //修改服务器状态记录为“已读”
    public void read(HttpServletRequest request, HttpServletResponse response) throws Exception{
        Integer id = Integer.valueOf(request.getParameter("id"));
        Map<String, Object> result = new HashMap<>();
        if(((ContactService)service).read(id)){ //修改成功
            result.put("success", true);
            result.put("msg", "查看成功");
        }else { //修改失败
            result.put("success", false);
            result.put("msg", "查看失败");
        }
        response.setContentType("text/json; charset=UTF-8");
        response.getWriter().write(new ObjectMapper().writeValueAsString(result)); //第三方工具
    }
}
