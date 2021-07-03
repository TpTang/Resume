package com.mj.xr.dao.impl;

import com.mj.xr.bean.Company;
import com.mj.xr.bean.Project;
import com.mj.xr.dao.ProjectDao;
import com.mj.xr.util.DBUtil;
import org.springframework.jdbc.core.RowMapper;

import java.util.ArrayList;
import java.util.List;

public class ProjectDaoImpl extends BaseDaoImpl<Project> implements ProjectDao {

    private static String querySql = null;
    private static RowMapper<Project> rowMapper = null;
    static{
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append("t1.id, t1.created_time, t1.name, t1.intro, t1.website, t1.image, t1.begin_day, t1.end_day, t1.company_id, ");
        sql.append("t2.id, t2.created_time, t2.name, t2.logo, t2.website, t2.intro ");
        sql.append("FROM project t1 JOIN company t2 ON t1.company_id = t2.id ");
        querySql = sql.toString();

        rowMapper = (resultSet, row)->{
            Project project = new Project();
            project.setId(resultSet.getInt("t1.id"));
            project.setCreatedTime(resultSet.getDate("t1.created_time"));
            project.setName(resultSet.getString("t1.name"));
            project.setIntro(resultSet.getString("t1.intro"));
            project.setWebsite(resultSet.getString("t1.website"));
            project.setIntro(resultSet.getString("t1.intro"));
            project.setImage(resultSet.getString("t1.image"));
            project.setBeginDay(resultSet.getDate("t1.begin_day"));
            project.setEndDay(resultSet.getDate("t1.end_day"));

            Company company = new Company();
            company.setId(resultSet.getInt("t2.id"));
            company.setCreatedTime(resultSet.getDate("t2.created_time"));
            company.setName(resultSet.getString("t2.name"));
            company.setWebsite(resultSet.getString("t2.website"));
            company.setIntro(resultSet.getString("t2.intro"));
            company.setLogo(resultSet.getString("t2.logo"));

            project.setCompany(company);
            return project;
        };
    }
    @Override
    public boolean save(Project bean) {
        Integer id = bean.getId();
        List<Object> args = new ArrayList<>();
        args.add(bean.getName());
        args.add(bean.getIntro());
        args.add(bean.getWebsite());
        args.add(bean.getImage());
        args.add(bean.getBeginDay());
        args.add(bean.getEndDay());
        args.add(bean.getCompany().getId());
        String sql;
        if(id == null || id < 1){ //添加记录
            sql = "INSERT INTO project(name, intro, website, image, begin_day, end_day," +
                    "company_id) VALUES(?,?,?,?,?,?,?)";
        }else { //更新记录
            sql = "UPDATE project SET name=?, intro=?, website=?, image=?, begin_day=?, " +
                    "end_day=?, company_id=? WHERE id=?";
            args.add(id);
        }

        return DBUtil.getTpl().update(sql, args.toArray()) > 0;
    }

    @Override
    public Project get(Integer id) {
        String sql = querySql + "WHERE t1.id = ?";
        return DBUtil.getTpl().queryForObject(sql, rowMapper, id);
    }

    @Override
    public List<Project> list() {
        return DBUtil.getTpl().query(querySql, rowMapper);
    }
}
