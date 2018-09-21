package com.mmall.controller.backend;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.service.IproductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Created by Administrator on 2018/9/10.
 */
@Controller
@RequestMapping(value = "/manage/product/")
public class ProductManageController {
    @Autowired
    private IUserService iUserService;
    @Autowired
    private IproductService iproductService;

    /**
     * 产品保存和更新接口
     * */
    @RequestMapping(value = "product_save.do" ,method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse productSave(HttpSession session , Product product){
        //验证是否登录
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(null==user){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"请先登录");
        }
        //验证是否是管理员
        if(iUserService.checkAdminRole(user).isSuccess()){
            //增加产品的逻辑
            return iproductService.saveOrUpdateProduct(product);
        }
        return ServerResponse.createByErrorMassage("没有管理员权限");
    }

    /**
     * 产品上下架接口
     * */
    @RequestMapping(value = "set_sale_status.do" ,method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse setSaleStatus(HttpSession session ,Integer productId , Integer status){
        //验证是否登录
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(null==user){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"请先登录");
        }
        //验证是否是管理员
        if(iUserService.checkAdminRole(user).isSuccess()){
            //产品上下架业务逻辑
            return iproductService.setSaleStatus(productId,status);
        }
        return ServerResponse.createByErrorMassage("没有管理员权限");
    }

    /**
     * 获取产品详情
     * */
    @RequestMapping(value = "get_detail.do" ,method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse getDetail(HttpSession session ,Integer productId){
        //验证是否登录
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(null==user){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"请先登录");
        }
        //验证是否是管理员
        if(iUserService.checkAdminRole(user).isSuccess()){
            //获取产品详情逻辑
            return null;
        }
        return ServerResponse.createByErrorMassage("没有管理员权限");
    }






}
