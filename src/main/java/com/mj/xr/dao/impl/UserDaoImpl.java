package com.mj.xr.dao.impl;

import com.mj.xr.bean.User;
import com.mj.xr.dao.UserDao;
import com.mj.xr.util.DBUtil;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import java.util.ArrayList;
import java.util.List;

public class UserDaoImpl extends BaseDaoImpl<User> implements UserDao {
    @Override
    public boolean save(User bean) {
        Integer id = bean.getId();
        List<Object> args = new ArrayList<>();
        args.add(bean.getPassword());
        args.add(bean.getEmail());
        args.add(bean.getPhoto());
        args.add(bean.getIntro());
        args.add(bean.getName());
        args.add(bean.getAddress());
        args.add(bean.getBirthday());
        args.add(bean.getPhone());
        args.add(bean.getJob());
        args.add(bean.getTrait());
        args.add(bean.getInterests());
        String sql;
        if(id == null || id < 1){ //添加
            sql = "INSERT INTO user(password, email, photo, intro, name, address, birthday," +
                    "phone, job, trait, interests) VALUES(?,?,?,?,?,?,?,?,?,?,?)";
        }else { //更新
            sql = "UPDATE user SET password=?, email=?, photo=?, intro=?, name=?, address=?," +
                    " birthday=?, phone=?, job=?, trait=?, interests=? WHERE id=?";
            args.add(id);
        }
        return DBUtil.getTpl().update(sql, args.toArray()) > 0;
    }

    @Override
    public User get(Integer id) {
        String sql = "SELECT id, created_time, password, email, photo, intro, name, address, birthday," +
                "phone, job, trait, interests FROM user WHERE id=?";
        return DBUtil.getTpl().queryForObject(sql, new BeanPropertyRowMapper<>(User.class), id);
    }

    @Override
    public List<User> list() {
        String sql = "SELECT id, created_time, password, email, photo, intro, name, address, birthday," +
                "phone, job, trait, interests FROM user";
        return DBUtil.getTpl().query(sql, new BeanPropertyRowMapper<>(User.class));
    }

    @Override
    public User get(User user) {
        String sql = "SELECT id, created_time, password, email, photo, intro, name, address, birthday," +
                "phone, job, trait, interests FROM user WHERE email=? AND password=?";
        List<User> users = DBUtil.getTpl().query(sql, new BeanPropertyRowMapper<>(User.class),user.getEmail(), user.getPassword());
        return users.isEmpty() ? null : users.get(0); //不能使用queryForObject：很可能查到空记录，空记录不能转成bean
    }
}
