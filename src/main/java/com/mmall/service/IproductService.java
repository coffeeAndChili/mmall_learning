package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;

/**
 * Created by Administrator on 2018/9/10.
 */
public interface IproductService {
    ServerResponse saveOrUpdateProduct(Product product);
    /**
     * 产品上下架接口
     * @param productId 主键
     * @param productStatus 状态
     * @return ServerResponse<String>
     * */
    ServerResponse<String> setSaleStatus(Integer productId,Integer productStatus);
}
