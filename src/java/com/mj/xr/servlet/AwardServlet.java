package com.mj.xr.servlet;

import com.mj.xr.bean.Award;
import com.mj.xr.bean.Project;
import com.mj.xr.bean.UploadParams;
import com.mj.xr.service.AwardService;
import com.mj.xr.service.impl.AwardServiceImpl;
import com.mj.xr.util.UploadUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.*;

@WebServlet("/award/*")
public class AwardServlet extends BaseServlet<Award>{


    //获取信息列表展示
    public void admin(HttpServletRequest request, HttpServletResponse response) throws Exception{
        request.setAttribute("awards", service.list());
        forward(request, response, "admin/award.jsp");
    }

    //持久化客户端传递过来的数据
    public void save(HttpServletRequest request, HttpServletResponse response) throws Exception{
        //处理文件上传
        UploadParams uploadParams = UploadUtil.parseRequest(request);
        Map<String, Object> params = uploadParams.getParams();
        Map<String, FileItem> fileParams = uploadParams.getFileParams();

        Award award = new Award();
        BeanUtils.populate(award, params);
        //编辑时上传了新的图片-重新设置Award的image属性
        FileItem item = fileParams.get("imageFile");
        award.setImage(UploadUtil.uploadImage(item, request, award.getImage()));

        if(service.save(award)){ //持久化成功
            redirect(request, response, "award/admin");
        }else { //持久化失败
            forwardError(request, response, "获奖信息保存失败");
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
            redirect(request, response ,"award/admin");
        }else { //删除失败
            //转发到错误页面
            forwardError(request, response, "获奖信息删除失败");
        }
    }

}
