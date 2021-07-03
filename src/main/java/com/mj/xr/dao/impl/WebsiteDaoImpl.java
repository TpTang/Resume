package com.mj.xr.dao.impl;

import com.mj.xr.bean.Website;
import com.mj.xr.dao.WebsiteDao;
import com.mj.xr.util.DBUtil;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import java.util.ArrayList;
import java.util.List;

public class WebsiteDaoImpl extends BaseDaoImpl<Website> implements WebsiteDao {

    /**
     * 添加或更新
     */
    public boolean save(Website bean){
        Integer id = bean.getId();
        List<Object> args = new ArrayList<>();
        args.add(bean.getFooter());
        String sql;
        if(id == null || id < 1){ //添加
            sql = "INSERT INTO `website`(footer) VALUES(?)";
        }else{ //更新
            sql = "UPDATE `website` SET `footer` = ? WHERE `id` = ?";
            args.add(id);
        }
        return DBUtil.getTpl().update(sql, args.toArray()) > 0;
    }

    /**
     * 获取单个对象
     */
    public Website get(Integer id){
        String sql = "SELECT id, created_time, footer FROM website WHERE id = ?";
        return DBUtil.getTpl().queryForObject(sql, new BeanPropertyRowMapper<>(Website.class), id);
    }
    /**
     * 获取多个对象
     */
    public List<Website> list(){
        String sql = "SELECT id, created_time, footer FROM `website`";
        return DBUtil.getTpl().query(sql, new BeanPropertyRowMapper<>(Website.class));
    }

}
