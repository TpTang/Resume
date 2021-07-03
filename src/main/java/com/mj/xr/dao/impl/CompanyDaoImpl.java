package com.mj.xr.dao.impl;

import com.mj.xr.bean.Company;
import com.mj.xr.dao.CompanyDao;
import com.mj.xr.util.DBUtil;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import java.util.ArrayList;
import java.util.List;

public class CompanyDaoImpl extends BaseDaoImpl<Company> implements CompanyDao {
    //更新、添加记录
    @Override
    public boolean save(Company bean) {
        Integer id = bean.getId();
        List<Object> args = new ArrayList<>();
        args.add(bean.getName());
        args.add(bean.getLogo());
        args.add(bean.getWebsite());
        args.add(bean.getIntro());
        String sql;
        if(id == null || id < 1){ //添加
            sql = "INSERT INTO company(name, logo, website, intro) VALUES(?, ?, ?, ?)";
        }else { //更新
            sql = "UPDATE company SET name=?, logo=?, website=?, intro=? WHERE id=?";
            args.add(id);
        }
        return DBUtil.getTpl().update(sql, args.toArray()) > 0;
    }

    //根据id获取记录
    @Override
    public Company get(Integer id) {
        String sql = "SELECT id, created_time, name, logo, website, intro FROM company WHERE id=?";
        return DBUtil.getTpl().queryForObject(sql, new BeanPropertyRowMapper<>(Company.class), id);
    }

    //获取所有记录
    @Override
    public List<Company> list() {
        String sql = "SELECT id, created_time, name, logo, website, intro FROM company";
        return DBUtil.getTpl().query(sql, new BeanPropertyRowMapper<>(Company.class));
    }
}
