package com.mj.xr.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import com.mj.xr.bean.Award;
import com.mj.xr.bean.UploadParams;
import com.mj.xr.bean.User;
import com.mj.xr.service.AwardService;
import com.mj.xr.service.SkillService;
import com.mj.xr.service.UserService;
import com.mj.xr.service.WebsiteService;
import com.mj.xr.service.impl.AwardServiceImpl;
import com.mj.xr.service.impl.SkillServiceImpl;
import com.mj.xr.service.impl.WebsiteServiceImpl;
import com.mj.xr.util.UploadUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.fileupload.FileItem;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@WebServlet("/user/*")
public class UserServlet extends BaseServlet<User>{
    private final SkillService skillService = new SkillServiceImpl();
    private final AwardService awardService = new AwardServiceImpl();
    private final WebsiteService websiteService = new WebsiteServiceImpl();

    //回显数据到客户端
    public void admin(HttpServletRequest request, HttpServletResponse response) throws Exception{
            request.setAttribute("user", service.list().get(0));
            forward(request, response, "admin/user.jsp");
    }

    //做登录验证
    public void login(HttpServletRequest request, HttpServletResponse response) throws Exception{

        //利用AJAX做
        //检查验证码
        response.setContentType("text/json; charset=UTF-8");
        Map<String, Object> result = new HashMap<>();
        String captcha = request.getParameter("captcha").toLowerCase(); //toLowerCase():实现客户端验证码忽略大小写
        String code = (String) request.getSession().getAttribute("code");
        if(!captcha.equals(code)){ //验证码不匹配
            result.put("success", false);
            result.put("msg", "验证码错误");
            response.getWriter().write(new ObjectMapper().writeValueAsString(result));
            return;
        }
        User user = new User();
        BeanUtils.populate(user, request.getParameterMap()); //只有password、email
        user = ((UserService) service).get(user);
        if(user != null){ //邮箱、密码验证成功
            request.getSession().setAttribute("user", user); //将用户存起来做下次请求的验证
            result.put("success", true);
        }else { //邮箱或密码错误
            result.put("success", false);
            result.put("msg", "邮箱或密码错误");
        }
        //自己自定义添加Cookie
        Cookie cookie = new Cookie("JSESSIONID", request.getSession().getId());
        cookie.setMaxAge(3600 * 24 * 7); //七天免登录
        response.addCookie(cookie);

        response.getWriter().write(new ObjectMapper().writeValueAsString(result));
    }

    //生成验证码
    public void captcha(HttpServletRequest request, HttpServletResponse response) throws Exception{
        //创建captcha对象
        DefaultKaptcha dk = new DefaultKaptcha();
        //配置验证码图片
        try(InputStream is = getClass().getClassLoader().getResourceAsStream("kaptcha.properties")){
            Properties properties = new Properties();
            properties.load(is);
            Config config = new Config(properties);
            dk.setConfig(config);
        }
        //生成验证码字符串
        String code = dk.createText();
        HttpSession session = request.getSession();
        session.setAttribute("code", code.toLowerCase());
        //生成验证码图片
        BufferedImage image = dk.createImage(code);
        //设置返回数据的格式
        response.setContentType("image/jpeg");
        //将图片数据写回到客户端
        ImageIO.write(image, "jpg", response.getOutputStream());
    }

    //退出登录
    public void logout(HttpServletRequest request, HttpServletResponse response) throws Exception{
        //清除用户信息
        request.getSession().removeAttribute("user");
        //重定向到login.jsp
        redirect(request, response, "page/login.jsp");
    }

    //展示修改密码页面
    public void password(HttpServletRequest request, HttpServletResponse response) throws Exception{
        forward(request,response, "admin/password.jsp");
    }

    //修改密码
    public void updatePassword(HttpServletRequest request, HttpServletResponse response) throws Exception{
        //对比Session中的user密码
        User user = (User) request.getSession().getAttribute("user");
        if(!user.getPassword().equals(request.getParameter("oldPassword"))){
            forwardError(request, response, "旧密码错误");
            return;
        }
        //修改密码
        user.setPassword(request.getParameter("newPassword"));
        if (service.save(user)){ //修改成功
            redirect(request, response, "page/login.jsp");
        }else { //修改失败
            forwardError(request ,response, "密码修改失败");
        }
    }

    //保存
    public void save(HttpServletRequest request, HttpServletResponse response) throws Exception{
        //处理文件上传
        UploadParams params = UploadUtil.parseRequest(request);

        User user = new User();
        BeanUtils.populate(user, params.getParams());
        //从Session中拿到用户邮箱、密码
        User loginUser = (User) request.getSession().getAttribute("user");
        user.setEmail(loginUser.getEmail());
        user.setPassword(loginUser.getPassword());

        FileItem item = params.getFileParams().get("photoFile");
        user.setPhoto(UploadUtil.uploadImage(item, request, user.getPhoto()));

        //保存
        if (service.save(user)){ //更新成功
            request.getSession().setAttribute("user", user); //当user更新时更新Session中的user
            redirect(request, response, "user/admin");
        }else {
            forwardError(request, response, "个人信息保存失败");
        }
    }

    //展示前台主页
    public void front(HttpServletRequest request, HttpServletResponse response) throws Exception{
        User user = service.list().get(0);
        //用户信息
        request.setAttribute("user", user);
        //个人技能
        request.setAttribute("skills", skillService.list());
        //获奖成就
        request.setAttribute("awards", awardService.list());
        //网站信息
        request.setAttribute("website", websiteService.list().get(0).getFooter());
        //个人特质
        request.setAttribute("trait", user.getTrait().split(","));
        //兴趣爱好
        request.setAttribute("interests", user.getInterests().split(","));

        forward(request, response, "front/user.jsp");
    }

    //配置访问项目名展示首页
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uri = request.getRequestURI();
        String[] cmps = uri.split("/");
        String methodName = "/"+cmps[cmps.length - 1];
        if(methodName.equals(request.getContextPath())){ //uri以项目名结尾
            try {
                front(request, response);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            super.doGet(request, response);
        }
    }
}
