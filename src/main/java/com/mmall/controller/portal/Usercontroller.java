package com.mmall.controller.portal;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.common.TokenCache;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.util.MD5Util;
import org.omg.CORBA.Object;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Created by Administrator on 2018/9/3.
 */
@Controller
@RequestMapping("/user/")
public class Usercontroller {


    /**
     *  这里的iUserService要和UserserviceImpl中@Service("iUserService")保持一致
     * */
    @Autowired
    private IUserService iUserService;



    /**
     *
     * 登录功能
     * @ResponseBody 返回值序列化成json
     *
     * */
    @RequestMapping(value = "login.do" , method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login(String username , String password , HttpSession session){
        ServerResponse<User> serverResponse = iUserService.login(username,password);
        if(serverResponse.isSuccess()){
            session.setAttribute(Const.CURRENT_USER,serverResponse);
        }
        return serverResponse;
    }

    /**
     * 退出功能
     * */
    @RequestMapping(value = "logout.do" , method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<String> logout(HttpSession session){
        session.removeAttribute(Const.CURRENT_USER);
        return ServerResponse.createBySuccess();
    }

    /**
     * 注册
     * */
    @RequestMapping(value = "register.do" , method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<String> register(User user){
        return iUserService.register(user);
    }

    /**
     * 校验用户名和邮箱
     * */
    @RequestMapping(value = "check_valid.do" , method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<String> checkValid(String str,String type){
        return iUserService.checkValid(str,type);
    }

    /**
     * 获取用户登录信息接口
     * */
    @RequestMapping(value = "get_user_info.do" , method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<User> getUserInfo(HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user!=null){
            return ServerResponse.createBySuccess(user);
        }
        return ServerResponse.createBySuccessMessage("用户未登录");
    }

    /**
     * 忘记密码,根据用户名查找密码提示问题
     * */
    @RequestMapping(value = "forget_get_question.do" , method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<String> forgetGetQuestion(String username){
        return iUserService.selectQuestion(username);
    }

    /**
     * 验证问题和答案是否正确
     * */
    @RequestMapping(value = "forget_check_answer.do" , method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<String> forgetCheckAnswer(String username ,String question,String answer){
        return iUserService.checkAnswer(username,question,answer);
    }

    /**
     * 修改密码
     * */
    @RequestMapping(value = "forget_rest_password.do" , method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<String> forgetRestPassword(String username , String passwordNew , String token){
        return iUserService.forgetRestPassword(username,passwordNew,token);
    }

    /**
     * 登录状态的重置密码
     * */
    @RequestMapping(value = "reset_password.do" , method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<String> resetPassword(HttpSession session , String passwordOld , String passwordNew){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(null == user){
            return ServerResponse.createByErrorMassage("用户未登录");
        }
        return iUserService.resetPassword(user,passwordNew,passwordOld);
    }

    /**
     * 更新个人用户信息
     * */
    @RequestMapping(value = "update_information.do" , method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<User> updateInformation(HttpSession session ,User user){
        //这里首先判断用户是否登录
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if(currentUser==null){
            return ServerResponse.createByErrorMassage("用户未登录");
        }
        //这里我们为了防止越权问题，所以不会使用前端传回来的userId和username，而是从session中取出userId和username，更新到这个对象中。防止id和username被变化
        user.setId(currentUser.getId());
        user.setUsername(currentUser.getUsername());
        ServerResponse<User> serverResponse = iUserService.updateInformation(user);
        //这里需要判断是否成功，如果成功则需要更新session
        if(serverResponse.isSuccess()){
            serverResponse.getData().setUsername(currentUser.getUsername());
            session.setAttribute(Const.CURRENT_USER,serverResponse.getData());
        }
        return serverResponse;
    }

    /**
     * 获取用户的详细信息
     * 在更新个人用户信息的时候，首先会先调用获取个人信息的接口，这里判断用户未登录，未强制他登录，所以updateInformation接口就不需要强制登录了
     *
     * */
    @RequestMapping(value = "get_information.do" , method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<User> getInformation(HttpSession session){
        //判断用户是否登录，如果未登录需要强制登录
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(null==user){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,需要强制登录 status=10");
        }
        return iUserService.getInformation(user.getId());
    }



}
