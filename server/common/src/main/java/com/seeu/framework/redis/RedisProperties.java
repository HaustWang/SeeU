package com.seeu.framework.redis;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "seeu.redis.pool")
public class RedisProperties {
	private String nodes;
	private int timeout;
	private int maxAttempts;
	private String single;
}
