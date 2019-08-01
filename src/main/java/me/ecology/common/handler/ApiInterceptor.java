package me.ecology.common.handler;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import me.ecology.app.user.service.UserService;
import me.ecology.common.component.JwtComponent;

@Slf4j
//@Component
public class ApiInterceptor extends HandlerInterceptorAdapter {
	private final JwtComponent jwtComponent;
	private final UserService userService;

	public ApiInterceptor(JwtComponent jwtComponent, UserService userService) {
		this.jwtComponent = jwtComponent;
		this.userService = userService;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		final String jwtToken = getAccessToken(request);
		log.info("jwtToken : {}", jwtToken);

		//TODO 에러 코드 재정의
		final Claims claims = Optional.ofNullable(jwtComponent.parseJwt(jwtToken))
				.orElseThrow(() -> new Exception("jwt token parsing error."));

		//TODO redis에 해당 데이터 id가 있는지 체크
		//TODO 만료가 안되었는지 체크
		System.out.println("subject : " + claims.getSubject());
		System.out.println("expire : " + claims.getExpiration());

		return true;
	}

	private String getAccessToken(final HttpServletRequest request) throws Exception {
		//TODO exception 변경 필요
		return Optional.<String>ofNullable(request.getHeader("Authorization"))
				.orElseThrow(() -> new Exception("NO ACCESS TOKEN"))
				.replace("Bearer", StringUtils.EMPTY).trim();
	}
}
