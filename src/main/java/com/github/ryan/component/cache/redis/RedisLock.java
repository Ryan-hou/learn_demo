package com.github.ryan.component.cache.redis;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;

import java.util.Collections;
import java.util.UUID;

/**
 * @author ryan.houyl@gmail.com
 * @description Redis单实例下实现分布式锁
 * 多实例下实现方式参见 RedLock：
 * https://redis.io/topics/distlock
 * @className RedisLock
 * @date October 29,2018
 */
@Slf4j
public class RedisLock {

    private static final String LOCK_SUCCESS = "OK";
    private static final String SET_IF_NOT_EXIST = "NX";
    private static final String SET_WITH_EXPIRE_TIME = "PX";
    private static final Long RELEASE_SUCCESS = 1L;


    private Jedis jedis;

    private RedisLock(Jedis jedis) {
        this.jedis = jedis;
    }

    private static RedisLock lock = new RedisLock(JedisClient.getInstance());

    public static RedisLock getLock() {
        return lock;
    }

    /**
     * 尝试获取分布式锁，true为获取成功
     * 由于我们只考虑Redis单机部署的场景，所以高可用暂不考虑
     *
     * @param lockKey    锁标识
     * @param requestId  请求标识(解锁时用来判断是不是原来的请求加的锁)
     * @param expireTime 锁过期时间(millisecond)
     * @return 是否获取锁成功
     */
    public boolean tryLock(String lockKey, String requestId, int expireTime) {

        /**
         * jedis.setnx(LOCK_KEY, LOCK_VALUE)
         * jedis.expire(LOCK_KEY, LOCK_TIME)
         * 上面分开写会出现客户端加锁成功后，没来得及设置过期时间就宕机，该锁会一直存在
         */
        String res = jedis.set(lockKey, requestId, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, expireTime);
        return LOCK_SUCCESS.equals(res) ? true : false;
    }


    /**
     * 释放分布式锁
     * @param lockKey 锁标识
     * @param requestId 请求标识
     * @return 是否释放成功
     */
    public boolean releaseLock(String lockKey, String requestId) {

        /**
         *  jedis.del(lockKey);
         *  不先判断锁的拥有者而直接解锁的方式，会导致任何客户端都可以随时进行解锁，即使这把锁不是它的。
         *
         *  // 判断加锁与解锁是不是同一个客户端
         *  if (requestId.equals(jedis.get(lockKey))) {
         *      // 若在此时，这把锁突然不是这个客户端的，则会误解锁
         *      // 在此期间锁过期，然后被别的客户端加锁成功
         *      jedis.del(lockKey);
         *  }
         */

        // 首先获取锁对应的value值，检查是否与requestId相等，如果相等则删除锁（解锁）。
        // 那么为什么要使用Lua语言来实现呢？因为要确保上述操作是原子性的。
        // 在eval命令执行Lua代码的时候，Lua代码将被当成一个命令去执行，并且直到eval命令执行完成，Redis才会执行其他命令
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] " +
                "then return redis.call('del', KEYS[1])" +
                "else return 0 end";
        Object res = jedis.eval(script, Collections.singletonList(lockKey), Collections.singletonList(requestId));
        return RELEASE_SUCCESS.equals(res) ? true : false;

    }

    public static void main(String[] args) {
        RedisLock lock = RedisLock.getLock();
        final String lockKey = "vms_gene_car";
        final String requestId = UUID.randomUUID().toString();
        log.info("lock = {}, requestId = {}", lockKey, requestId);
        boolean res = lock.tryLock(lockKey, requestId, 10000);
        log.info("Lock res = {}", res);

        // boolean unlock = lock.releaseLock(lockKey, "aaaa");
        boolean unlock = lock.releaseLock(lockKey, requestId);
        log.info("Unlock res = {}", unlock);
    }

}
