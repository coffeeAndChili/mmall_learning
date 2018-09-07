package com.mmall.common;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import java.io.Serializable;

/**
 * Created by Administrator on 2018/9/3.
 *
 * @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL) 保证序列化json的时候，如果是null的字段，key也会消失
 *
 *
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ServerResponse<T> implements Serializable {

    private int status;
    private String msg;
    private T data;

    private ServerResponse(int status){
        this.status = status;
    }

    private ServerResponse(int status , T data){
        this.status = status;
        this.data = data;
    }

    private ServerResponse(int status , String msg){
        this.status = status;
        this.msg = msg;
    }

    private ServerResponse(int status , String msg , T data){
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    //@JsonIgnore 使被修饰的方法返回值不被序列化显示到json中
    @JsonIgnore
    public boolean isSuccess(){
        return this.status == ResponseCode.SUCCESS.getCode() ;
    }

    public int getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }

    public static <T> ServerResponse<T> createBySuccess(){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode());
    }

    public static <T> ServerResponse<T> createBySuccessMessage(String msg){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(),msg);
    }

    public static <T> ServerResponse<T> createBySuccess(T data){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(),data);
    }

    public static <T> ServerResponse<T> createBySuccess(String msg ,T data ){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(),msg,data);
    }

    /**
     * 返回错误code和错误信息
     * 这里是公共的错误
     * */
    public static <T> ServerResponse<T> createByError(){
        return new ServerResponse<T>(ResponseCode.ERROR.getCode(),ResponseCode.ERROR.getDesc());
    }

    /**
     * 这里传入了一个参数
     * 是用于自定义非公共的错误信息
     * */
    public static <T> ServerResponse<T> createByErrorMassage(String errorMessage){
        return new ServerResponse<T>(ResponseCode.ERROR.getCode(),errorMessage);
    }

    public static <T> ServerResponse<T> createByErrorCodeMessage(int errorCode,String errorMessage){
        return new ServerResponse<T>(errorCode,errorMessage);
    }




}
