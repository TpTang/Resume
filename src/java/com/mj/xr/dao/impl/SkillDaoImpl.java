package com.mj.xr.dao.impl;

import com.mj.xr.bean.Skill;
import com.mj.xr.dao.SkillDao;
import com.mj.xr.util.DBUtil;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import java.util.ArrayList;
import java.util.List;

public class SkillDaoImpl extends BaseDaoImpl<Skill> implements SkillDao {

    @Override
    public boolean save(Skill bean) {
        Integer id = bean.getId();
        List<Object> args = new ArrayList<>();
        args.add(bean.getName());
        args.add(bean.getLevel());
        String sql;
        if(id == null || id < 1){ //添加
            sql = "INSERT INTO skill(name, level) VALUES(?, ?)";
        }else{ //更新
            sql = "UPDATE skill SET name = ?, level = ? WHERE id = ?";
            args.add(id);
        }
        return DBUtil.getTpl().update(sql, args.toArray()) > 0;
    }

    @Override
    public Skill get(Integer id) {
        String sql = "SELECT id, created_time, name, level FROM skill WHERE id = ?";
        return DBUtil.getTpl().queryForObject(sql, new BeanPropertyRowMapper<>(Skill.class), id);
    }

    @Override
    public List<Skill> list() {
        String sql = "SELECT id, created_time, name, level FROM skill";
        return DBUtil.getTpl().query(sql, new BeanPropertyRowMapper<>(Skill.class));
    }
}
