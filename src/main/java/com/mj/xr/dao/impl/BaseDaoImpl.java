package com.mj.xr.dao.impl;

import com.mj.xr.dao.BaseDao;
import com.mj.xr.util.DBUtil;
import com.mj.xr.util.StringUtil;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * 抽取具体实现类的公共代码
 * @param <T> 为了让implements的接口知道自己被泛型了什么
 */
public abstract class BaseDaoImpl<T> implements BaseDao<T> {
    protected String table(){ //默认实现拿到表名，子类依然可以重写实现自己想要的拿表逻辑
        ParameterizedType type = (ParameterizedType) getClass().getGenericSuperclass(); //到时候子类调用这个方法：getClass()获得子类的Class
        Class beanCls = (Class) type.getActualTypeArguments()[0];//本项目不需要拿到整个列表
        return StringUtil.underlineCase(beanCls.getSimpleName()); //拿到泛型类去转表名
    }
    /**
     * 根据id删除单条记录
     */
    @Override
    public boolean remove(Integer id){
        String sql = "DELETE FROM "+ table() +" WHERE id=?";
        return DBUtil.getTpl().update(sql, id) > 0;
    }
    /**
     * 批量删除记录
     */
    @Override
    public boolean remove(List<Integer> ids){
        List<Integer> args = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("DELETE FROM ").append(table()).append(" WHERE id in (");
        for(Integer id : ids){
            sql.append("?,");
            args.add(id);
        }
        sql.replace(sql.length()-1, sql.length(), ")");
        return DBUtil.getTpl().update(sql.toString(), args.toArray()) > 0;
    }

    /**
     * 查看表的记录总条数
     */
    @Override
    public int count() {
        String sql = "SELECT count(*) FROM"+table();
        return DBUtil.getTpl().queryForObject(sql, Integer.class);
    }
}
