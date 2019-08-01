package me.ecology.app.user.service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.ecology.app.user.repository.UserRepository;
import me.ecology.common.component.JwtComponent;
import me.ecology.common.component.UserPwdHashComponent;
import me.ecology.vo.user.User;
import me.ecology.vo.user.UserParam;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {
	private final Environment env;
	private final UserRepository userRepository;
	private final UserPwdHashComponent userPwdHashComponent;
	private final JwtComponent jwtComponent;
	@Qualifier("redisTemplate")
	private final RedisTemplate<String, Object> redisTemplate;

	/**
	 * 계정 생성
	 *
	 * @param userParam
	 * @return
	 * @throws Exception
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public User signup(final UserParam userParam) throws Exception {
		//user_id가 기존에 있는지 체크
		if(userRepository.existsByUserId(userParam.getUserId()))
			throw new Exception("already exist user id");

		final User saveUser = User.builder()
				.userId(userParam.getUserId())
				.userPwd(userPwdHashComponent.encodeArgon2Hash(userParam.getUserPwd()))
				.build();

		if(log.isDebugEnabled())
			log.debug("saveUser[{}]", saveUser);
		//TODO exception 변경 필요
		Optional.<User>ofNullable(userRepository.save(saveUser))
		.orElseThrow(() -> new Exception("save user failed"));

		return saveUser;
	}

	/**
	 * 계정 인증
	 *
	 * @param userParam
	 * @return
	 * @throws Exception
	 */
	public User signin(final UserParam userParam) throws Exception {
		User srcUser = userRepository.findByUserId(userParam.getUserId()).orElseThrow(() -> new Exception("id or pwd is wrong."));

		if(!userPwdHashComponent.verifyArgon2Hash(srcUser.getUserPwd(), userParam.getUserPwd()))
			throw new Exception("id or pwd is wrong.");

		return getTokenData(userParam.getUserId());
	}

	/**
	 * jwt refresh
	 *
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public User refresh(final String userId) throws Exception {
		return getTokenData(userId);
	}

	private User getTokenData(final String userId) throws Exception {
		//jwt token 생성
		final String token = jwtComponent.makeJwt(userId);

		//redis에 데이터 추가
		redisTemplate.opsForValue().set(userId, token, env.getProperty("jwt.expire", long.class), TimeUnit.MINUTES);

//		User resultUser = User.builder().token(token).build();
		User resultUser = new User();
		resultUser.setToken(token);

		log.info("user token info : {}", resultUser.getToken());

		return resultUser;
	}
}
