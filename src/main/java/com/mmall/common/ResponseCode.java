package com.mmall.common;

/**
 * Created by Administrator on 2018/9/3.
 * 为了响应编码的枚举类
 */
public enum  ResponseCode {

    SUCCESS(0,"SUCCESS"),
    ERROR(1,"ERROR"),
    NEED_LOGIN(10,"NEED_LOGIN"),
    //参数错误 illegal argument
    ILLEGAL_ARGUMENT(2,"ILLEGAL_ARGUMENT");

    private final int code;
    private final String desc;

    ResponseCode(int code,String desc){
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }
    public String getDesc() {
        return desc;
    }
}
