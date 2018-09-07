package com.mmall.dao;

import com.mmall.pojo.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    //校验用户名是否存在
    int checkUserName(String username);
    //校验邮箱是否存在
    int checkEmail(String email);

    //用户登录
    User selectLogin(@Param("username") String userName ,@Param("password") String passWord);

    //根据用户名称，返回密码提示问题
    String selectQuestionByUserName(String username);

    //验证密码提示问题和答案是否正确
    int checkAnswer(@Param("username") String username ,@Param("question") String question,@Param("answer") String answer);

    //修改密码
    int updatePasswordByUsername(@Param("username") String username ,@Param("password") String password);

    //验证密码是否正确
    int checkPassword(@Param("password")String password , @Param("userId")Integer  userId);

    //校验email是否被其他人使用
    int checkEmailByUserid(@Param("email")String email , @Param("userId")Integer  userId);





}