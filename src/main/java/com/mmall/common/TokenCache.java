package com.mmall.common;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2018/9/6.
 * 此类用于控制tonken的有效期
 */
public class TokenCache {
    //声明日志
    private static Logger logger = LoggerFactory.getLogger(TokenCache.class);

    public static final String TOKEN_PREFIX = "token_";

    /**
     * 此处声明了一个静态的内存块
     * LoadingCache 是guava里面的本地缓存类
     * CacheBuilder 是guava里面的创建缓存的工具类
     * initialCapacity()设置缓存的初始化容量
     * maximumSize() 设置最大容量，当缓存超过最大容量，guave的cache将会使用LRU算法来移除最少使用的缓存项
     * expireAfterAccess() 设置缓存的最长有效期
     * */
    private static LoadingCache<String,String> localCache = CacheBuilder.newBuilder().initialCapacity(1000).maximumSize(10000).expireAfterAccess(12,TimeUnit.HOURS)
            .build(new CacheLoader<String, String>() {
                //这个方法是默认的数据加载实现，当调用get取值的时候哦当key没有对应的值，就会调用这个方法进行加载
                @Override
                public String load(String s) throws Exception {
                    return "null";
                }
            });

    public static void setKey(String key ,String value){
        localCache.put(key,value);
    }

    public static String getKey(String key){
        String value = null;
        try{
            value = localCache.get(key);
            if(value.equals("null")){
                return null;
            }
            return value;
        }catch (Exception e){
            logger.error("localCache get error",e);
        }
        return null;
    }

}
