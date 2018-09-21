package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.Category;

import java.util.List;

/**
 * Created by Administrator on 2018/9/8.
 */
public interface ICategoryService {
    ServerResponse addCategory(String categoryName , Integer parentId);
    ServerResponse updateCategoryName(Integer categoryId , String categoryName);
    ServerResponse<List<Category>> getChildrenParallelCategory(Integer categoryId);
    ServerResponse selectCategoryAndChilrenCategory(Integer categoryId);
}
