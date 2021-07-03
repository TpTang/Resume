package com.mj.xr.service;

import com.mj.xr.bean.User;

public interface UserService extends BaseService<User>{
    User get(User user);
}
