package com.github.ryan.component.cache.redis;

import redis.clients.jedis.Jedis;

/**
 * @author ryan.houyl@gmail.com
 * @description
 * @className JedisClient
 * @date October 29,2018
 */
public class JedisClient {

    private JedisClient() {}

    private static final int PORT = 6379;
    private static final String HOST = "localhost";

    private static volatile Jedis jedis;

    public static Jedis getInstance() {

        if (jedis == null) {
            synchronized (JedisClient.class) {
               if (jedis == null) {
                   jedis = new Jedis(HOST, PORT);
               }
            }
        }
        return jedis;
    }
}
