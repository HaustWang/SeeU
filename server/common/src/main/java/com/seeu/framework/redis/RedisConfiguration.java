package com.seeu.framework.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Configuration
public class RedisConfiguration {
    @Autowired
    private RedisProperties redisProperties;

    @Bean
    public JedisCluster getJedisCluster() {

        if (redisProperties.getNodes() == null)
            return null;

        String[] serverArray = redisProperties.getNodes().split(",");
        Set<HostAndPort> nodes = new HashSet<>();

        for (String ipPort : serverArray) {
            String[] ipPortPair = ipPort.split(":");
            nodes.add(new HostAndPort(ipPortPair[0].trim(), Integer.parseInt(ipPortPair[1].trim())));
        }

        log.info("redis ip: {}", nodes);

        //其它的配置要生效，需要换一种初始化方式
        return new JedisCluster(nodes, redisProperties.getTimeout(), redisProperties.getMaxAttempts());
    }

}
