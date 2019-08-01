package me.ecology.config.redis;

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class RedisConfig {
	private final Environment env;
	private String redisHost;
	private int redisPort;
	private int redisDatabase;

	@PostConstruct
	public void setConnectionInfo() throws IOException {
		this.redisHost = env.getProperty("spring.redis.host", String.class, "localhost");
		this.redisPort = env.getProperty("spring.redis.port", int.class, 6379);
		this.redisDatabase = env.getProperty("spring.redis.database", int.class, 0);
	}

	@Bean(name="redisConnectionFactory")
	public LettuceConnectionFactory redisConnectionFactory() {
		final RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration(redisHost, redisPort);
		redisConfig.setDatabase(redisDatabase);
		return new LettuceConnectionFactory(redisConfig);
	}

	@Bean(name="redisTemplate")
	public <K, T> RedisTemplate<K, T> redisTemplate() {
		return getRedisTemplate(redisConnectionFactory());
	}

	private <K, T> RedisTemplate<K, T> getRedisTemplate(final LettuceConnectionFactory connectionFactory) {
		final RedisTemplate<K, T> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(connectionFactory);
		redisTemplate.setEnableTransactionSupport(true);
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
		return redisTemplate;
	}
}
