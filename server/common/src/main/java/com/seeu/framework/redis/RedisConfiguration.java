package com.seeu.framework.redis;

import java.util.HashSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

@Configuration
public class RedisConfiguration {
	@Autowired
	private RedisProperties redisProperties;

	private Logger logger = LoggerFactory.getLogger(RedisConfiguration.class);

	@Bean
	public JedisCluster getJedisCluster() {

		if (redisProperties.getNodes() == null)
			return null;

		String[] serverArray = redisProperties.getNodes().split(",");
		Set<HostAndPort> nodes = new HashSet<>();

		for (String ipPort : serverArray) {
			String[] ipPortPair = ipPort.split(":");
			nodes.add(new HostAndPort(ipPortPair[0].trim(), Integer.valueOf(ipPortPair[1].trim())));
		}

		logger.info("redis ip: {}", nodes);

		//其它的配置要生效，需要换一种初始化方式
		return new JedisCluster(nodes, redisProperties.getTimeout(), redisProperties.getMaxAttempts());
	}

}
