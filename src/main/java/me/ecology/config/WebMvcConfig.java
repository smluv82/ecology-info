package me.ecology.config;

import javax.annotation.Resource;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import me.ecology.common.component.JwtComponent;
import me.ecology.common.handler.ApiInterceptor;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
	@Resource
	private JwtComponent jwtComponent;

	@Resource
	private RedisTemplate<String, Object> redisTemplate;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new ApiInterceptor(jwtComponent, redisTemplate))
		.addPathPatterns("/ecology/**", "/user/refresh");
	}
}
