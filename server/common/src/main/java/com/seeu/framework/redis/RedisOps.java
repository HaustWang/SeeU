package com.seeu.framework.redis;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisDataException;

@Slf4j
@Repository
public class RedisOps {
    private JedisCluster jedisCluster;

    public void setJedisCluster(JedisCluster jedisCluster) {
        this.jedisCluster = jedisCluster;
    }

    // 由于不可控原因，redis集群未实现keys的一些操作，所以最好不用
    public Set<String> keys() {
        return keys("*");
    }

    public Set<String> keys(String key) {
        TreeSet<String> keys = new TreeSet<>();
        Map<String, JedisPool> clusterNodes = jedisCluster.getClusterNodes();
        for (String k : clusterNodes.keySet()) {
            JedisPool jp = clusterNodes.get(k);
            Jedis connection = jp.getResource();
            try {
                keys.addAll(connection.keys(key));
            } catch (Exception e) {
                log.error("catch an exception:", e);
            } finally {
                connection.close();
            }
        }
        return keys;
    }

    public String autoId(String key) {
        String sid = jedisCluster.get(key);
        if (StringUtils.isEmpty(sid)) {
            jedisCluster.set(key, "1");
            sid = "1";
        }
        try {
            jedisCluster.incr(key);
        } catch (JedisDataException e) {
            jedisCluster.set(key, "1");
            sid = "1";
            jedisCluster.incr(key);
        }
        return sid;
    }

    public void delete(String key) {
        jedisCluster.del(key);
    }

    public void setOpsValue(String key, String value) {
        jedisCluster.set(key, value);
    }

    public void setOpsValue(String key, String value, int seconds) {
        jedisCluster.setex(key, seconds, value);
    }

    public String getOpsValue(String key) {
        return jedisCluster.get(key);
    }

    public void putOpsHash(String key, String field, String value) {
        jedisCluster.hset(key, field, value);
    }

    public void putAllOpsHash(String key, Map<String, String> m) {
        jedisCluster.hmset(key, m);
    }

    public Set<String> opsHashKeys(String key) {
        return jedisCluster.hkeys(key);
    }

    public String getOpsHash(String key, String field) {
        return jedisCluster.hget(key, field);
    }

    public Map<String, String> getOpsHashAll(String key) {
        return jedisCluster.hgetAll(key);
    }

    public long opsHashSize(String key) {
        return jedisCluster.hlen(key);
    }

    public boolean hasOpsHashKey(String key, String field) {
        return jedisCluster.hexists(key, field);
    }

    public Long delOpsHash(String key, final String... field) {
        return jedisCluster.hdel(key, field);
    }

    public List<String> opsHashValues(String key) {
        return jedisCluster.hvals(key);
    }

    public Map<String, String> getAllOpsHash(String key) {
        return jedisCluster.hgetAll(key);
    }

    public Long addOpsSet(String key, final String... values) {
        return jedisCluster.sadd(key, values);
    }

    public long opsSetSize(String key) {
        return jedisCluster.scard(key);
    }

    public boolean opsSetIsMember(String key, String field) {
        return jedisCluster.sismember(key, field);
    }

    public Set<String> opsSetMembers(String key) {
        return jedisCluster.smembers(key);
    }

    public String opsSetRandomMember(String key) {
        return jedisCluster.srandmember(key);
    }

    public Long opsSetRemove(String key, final String... value) {
        return jedisCluster.srem(key, value);
    }

    public Long opsSetExpire(String key, int seconds) {
        return jedisCluster.expire(key, seconds);
    }

    public Boolean exists(String key) {
        return jedisCluster.exists(key);
    }

    public String rpopLpush(String key, String value) {
        return jedisCluster.rpoplpush(key, value);
    }

    ;

    public String rpop(String key) {
        return jedisCluster.rpop(key);
    }

    ;

    public long rpush(String key, String value) {
        return jedisCluster.rpush(key, value);
    }
}
