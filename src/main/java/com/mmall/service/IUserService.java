package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;


/**
 * Created by Administrator on 2018/9/3.
 */
public interface IUserService {

    /**
     * 登录接口
     * @param username 用户名
     * @param password 密码
     * @return ServerResponse<User>
     * */
    ServerResponse<User> login(String username , String password);
    ServerResponse<String> register(User user);
    ServerResponse<String> checkValid(String str,String type);
    ServerResponse selectQuestion(String username);
    ServerResponse<String> checkAnswer(String username ,String question,String answer);
    ServerResponse<String> forgetRestPassword(String username , String passwordNew , String token);
    ServerResponse<String> resetPassword(User user ,String passwordNew,String passwordOld);
    ServerResponse<User> updateInformation(User user);
    /**
     * 根据userId查找用户信息
     *  @param userId
     *  @return User
     * */
    ServerResponse<User> getInformation(Integer userId);

}
