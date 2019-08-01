package me.ecology.config;

import javax.annotation.Resource;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import me.ecology.app.user.service.UserService;
import me.ecology.common.component.JwtComponent;
import me.ecology.common.handler.ApiInterceptor;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
	@Resource
	private JwtComponent jwtComponent;

	@Resource
	private UserService userService;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new ApiInterceptor(jwtComponent, userService))
		.addPathPatterns("/ecology/**");
	}
}
