package com.mj.xr.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Experience extends BaseBean{
    private String job;
    private String intro;
    private Company company; //使用Company更面向对象
    private Date beginDay;
    private Date endDay;

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

//    @JsonIgnore
//    public Company getCompany() {
//        return company;
//    }
//
//    public Integer getCompanyId(){
//        return company.id;
//    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Date getBeginDay() {
        return beginDay;
    }

    //设置JSP页面时间转换（bean->JSP）
//    public String getBeginDayString(){
//        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
//        return fmt.format(beginDay);
//    }

    public void setBeginDay(Date beginDay) {
        this.beginDay = beginDay;
    }

    public Date getEndDay() {
        return endDay;
    }

    //设置JSP页面时间转换（bean->JSP）
//    public String getEndDayString(){
//        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
//        return fmt.format(endDay);
//    }

    public void setEndDay(Date endDay) {
        this.endDay = endDay;
    }
}
