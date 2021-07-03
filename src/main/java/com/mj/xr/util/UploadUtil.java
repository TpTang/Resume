package com.mj.xr.util;

import com.mj.xr.bean.UploadParams;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 文件上传工具类
 */
public class UploadUtil {
    private static final String BASE_DIR = "upload"; //上传目录
    private static final String IMG_DIR  = "img"; //图片目录

    /**
     * 图片上传
     * @param item 图片条目
     * @param oldImage 被setImage过了的image
     * @return 存储到数据库的图片路径
     */
    public static String uploadImage(FileItem item, HttpServletRequest request, String oldImage) throws Exception{
        //oldImage为空串：添加操作  否则更新
        //如果oldImage为空串就先将其置为null
        if(oldImage != null && oldImage.length() == 0) oldImage = null;

        if(item == null) return oldImage;
        InputStream is = item.getInputStream();
        if(is.available() == 0) return oldImage; //没有文件数据流

        ServletContext ctx = request.getServletContext();
        String filename = UUID.randomUUID()+"."+ FilenameUtils.getExtension(item.getName());
        String image = BASE_DIR+"/"+IMG_DIR+"/"+filename;
        String filepath = ctx.getRealPath(image); //文件在硬盘上的绝对路径
        FileUtils.copyInputStreamToFile(item.getInputStream(), new File(filepath));

        //删除旧文件
        if(oldImage != null){
            FileUtils.deleteQuietly(new File(ctx.getRealPath(oldImage))); //如果第一次上传就“安静”删除不存在的旧文件
        }
        return image;
    }

    /**
     * 文件上传：解析request
     * @return 封装了文件参数和非文件参数的Bean对象
     */
    public static UploadParams parseRequest(HttpServletRequest request) throws Exception{
        ServletFileUpload upload = new ServletFileUpload(new DiskFileItemFactory());
        upload.setHeaderEncoding("UTF-8");
        List<FileItem> fileItems = upload.parseRequest(request);

        Map<String, Object> params = new HashMap<>();
        Map<String, FileItem> fileparams = new HashMap<>();
        for (FileItem item : fileItems) {
            String fieldName = item.getFieldName();
            if(item.isFormField()){ //非文件参数
                params.put(fieldName, item.getString("UTF-8"));
            }else { //文件参数
                fileparams.put(fieldName, item);
            }
        }
        UploadParams uploadParams = new UploadParams();
        uploadParams.setParams(params);
        uploadParams.setFileParams(fileparams);
        return uploadParams;
    }
}
