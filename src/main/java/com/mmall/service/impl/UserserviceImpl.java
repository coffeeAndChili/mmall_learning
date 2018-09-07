package com.mmall.service.impl;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.common.TokenCache;
import com.mmall.dao.UserMapper;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.UUID;

/**
 * Created by Administrator on 2018/9/3.
 */
@Service("iUserService")
public class UserserviceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public ServerResponse<User> login(String username, String password) {
        //首先验证用户名是否存在
        int resultCount = userMapper.checkUserName(username);
        if(resultCount == 0){
            return ServerResponse.createByErrorMassage("用户名不存在");
        }
        //密码登录MD5
        String md5password = MD5Util.MD5EncodeUtf8(password);
        User user = userMapper.selectLogin(username,md5password);
        //这里如果user为null，那么一定是密码错误
        if (user == null){
            return ServerResponse.createByErrorMassage("密码错误");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess("登录成功",user);
    }


    /**
     * register 注册
     * */
    public ServerResponse<String> register(User user){
        //这里首先验证用户名和email是否存在
        /*int resultCount = userMapper.checkUserName( user.getUsername());
        if(resultCount>0){
            return ServerResponse.createByErrorMassage("用户名已存在");
        }*/
        ServerResponse validResponse = this.checkValid(user.getUsername(),Const.USERNAME);
        if(!validResponse.isSuccess()){
            return validResponse;
        }
        validResponse = this.checkValid(user.getEmail(),Const.EMAIL);
        if(!validResponse.isSuccess()){
            return validResponse;
        }
        /*resultCount = userMapper.checkEmail(user.getEmail());
        if (resultCount>0){
            return ServerResponse.createByErrorMassage("邮箱已经存在");
        }*/
        user.setRole(Const.Role.ROLE_CONSUMER);
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
        int resultCount = userMapper.insert(user);
        if(resultCount == 0){
            return ServerResponse.createByErrorMassage("注册失败");
        }
        return ServerResponse.createBySuccessMessage("注册成功");
    }

    /**
     * 验证username和email，用于给前端提供实时验证
     * 返回false就是存在，返回true就是不存在
     * */
    public ServerResponse<String> checkValid(String str,String type){
        int resultCount;
        if(StringUtils.isNotBlank(type)){
            if (Const.USERNAME.equals(type)){
                resultCount = userMapper.checkUserName(str);
                if(resultCount>0){
                    return ServerResponse.createByErrorMassage("用户名已存在");
                }
            }else if(Const.EMAIL.equals(type)){
                resultCount = userMapper.checkEmail(str);
                if (resultCount>0){
                    return ServerResponse.createByErrorMassage("邮箱已经存在");
                }
            }
        }else {
            return ServerResponse.createByErrorMassage("参数错误");
        }
        return ServerResponse.createBySuccessMessage("校验成功");
    }


    /**
     * 根据用户名称查找密码提示问题
     * */
    public ServerResponse selectQuestion(String username){
        ServerResponse serverResponse = this.checkValid(username,Const.USERNAME);
        if (serverResponse.isSuccess()){
            return serverResponse.createBySuccessMessage("用户名不存在");
        }
        String question = userMapper.selectQuestionByUserName(username);
        if(StringUtils.isNotBlank(question)){
            return serverResponse.createBySuccess(question);
        }
        return  serverResponse.createByErrorMassage("密码提示问题是空的");
    }


    /**
     * 验证密码提示问题和答案是否正确
     * */
    public ServerResponse<String> checkAnswer(String username ,String question,String answer){
        int resultCount =  userMapper.checkAnswer(username,question,answer);
        if(resultCount>0){
            //问题回答正确
            String forgetToken = UUID.randomUUID().toString();
            TokenCache.setKey(TokenCache.TOKEN_PREFIX+username,forgetToken);
            return ServerResponse.createBySuccessMessage(forgetToken);
        }
        return ServerResponse.createByErrorMassage("回答错误");
    }

    public ServerResponse<String> forgetRestPassword(String username , String passwordNew , String token){
        //首先验证token
        if(StringUtils.isBlank(token)){
            return ServerResponse.createByErrorMassage("参数错误，需要传递token");
        }
        ServerResponse serverResponse = this.checkValid(username,Const.USERNAME);
        if (serverResponse.isSuccess()){
            return serverResponse.createBySuccessMessage("用户名不存在");
        }
        String nowToken = TokenCache.getKey(TokenCache.TOKEN_PREFIX+username);
        if(StringUtils.isBlank(nowToken)){
            return ServerResponse.createByErrorMassage("token无效或者过期");
        }

        if(StringUtils.equals(nowToken,token)){
            String md5NewPassword = MD5Util.MD5EncodeUtf8(passwordNew);
            int resultCount = userMapper.updatePasswordByUsername(username,passwordNew);
            if(resultCount>0){
                return ServerResponse.createBySuccessMessage("修改密码成功");
            }
        }else{
            return ServerResponse.createByErrorMassage("token错误，请从新获取");
        }
        return ServerResponse.createByErrorMassage("修改密码失败");
    }


    public ServerResponse<String> resetPassword(User user ,String passwordNew,String passwordOld){
        //这里首先验证旧密码是否正确，防止横向越权，所以要配合userid作为查询条件
        int resultCount =  userMapper.checkPassword(MD5Util.MD5EncodeUtf8(passwordOld),user.getId());
        if(resultCount==0){
            return ServerResponse.createByErrorMassage("旧密码错误");
        }
        user.setPassword(MD5Util.MD5EncodeUtf8(passwordNew));
        int updateCount = userMapper.updateByPrimaryKeySelective(user);
        if(updateCount>0){
            return ServerResponse.createBySuccessMessage("密码更新成功");
        }
        return ServerResponse.createByErrorMassage("密码更新失败");
    }


    public ServerResponse<User> updateInformation(User user){
        //username是不能被更新的
        //要验证email是否被其他用户使用
        int resultCount = userMapper.checkEmailByUserid(user.getEmail(),user.getId());
        if(resultCount>0){
            return ServerResponse.createByErrorMassage("email已经被使用，请更换email");
        }
        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setEmail(user.getEmail());
        updateUser.setPhone(user.getPhone());
        updateUser.setQuestion(user.getQuestion());
        updateUser.setAnswer(user.getAnswer());
        resultCount = userMapper.updateByPrimaryKeySelective(updateUser);
        if(resultCount>0){
            return ServerResponse.createBySuccess("更新成功",updateUser);
        }
        return ServerResponse.createByErrorMassage("更新个人信息失败");
    }



    public ServerResponse<User> getInformation(Integer userId){
        User user = userMapper.selectByPrimaryKey(userId);
        if(null == user){
            return ServerResponse.createByErrorMassage("没有找到用户信息");

        }
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess(user);
    }









}
