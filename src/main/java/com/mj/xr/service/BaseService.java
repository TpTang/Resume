package com.mj.xr.service;

import java.util.List;

public interface BaseService<T> {
    /**
     * 根据id删除单条记录
     */
    boolean remove(Integer id);
    /**
     * 批量删除记录
     */
    boolean remove(List<Integer> ids);

    /**
     * 添加或更新
     */
    boolean save(T bean);

    /**
     * 获取单个对象
     */
    T get(Integer id);
    /**
     * 获取多个对象
     */
    List<T> list();
    /**
     * 获取统计值
     */
    int count();
}
