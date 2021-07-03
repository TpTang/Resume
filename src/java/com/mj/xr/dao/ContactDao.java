package com.mj.xr.dao;

import com.mj.xr.bean.Contact;
import com.mj.xr.bean.ContactFilterParam;
import com.mj.xr.bean.ContactListResult;

import java.util.List;

public interface ContactDao extends BaseDao<Contact>{
    //Contact这个模块特有的
    ContactListResult list(ContactFilterParam param);
    Boolean read(Integer id);
}
