package com.gz.design.panda.biz.utils;

import com.caiyi.financial.nirvana.cash.loan.common.entity.Lock;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisCommands;

/**
 * Created by fangzhipeng on 2017/4/1.
 */
@Component
public class RedisLockUtil {

    //单个业务持有锁的时间30s，防止死锁
    private final static long LOCK_EXPIRE = 30 * 1000L;

    @Autowired
    private StringRedisTemplate template;

    /**
     * 尝试获取全局锁
     *
     * @param lock 锁的名称
     * @return true 获取成功，false获取失败
     */
    public boolean tryLock(Lock lock) {
        return getLock(lock, LOCK_EXPIRE);
    }

    /**
     * 尝试获取全局锁
     *
     * @param lock 锁的名称
     * @param lockExpireTime 锁的过期
     * @return true 获取成功，false获取失败
     */
    public boolean tryLock(Lock lock, long lockExpireTime) {
        return getLock(lock, lockExpireTime);
    }


    /**
     * 操作redis获取全局锁
     *
     * @param lock 锁的名称
     * @param lockExpireTime 获取成功后锁的过期时间
     * @return true 获取成功，false获取失败
     */
    public boolean getLock(Lock lock, long lockExpireTime) {
        if (StringUtils.isEmpty(lock.getName()) || StringUtils.isEmpty(lock.getValue())) {
            return false;
        }
                String status = template.execute(new RedisCallback<String>() {
                    @Override
                    public String doInRedis(RedisConnection connection) throws DataAccessException {
                        JedisCommands jedis = (JedisCommands) connection.getNativeConnection();
                        //nx  setNx  ex expireTime
                        //ex  秒  px 毫秒
                        String status = jedis
                                .set(lock.getName(), lock.getValue(), "NX", "PX", lockExpireTime);
                        return status;
                    }
                });
        //抢到锁
        if ("OK".equals(status)) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * 释放锁
     */
    public void releaseLock(Lock lock) {
        if (!StringUtils.isEmpty(lock.getName())) {
            template.delete(lock.getName());
        }
    }

}