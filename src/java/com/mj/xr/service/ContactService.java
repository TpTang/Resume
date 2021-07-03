package com.mj.xr.service;

import com.mj.xr.bean.Contact;
import com.mj.xr.bean.ContactFilterParam;
import com.mj.xr.bean.ContactListResult;

import java.util.List;

public interface ContactService extends BaseService<Contact>{

    //contact这个模块特有的东西
    ContactListResult list(ContactFilterParam param);
    Boolean read(Integer id);
}
