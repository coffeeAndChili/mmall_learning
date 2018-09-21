package com.mmall.service.impl;

import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Product;
import com.mmall.service.IproductService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2018/9/10.
 */
@Service(value = "iProductService")
public class ProductServiceImpl implements IproductService {

    @Autowired
    private ProductMapper productMapper;

    /**
     * 用一个方法来搞定保存和更新的功能
     * */
    public ServerResponse saveOrUpdateProduct(Product product){
        if(null != product){
            //讲subImages中的第一个图片赋值给主图
            if(StringUtils.isNotBlank(product.getSubImages())){
                String[] subArray = product.getSubImages().split(",");
                if(subArray.length>0){
                    product.setMainImage(subArray[0]);
                }
            }
            if (null != product.getId()){
                //进行更新
                int rowCount = productMapper.updateByPrimaryKey(product);
                if(rowCount>0){
                    return ServerResponse.createBySuccessMessage("更新产品成功");
                }else {
                    return ServerResponse.createByErrorMassage("更新产品失败");
                }
            }else {
                //进行保存
                int rowCount = productMapper.insert(product);
                if(rowCount>0){
                    return ServerResponse.createBySuccessMessage("保存产品成功");
                }else {
                    return ServerResponse.createByErrorMassage("保存产品失败");
                }
            }
        }
        return ServerResponse.createByErrorMassage("产品参数不正确");
    }

    /**
     * 产品上下架接口
     * @param productId 主键
     * @param productStatus 状态
     * @return ServerResponse<String>
     * */
    public ServerResponse<String> setSaleStatus(Integer productId,Integer productStatus){
        if(null == productId || null == productStatus){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product = new Product();
        product.setId(productId);
        product.setStatus(productStatus);
        int resultCount = productMapper.updateByPrimaryKeySelective(product);
        if(resultCount>0){
            return ServerResponse.createBySuccessMessage("更新成功");
        }else {
            return ServerResponse.createByErrorMassage("更新失败");
        }
    }








}
