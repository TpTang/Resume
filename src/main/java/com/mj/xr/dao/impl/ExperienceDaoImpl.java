package com.mj.xr.dao.impl;

import com.mj.xr.bean.Company;
import com.mj.xr.bean.Experience;
import com.mj.xr.dao.ExperienceDao;
import com.mj.xr.util.DBUtil;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;

import java.util.ArrayList;
import java.util.List;

public class ExperienceDaoImpl extends BaseDaoImpl<Experience> implements ExperienceDao {

    //程序调用100遍生成的东西都是一样的
    private static String querySql = null;
    private static RowMapper<Experience> rowMapper = null; //程序加载时对象内存固定成了一份，但调用时值可变

    static{
            //连接查询
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT ");
            sql.append("t1.id, t1.created_time, t1.job, t1.intro, t1.begin_day, t1.end_day, t1.company_id,");
            sql.append("t2.id, t2.created_time, t2.name, t2.logo, t2.website, t2.intro ");
            sql.append("FROM experience t1 JOIN company t2 on t1.company_id = t2.id ");
            querySql =  sql.toString();

            rowMapper = (resultSet, row)->{
            Experience experience = new Experience();
            experience.setId(resultSet.getInt("t1.id"));
            experience.setCreatedTime(resultSet.getDate("t1.created_time"));
            experience.setJob(resultSet.getString("t1.job"));
            experience.setIntro(resultSet.getString("t1.intro"));
            experience.setBeginDay(resultSet.getDate("t1.begin_day"));
            experience.setEndDay(resultSet.getDate("t1.end_day"));

            Company company = new Company();
            company.setId(resultSet.getInt("t2.id"));
            company.setCreatedTime(resultSet.getDate("t2.created_time"));
            company.setName(resultSet.getString("t2.name"));
            company.setLogo(resultSet.getString("t2.logo"));
            company.setIntro(resultSet.getString("t2.intro"));
            company.setWebsite(resultSet.getString("t2.website"));

            experience.setCompany(company);
            return experience;
        };
    }

    @Override
    public boolean save(Experience bean) {
        Integer id = bean.getId();
        List<Object> args = new ArrayList<>();
        args.add(bean.getJob());
        args.add(bean.getIntro());
        args.add(bean.getBeginDay());
        args.add(bean.getEndDay());
        args.add(bean.getCompany().getId());
        String sql;
        if(id == null || id < 1){ //添加
            sql = "INSERT INTO experience(job, intro, begin_day, end_day, company_id) VALUES(?,?,?,?,?)";
        }else { //更新
            sql = "UPDATE experience SET job=?, intro=?, begin_day=?, end_day=?, company_id=? WHERE id=?";
            args.add(id);
        }
        return DBUtil.getTpl().update(sql, args.toArray()) > 0;
    }

    @Override
    public Experience get(Integer id) {
        String sql = querySql + "WHERE t1.id = ?";
        return DBUtil.getTpl().queryForObject(sql, rowMapper, id);
    }

    @Override
    public List<Experience> list() {
        return DBUtil.getTpl().query(querySql, rowMapper);
    }

}
