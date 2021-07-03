package com.mj.xr.dao.impl;

import com.mj.xr.bean.Contact;
import com.mj.xr.bean.ContactFilterParam;
import com.mj.xr.bean.ContactListResult;
import com.mj.xr.dao.ContactDao;
import com.mj.xr.util.DBUtil;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ContactDaoImpl extends BaseDaoImpl<Contact> implements ContactDao {
    @Override
    public boolean save(Contact bean) {
        Integer id = bean.getId();
        List<Object> args = new ArrayList<>();
        args.add(bean.getName());
        args.add(bean.getEmail());
        args.add(bean.getComment());
        args.add(bean.getSubject());
        args.add(bean.getAlreadyRead());
        String sql;
        if (id == null || id < 1){ //添加
            sql = "INSERT INTO contact(name, email, comment, subject, already_read) VALUES(?,?,?,?,?)";
        }else { //更新
            sql = "UPDATE set name=?, email=?, comment=?, subject=?, already_read=? WHERE id = ?";
            args.add(id);
        }
        return DBUtil.getTpl().update(sql, args.toArray()) > 0;
    }

    @Override
    public Contact get(Integer id) {
        String sql = "SELECT id, created_time, name, email, comment, subject, already_read FROM contact WHERE id=?";
        return DBUtil.getTpl().queryForObject(sql, new BeanPropertyRowMapper<>(Contact.class), id);
    }

    @Override
    public List<Contact> list() {
        String sql = "SELECT id, created_time, name, email, comment, subject, already_read FROM contact";
        return DBUtil.getTpl().query(sql, new BeanPropertyRowMapper<>(Contact.class));
    }

    //根据传入的筛选条件查询数据列表
    @Override
    public ContactListResult list(ContactFilterParam param) {
        ContactListResult result = new ContactListResult();
        //根据筛选条件动态生成sql语句
        StringBuilder sql = new StringBuilder();
        StringBuilder condition = new StringBuilder();
        List<Object> args = new ArrayList<>(); //参数
        sql.append("SELECT id, created_time, name, email, comment, subject, already_read FROM contact WHERE 1 = 1 ");

        if(param.getBeginDay() != null){
            condition.append("AND created_time >= ? ");
            args.add(param.getBeginDay());
            result.setBeginDay(param.getBeginDay());
        }

        if (param.getEndDay() != null){
            condition.append("AND created_time <= ? ");
            args.add(param.getEndDay());
            result.setEndDay(param.getEndDay());
        }

        String keyword = param.getKeyword();
        if(keyword != null && keyword.length() > 0){
            result.setKeyword(param.getKeyword());
            condition.append("AND (name LIKE ? OR subject LIKE ? OR comment LIKE ? OR email LIKE ?) ");
            keyword = "%"+keyword+"%";
            args.add(keyword);
            args.add(keyword);
            args.add(keyword);
            args.add(keyword);

        }
        Integer read = param.getAlreadyRead();
        if( read != null && read < ContactFilterParam.READ_ALL){
            condition.append("AND already_read=? ");
            args.add(read);
            result.setAlreadyRead(read);
        }

        Integer pageNo = param.getPageNo();
        if(pageNo == null || pageNo < 1){
            pageNo = 1; //默认展示第一页
        }
        Integer pageSize = param.getPageSize();
        if(pageSize == null || pageSize < 10){
            pageSize = 10; //默认展示10条
        }
        //在分页之前做总留言数、总页数
        String countSql = "SELECT COUNT(*) FROM contact WHERE 1= 1 " + condition;
        Integer totalCount = DBUtil.getTpl().queryForObject(countSql, Integer.class, args.toArray());
        if(totalCount == 0) return result; //没有符合条件的记录就不用往下走了
        result.setTotalCount(totalCount);
        result.setTotalPages((totalCount + pageSize - 1) / pageSize);


        condition.append("LIMIT ?, ?");
        args.add((pageNo - 1) * pageSize);
        args.add(pageSize);
        result.setPageNo(pageNo);
        result.setPageSize(pageSize);

        sql.append(condition);
        List<Contact> contacts = DBUtil.getTpl().query(sql.toString(), new BeanPropertyRowMapper<>(Contact.class), args.toArray());
        result.setContacts(contacts);
        return result;
    }

    //修改阅读状态为“已读”
    @Override
    public Boolean read(Integer id) {
        String sql = "UPDATE contact SET already_read = 1 WHERE id = ?";
        return DBUtil.getTpl().update(sql, id) > 0;
    }
}
