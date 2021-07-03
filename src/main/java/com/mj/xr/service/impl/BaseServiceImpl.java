package com.mj.xr.service.impl;

import com.mj.xr.dao.BaseDao;
import com.mj.xr.service.BaseService;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public abstract class BaseServiceImpl<T> implements BaseService<T> {
    protected final BaseDao<T> dao = newDao();
    protected BaseDao<T> newDao(){

        //通过反射创建实例对象
        try {
            String className = getClass().getName().replace(".service.", ".dao.")
                    .replace("ServiceImpl", "DaoImpl"); //替换时写严谨点
            return (BaseDao<T>) Class.forName(className).getConstructor().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * 根据id删除单条记录
     */
    public boolean remove(Integer id){
        return dao.remove(id);
    }
    /**
     * 批量删除记录
     */
    public boolean remove(List<Integer> ids){
        return dao.remove(ids);
    }

    /**
     * 添加或更新
     */
    public boolean save(T bean){
        return dao.save(bean);
    }

    /**
     * 获取单个对象
     */
    public T get(Integer id){
        return dao.get(id);
    }
    /**
     * 获取多个对象
     */
    public List<T> list(){
        return dao.list();
    }
    /**
     * 获取统计值
     */
    public int count(){
        return dao.count();
    }
}
