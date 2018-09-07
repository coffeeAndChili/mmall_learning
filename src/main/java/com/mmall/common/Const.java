package com.mmall.common;

/**
 * Created by Administrator on 2018/9/5.
 */
public class Const {
    public static final String CURRENT_USER = "currentUser";

    public static final String USERNAME = "username";
    public static final String EMAIL = "email";

    //内部接口类进行常量分组，少量数据的时候，建议用这种方法
    public interface Role{
        int ROLE_CONSUMER = 0; //普通用户
        int ROLE_ADMIN = 1; //管理员
    }



}
