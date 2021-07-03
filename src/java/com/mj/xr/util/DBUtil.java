package com.mj.xr.util;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DBUtil {
    private static JdbcTemplate tpl = null;
    static{
         try{
            try(InputStream is  = DBUtil.class.getClassLoader().
                    getResourceAsStream("druid.properties")){
                Properties properties = new Properties();
                properties.load(is);
                DataSource ds = DruidDataSourceFactory.createDataSource(properties);
                tpl = new JdbcTemplate(ds);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static JdbcTemplate getTpl(){
        return tpl;
    }

}
