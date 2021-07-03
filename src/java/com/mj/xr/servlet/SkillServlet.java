package com.mj.xr.servlet;

import com.mj.xr.bean.Skill;
import com.mj.xr.service.SkillService;
import com.mj.xr.service.impl.SkillServiceImpl;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@WebServlet("/skill/*")
public class SkillServlet extends BaseServlet<Skill>{
//    private final SkillService service = new SkillServiceImpl(); //在父类自动化生成了

    //获取数据库数据展示
    public void admin(HttpServletRequest request, HttpServletResponse response) throws Exception{
        List<Skill> skills = service.list();
        if(skills != null){
            request.setAttribute("skills", skills);
            forward(request, response, "admin/skill.jsp");
        }else{
            forwardError(request, response, "获取技能信息失败");
        }
    }

    //保存客户端传递的数据
    public void save(HttpServletRequest request, HttpServletResponse response) throws Exception{
        Skill skill = new Skill();
        BeanUtils.populate(skill, request.getParameterMap());
        if(service.save(skill)){ //持久化成功
            //重定向
            redirect(request, response, "skill/admin");
        }else{ //持久化失败
            //转发到错误页面
            forwardError(request, response, "仅能信息保存失败");
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
            redirect(request, response, "skill/admin");
        }else { //删除失败
            //转发到错误页面
            forwardError(request, response, "删除技能信息失败");
        }
    }
}
