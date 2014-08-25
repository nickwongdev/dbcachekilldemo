package com.yskts.cachekill.manager;

import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Created by Nick on 8/24/2014.
 */
@Component
public class RedisManager {

    private static final String HOST = "localhost";
    private JedisPool jedisPool;

    private String generateKey(final String entity, final String key) {
        return entity + "|" + key;
    }

    public RedisManager() {
        jedisPool = new JedisPool(HOST);
    }

    public void put(final String entity, final String key, final String payload) {
        Jedis con = null;
        try {
            con = jedisPool.getResource();
            final String cacheKey = generateKey(entity, key);
            con.set(cacheKey, payload);
        } catch (Exception e) {
        } finally {
            if (con != null) {
                con.close();
            }
        }
    }

    public String get(final String entity, final String key) {
        Jedis con = null;
        try {
            con = jedisPool.getResource();
            final String cacheKey = generateKey(entity, key);
            return con.get(cacheKey);
        } catch (Exception e) {
        } finally {
            if (con != null) {
                con.close();
            }
        }
        return null;
    }

    public void del(final String entity, final String key) {
        Jedis con = null;
        try {
            con = jedisPool.getResource();
            final String cacheKey = generateKey(entity, key);
            con.del(cacheKey);
        } catch (Exception e) {
        } finally {
            if (con != null) {
                con.close();
            }
        }
    }
}
