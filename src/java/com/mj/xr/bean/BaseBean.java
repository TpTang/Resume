package com.mj.xr.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.text.SimpleDateFormat;
import java.util.Date;

public class BaseBean {
    protected Integer id;
    protected Date createdTime;

    public void setId(Integer id) {
        this.id = id;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Integer getId() {
        return id;
    }

//    @JsonIgnore
    public Date getCreatedTime() {
        return createdTime;
    }

    @JsonIgnore //不加这个注解会递归调用导致栈溢出
    public String getJson() throws Exception{
        ObjectMapper mapper = new ObjectMapper();
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd")); //声明日期转换格式
        return mapper.writeValueAsString(this).replace("\"", "'"); //底层原理：调用对象的get方法
                                        //this：调用当前方法的对象
    }
}
