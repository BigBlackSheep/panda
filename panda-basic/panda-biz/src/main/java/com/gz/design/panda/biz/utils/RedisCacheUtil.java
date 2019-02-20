package com.gz.design.panda.biz.utils;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.stereotype.Component;

@Component
public class RedisCacheUtil {

    public final static String CAHCENAME = "CACHE|";//缓存名

    //强制人工复生缓存
    public final static String CAHCENAME_SYS_REVIEW = "CAHCENAME_SYS_REVIEW";//缓存名
    public final static String CACHE_USERID = "CACHE_USERID|";//缓存名
    public final static String CACHE_PRODUCT_SHOWID = "CACHE_PRODUCT_SHOWID|";//缓存名
    public final static String CACHE_PRODUCT_PERIOD = "CACHE_PRODUCT_PERIOD|";//缓存名
    public final static String CACHE_PRODUCT_SHOWID_ADDCONF = "CACHE_PRODUCT_SHOWID_ADDCONF|";//缓存名

    @Autowired
    @Qualifier("stringRedisTemplate")
    private StringRedisTemplate redisTemplate;

    public <T> boolean putCache(String key, T obj) {
        final byte[] bkey = key.getBytes();
        final byte[] bvalue = ProtoStuffSerializerUtil.serialize(obj);
        return redisTemplate.execute(
                (RedisCallback<Boolean>) connection -> connection.setNX(bkey, bvalue));
    }

    public <T> boolean putCacheWithExpireTime(String key, T obj, final long expireTime) {
        final byte[] bkey = key.getBytes();
        final byte[] bvalue = ProtoStuffSerializerUtil.serialize(obj);
        return redisTemplate.execute(
                (RedisCallback<Boolean>) connection -> connection.setEx(bkey, expireTime, bvalue));
    }

    public <T> boolean putListCache(String key, List<T> objList) {
        final byte[] bkey = key.getBytes();
        final byte[] bvalue = ProtoStuffSerializerUtil.serializeList(objList);
        return redisTemplate.execute(
                (RedisCallback<Boolean>) connection -> connection.setNX(bkey, bvalue));
    }

    public <T> boolean putListCacheWithExpireTime(String key, List<T> objList,
            final long expireTime) {
        final byte[] bkey = key.getBytes();
        final byte[] bvalue = ProtoStuffSerializerUtil.serializeList(objList);
        return redisTemplate.execute(
                (RedisCallback<Boolean>) connection -> connection.setEx(bkey, expireTime, bvalue));
    }

    public <T> T getCache(final String key, Class<T> targetClass) {
        byte[] result = redisTemplate.execute(
                (RedisCallback<byte[]>) connection -> connection.get(key.getBytes()));
        if (result == null) {
            return null;
        }
        return ProtoStuffSerializerUtil.deserialize(result, targetClass);
    }

    public <T> List<T> getListCache(final String key, Class<T> targetClass) {
        byte[] result = redisTemplate.execute(
                (RedisCallback<byte[]>) connection -> connection.get(key.getBytes()));
        if (result == null) {
            return null;
        }
        return ProtoStuffSerializerUtil.deserializeList(result, targetClass);
    }

    /**
     * 精确删除key
     */
    public void deleteCache(String key) {
        redisTemplate.delete(key);
    }

    /**
     * 模糊删除key
     */
    public void deleteCacheWithPattern(String pattern) {
        Set<String> keys = redisTemplate.keys(pattern);
        redisTemplate.delete(keys);
    }

    /**
     * 清空所有缓存
     */
    public void clearCache() {
        deleteCacheWithPattern(RedisCacheUtil.CAHCENAME + "*");
    }

    /**
     * 订单获取自增主键
     */
    public Long getOrderId(String key) {
        RedisAtomicLong entityIdCounter = new RedisAtomicLong(CAHCENAME + "|" + key,
                redisTemplate.getConnectionFactory());
        entityIdCounter.expire(1, TimeUnit.DAYS);
        Long increment = entityIdCounter.incrementAndGet();
        return increment;
    }

    public String getAppOrderId(String appId) {
        String today = DateUtil.formatYMd(new Date());
        Long count = this.getOrderId(today);
        String format = NumberFormatUtil.format(count, "000000");
        String orderId = appId.concat("-").concat(today).concat(format);
        return orderId;
    }

    /**
     * 获取当天机审订单数
     */
    public Long getSysReviewOrder(String key) {
        RedisAtomicLong entityIdCounter = new RedisAtomicLong(CAHCENAME_SYS_REVIEW + "|" + key,
                redisTemplate.getConnectionFactory());
        entityIdCounter.expire(1, TimeUnit.DAYS);
        Long increment = entityIdCounter.incrementAndGet();
        return increment;
    }
}