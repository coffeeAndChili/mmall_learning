package com.mmall.controller.backend;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.ICategoryService;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Created by Administrator on 2018/9/8.
 */
@Controller
@RequestMapping("/manage/category/")
public class CategoryManageController {

    @Autowired
    private IUserService iUserService;
    @Autowired
    private ICategoryService iCategoryService;

    /**
     * 新增一个类别
     * @RequestMapping 不指定method的话默认是get请求
     * @RequestParamm 作用1：前端和后台的字段名称不一致时，可以使用value="前端字段名称" 用来和方法中的参数进行绑定
     * 也可以通过defaultValue来指定默认值
     * */
    @RequestMapping(value = "add_category.do")
    @ResponseBody
    public ServerResponse addCategory(HttpSession session , String categoryName ,@RequestParam(value="parentId",defaultValue="0") Integer parentId){
        //首先判断登录
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(null==user){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"请先登录");
        }
        //验证是否是管理员
        ServerResponse serverResponse = iUserService.checkAdminRole(user);
        if (serverResponse.isSuccess()){
            return iCategoryService.addCategory(categoryName,parentId);
        }else {
            return ServerResponse.createByErrorMassage("没有管理员权限");
        }
    }

    /**
     * 修改类别名称
     * */
    @RequestMapping(value = "set_category_name.do")
    @ResponseBody
    public ServerResponse setCategoryName(HttpSession session ,Integer categoryId , String categoryName){
        //首先判断登录
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(null==user){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"请先登录");
        }
        //验证是否是管理员
        ServerResponse serverResponse = iUserService.checkAdminRole(user);
        if (serverResponse.isSuccess()){
            //更新categoryNamme
            return iCategoryService.updateCategoryName(categoryId,categoryName);
        }else {
            return ServerResponse.createByErrorMassage("没有管理员权限");
        }
    }

    /**
     * 根据节点id,查找下一层级的子节点，不需要递归
     * */
    @RequestMapping(value = "get_children_parallel_category.do" ,method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse getChildrenParallelCategory(HttpSession session ,@RequestParam(value = "categoryId" ,defaultValue = "0") Integer categoryId){
        //首先判断登录
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(null==user){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"请先登录");
        }
        //验证是否是管理员
        ServerResponse serverResponse = iUserService.checkAdminRole(user);
        if (serverResponse.isSuccess()){
            //根据categoryId查询下一层级的节点，不递归
            return iCategoryService.getChildrenParallelCategory(categoryId);
        }else {
            return ServerResponse.createByErrorMassage("没有管理员权限");
        }
    }

    /**
     * 根据categoryId递归查询当前节点下的所有子节点
     * */
    @RequestMapping(value = "get_category_and_deep_children_category.do")
    @ResponseBody
    public ServerResponse getCategoryAndDeepChildrenCategory(HttpSession session ,@RequestParam(value = "categoryId" ,defaultValue = "0") Integer categoryId){
        //首先判断登录
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(null==user){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"请先登录");
        }
        //验证是否是管理员
        ServerResponse serverResponse = iUserService.checkAdminRole(user);
        if (serverResponse.isSuccess()){
            //根据categoryId递归查询所有下层节点
            return iCategoryService.selectCategoryAndChilrenCategory(categoryId);
        }else {
            return ServerResponse.createByErrorMassage("没有管理员权限");
        }
    }
















}
