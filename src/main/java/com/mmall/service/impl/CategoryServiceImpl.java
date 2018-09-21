package com.mmall.service.impl;

import com.google.common.cache.LoadingCache;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.pojo.Category;
import com.mmall.service.ICategoryService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.SetUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Administrator on 2018/9/8.
 */
@Service("iCategoryService")
public class CategoryServiceImpl implements ICategoryService {

    private Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);
    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 新增一个类别
     * */
    public ServerResponse addCategory(String categoryName ,Integer parentId){
        if(parentId == null || StringUtils.isBlank(categoryName)){
            return ServerResponse.createByErrorMassage("参数错误");
        }
        Category category = new Category();
        category.setName(categoryName);
        category.setParentId(parentId);
        category.setStatus(true);
        int resultCount = categoryMapper.insert(category);
        if(resultCount>0){
            return ServerResponse.createBySuccess("添加品类成功");
        }
        return ServerResponse.createByErrorMassage("添加品类失败");
    }

    /**
     * 更新类别名称
     * */
    public ServerResponse updateCategoryName(Integer categoryId , String categoryName){
        if(categoryId==null || StringUtils.isBlank(categoryName)){
            return ServerResponse.createByErrorMassage("参数错误");
        }
        Category category = new Category();
        category.setId(categoryId);
        category.setName(categoryName);
        int resultCount = categoryMapper.updateByPrimaryKeySelective(category);
        if(resultCount>0){
            return ServerResponse.createBySuccessMessage("更新品类名称成功");
        }
        return ServerResponse.createByErrorMassage("更新品类名称失败");
    }

    /**
     * 根据categoryId查找下一层级的子节点
     * */
    public ServerResponse<List<Category>> getChildrenParallelCategory(Integer categoryId){
        List<Category> list = categoryMapper.selectCategoryChildrenByParentId(categoryId);
        if(CollectionUtils.isEmpty(list)){
            logger.info("未找到当前分类的子分类");
        }
        return ServerResponse.createBySuccess(list);
    }


    /**
     * 根据categoryId用递归算法查找所有子节点id和本节点的id
     * */
    public ServerResponse selectCategoryAndChilrenCategory(Integer categoryId){
        Set<Category> set = Sets.newHashSet();
        findChildCategory(set,categoryId);
        List<Integer> list = Lists.newArrayList();
        if(categoryId!=null){
            for(Category category : set){
                list.add(category.getId());
            }
        }
        return ServerResponse.createBySuccess(list);
    }

    /**
     * 递归算法
     * */
    private Set<Category> findChildCategory(Set<Category> set ,Integer categoryId){
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        if(category!=null){
            set.add(category);
        }
        //递归算法查找子节点。注意mybatis查询出来的集合，就算没有数据,也会返回一个长度为0的集合，所以不可能是null，不需要做null判断
        List<Category> list = categoryMapper.selectCategoryChildrenByParentId(categoryId);
        for(Category category1 : list){
            findChildCategory(set,category1.getId());
        }
        return set;
    }




}
