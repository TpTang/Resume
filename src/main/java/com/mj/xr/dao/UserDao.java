package com.mj.xr.dao;

import com.mj.xr.bean.User;

public interface UserDao extends BaseDao<User>{
    User get(User user);
}
