package com.mj.xr.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.text.SimpleDateFormat;

public class Contact extends BaseBean{
    private String name;
    private String email;
    private String comment;
    private String subject;
    private Boolean alreadyRead = false; //数据库处理Boolean：true-1 false-0

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Boolean getAlreadyRead() {
        return alreadyRead;
    }

    public void setAlreadyRead(Boolean alreadyRead) {
        this.alreadyRead = alreadyRead;
    }

    @JsonIgnore //不加这个注解会递归调用导致栈溢出
    public String getJson() throws Exception{
        ObjectMapper mapper = new ObjectMapper();
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")); //声明日期转换格式
        return mapper.writeValueAsString(this).replace("\"", "'"); //底层原理：调用对象的get方法
        //this：调用当前方法的对象
    }

}
