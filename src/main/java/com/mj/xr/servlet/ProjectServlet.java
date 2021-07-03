package com.mj.xr.servlet;

import com.mj.xr.bean.Company;
import com.mj.xr.bean.Project;
import com.mj.xr.bean.UploadParams;
import com.mj.xr.service.CompanyService;
import com.mj.xr.service.UserService;
import com.mj.xr.service.WebsiteService;
import com.mj.xr.service.impl.CompanyServiceImpl;
import com.mj.xr.service.impl.UserServiceImpl;
import com.mj.xr.service.impl.WebsiteServiceImpl;
import com.mj.xr.util.UploadUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/project/*")
public class ProjectServlet extends BaseServlet<Project>{
    private final UserService userService = new UserServiceImpl();
    private final WebsiteService websiteService = new WebsiteServiceImpl();

    //获取数据库数据展示
    public void admin(HttpServletRequest request, HttpServletResponse response) throws Exception{
        CompanyService companyService = new CompanyServiceImpl();
        request.setAttribute("projects", service.list());
        request.setAttribute("companies", companyService.list());
        forward(request, response, "admin/project.jsp");
    }

    //持久化客户端传递的数据
    public void save(HttpServletRequest request, HttpServletResponse response) throws Exception{
        //处理文件上传
        UploadParams uploadParams = UploadUtil.parseRequest(request);
        Map<String, Object> params = uploadParams.getParams();
        Map<String, FileItem> fileParams = uploadParams.getFileParams();

        Project project = new Project();
        BeanUtils.populate(project, params);
        //对公司信息做特殊处理
        Company company = new Company();
        company.setId(Integer.valueOf(params.get("companyId").toString())); //文件上传不能request.getParameter()获取参数
        project.setCompany(company);
        //对文件数据做处理
        FileItem item = fileParams.get("imageFile");
        project.setImage(UploadUtil.uploadImage(item, request, project.getImage()));

        if(service.save(project)){ //持久化成功
            redirect(request, response, "project/admin");
        }else { //持久化失败
            forwardError(request, response, "项目信息保存失败");
        }
    }

    //删除信息（单条删除、批量删除）
    public void remove(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String[] idStrs = request.getParameterValues("id");
        List<Integer> ids = new ArrayList<>();
        for (String idStr : idStrs) {
            ids.add(Integer.valueOf(idStr));
        }

        if(service.remove(ids)) { //删除成功
            redirect(request, response, "project/admin");
        }else { //删除失败
            forwardError(request, response, "项目信息删除失败");
        }
    }

    //获取数据展示到前台
    public void front(HttpServletRequest request, HttpServletResponse response) throws Exception{
        request.setAttribute("user", userService.list().get(0));
        request.setAttribute("website", websiteService.list().get(0).getFooter());
        request.setAttribute("projects", service.list());
        forward(request, response, "front/project.jsp");
    }
}
