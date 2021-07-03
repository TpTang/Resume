package com.mj.xr.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Education extends BaseBean{
    private String name;
    private String intro;
    private Date beginDay;
    private Date endDay;
    /**
     * 0:其他
     * 1:小学
     * 2:初中
     * 3:高中
     * 4:中专
     * 5:大专
     * 6:本科
     * 7:硕士
     * 8:博士
     */
    private Integer type;

    public void setName(String name) {
        this.name = name;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public void setBeginDay(Date beginDay) {
        this.beginDay = beginDay;
    }

    public void setEndDay(Date endDay) {
        this.endDay = endDay;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getIntro() {
        return intro;
    }

    public Date getBeginDay() {
        return beginDay;
    }

    //或者在后台处理jsp页面的日期转换
    @JsonIgnore
    public String getBeginDateString(){
        return new SimpleDateFormat("yyyy-MM-dd").format(beginDay);
    }

    public Date getEndDay() {
        return endDay;
    }

    @JsonIgnore
    public String getEndDateString(){
        return new SimpleDateFormat("yyyy-MM-dd").format(endDay);
    }

    public Integer getType() {
        return type;
    }
    //在后台处理jsp页面调用getType()时转换输出
    @JsonIgnore //Java对象转换成JSON时忽略此get()
    public String getTypeString(){
        switch(type){
            case 1 : return "小学";
            case 2 : return "初中";
            case 3 : return "高中";
            case 4 : return "中专";
            case 5 : return "大专";
            case 6 : return "本科";
            case 7 : return "硕士";
            case 8 : return "博士";
            default: return "其他";
        }
    }

    //实现Bean对象转JavaScript对象[JSON] => 改写到父类去供所有bean使用
//    @Override
//    public String toString() {
        //方式一：重写toString,返回JS对象类型字符串
//        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
//        String beginDayString = fmt.format(beginDay);
//        String endDayString = fmt.format(endDay);
//        return "{name:'"+name+"', intro:'"+intro+"', " +
//                "beginDay:'"+beginDayString+"',endDay:'"+endDayString+"', type:"+type+"}";

        //方式二：第三方库
//        try {
//            ObjectMapper mapper = new ObjectMapper();
//            mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd")); //声明日期转换格式
//            return mapper.writeValueAsString(this).replace("\"", "'"); //底层原理：调用对象的get方法
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
}
