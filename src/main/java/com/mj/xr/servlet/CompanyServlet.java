package com.mj.xr.servlet;

import com.mj.xr.bean.Company;
import com.mj.xr.bean.UploadParams;
import com.mj.xr.util.UploadUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.fileupload.FileItem;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@WebServlet("/company/*")
public class CompanyServlet extends BaseServlet<Company>{

    //获取数据库数据展示
    public void admin(HttpServletRequest request, HttpServletResponse response) throws Exception{
        request.setAttribute("companies", service.list());
        //转发到JSP页面展示
        forward(request, response, "admin/company.jsp");
    }

    //持久化客户端传递的数据
    public void save(HttpServletRequest request, HttpServletResponse response) throws Exception{
        //处理文件上传
        UploadParams uploadParams = UploadUtil.parseRequest(request);
        Map<String, Object> params = uploadParams.getParams();
        Map<String, FileItem> fileParams = uploadParams.getFileParams();

        Company company = new Company();
        BeanUtils.populate(company, params);
        //编辑时上传了新的图片-重新设置Award的image属性
        FileItem item = fileParams.get("logoFile");
        company.setLogo(UploadUtil.uploadImage(item, request, company.getLogo()));

        if(service.save(company)){ //持久化成功
            redirect(request, response, "company/admin");
        }else { //持久化失败
            forwardError(request, response, "公司信息保存失败");
        }
    }

    //删除信息（单条删除、批量删除）
    public void remove(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String[] idStrs = request.getParameterValues("id");
        List<Integer> ids = new ArrayList<>();
        for (String idStr : idStrs) {
            ids.add(Integer.valueOf(idStr));
        }
        if(service.remove(ids)){ //删除成功-重定向
            redirect(request, response, "company/admin");
        }else { //删除失败-转发到错误页面
            forwardError(request, response, "公司信息删除失败");
        }
    }
}
