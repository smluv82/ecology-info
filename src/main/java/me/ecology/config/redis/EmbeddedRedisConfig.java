package me.ecology.config.redis;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import redis.embedded.RedisServer;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class EmbeddedRedisConfig {
	private final Environment env;
	private RedisServer redisServer;

	@PostConstruct
	public void redisServer() throws IOException {
		log.info("redisServer start");
//		this.redisServer = new RedisServer(env.getProperty("spring.redis.port", int.class, 6379));

		redisServer = RedisServer.builder()
				.port(env.getProperty("spring.redis.port", int.class, 6379))
				.setting("maxmemory 128M")
				.build();

		redisServer.start();
	}

	@PreDestroy
	public void stopRedis() {
		if (redisServer != null)
			redisServer.stop();
	}
}
