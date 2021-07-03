package com.mj.xr.service.impl;

import com.mj.xr.bean.Contact;
import com.mj.xr.bean.ContactFilterParam;
import com.mj.xr.bean.ContactListResult;
import com.mj.xr.dao.ContactDao;
import com.mj.xr.service.BaseService;
import com.mj.xr.service.ContactService;

import java.util.List;

public class ContactServiceImpl extends BaseServiceImpl<Contact> implements ContactService {
    @Override
    public ContactListResult list(ContactFilterParam param) {
        return ((ContactDao)dao).list(param);
    }

    @Override
    public Boolean read(Integer id) {
        return ((ContactDao)dao).read(id);
    }
}
