package me.ecology.common.handler;

import java.util.Objects;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.google.common.base.Strings;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import me.ecology.common.component.JwtComponent;

@Slf4j
public class ApiInterceptor extends HandlerInterceptorAdapter {
	private final JwtComponent jwtComponent;
	private final RedisTemplate<String, Object> redisTemplate;

	public ApiInterceptor(JwtComponent jwtComponent, RedisTemplate<String, Object> redisTemplate) {
		this.jwtComponent = jwtComponent;
		this.redisTemplate = redisTemplate;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		final String jwtToken = getAccessToken(request);
		log.info("jwtToken : {}", jwtToken);

		//TODO 에러 코드 재정의
		final Claims claims = Optional.ofNullable(jwtComponent.parseJwt(jwtToken))
				.orElseThrow(() -> new Exception("jwt token parsing error."));

		//redis에 해당 데이터 id가 있는지 체크
		final String srcToken = String.valueOf(redisTemplate.opsForValue().get(claims.getSubject()));

		log.info("reqToken[{}] <> srcToken[{}]", jwtToken, srcToken);

		if(Strings.isNullOrEmpty(srcToken) ||
				!Objects.equals(jwtToken, srcToken)) {
			//redis에 토큰이 없거나 redis의 토큰과 불일치
			throw new Exception("server token is not equal");
		}

		//토큰 유효시간 체크는 jwt parse 쪽에서 체크해서 생략
//		Date expDate = claims.getExpiration();
//		if(expDate.before(new Date())) {
//			//현재 시간보다 이전임.
//			throw new Exception("token is expired.");
//		}

		//refresh인 경우 userId를 attribute로 전달
		if(Objects.equals("/user/refresh", request.getRequestURI()))
			request.setAttribute("userId", claims.getSubject());

		return true;
	}

	private String getAccessToken(final HttpServletRequest request) throws Exception {
		//TODO exception 변경 필요
		return Optional.<String>ofNullable(request.getHeader("Authorization"))
				.orElseThrow(() -> new Exception("NO ACCESS TOKEN"))
				.replace("Bearer", StringUtils.EMPTY).trim();
	}
}
