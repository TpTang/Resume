package com.mj.xr.listener;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.DateConverter;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.Date;

/**
 * 监听器：ServletContextListener-监听Context的创建与销毁（context：可以跟Tomcat通信，
 * 一个项目只有一个context对象，项目装载时创建、卸载时销毁）
 */
@WebListener()
public class ContextListener implements ServletContextListener {

    //监听到项目加载（部署）时做一些资源的加载(当然也可以用Filter做,但监听context更好)
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        DateConverter dateConverter = new DateConverter(null);
        dateConverter.setPatterns(new String[]{"yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss"});
        ConvertUtils.register(dateConverter, Date.class);
    }

    //监听到项目卸载时做一些资源的回收
    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
