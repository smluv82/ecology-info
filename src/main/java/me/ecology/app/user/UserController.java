package me.ecology.app.user;

import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Strings;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.ecology.app.user.service.UserService;
import me.ecology.vo.user.User;
import me.ecology.vo.user.UserParam;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value="/user", consumes=MediaType.APPLICATION_JSON_UTF8_VALUE)
public class UserController {
	private final UserService userService;

	/**
	 * 계정 생성
	 *
	 * @param userParam
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/signup")
	public User signup(@RequestBody @Valid final UserParam userParam) throws Exception {
		log.info("signup");

		return userService.signup(userParam);
	}

	/**
	 * 계정 인증
	 *
	 * @param userParam
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/signin")
	public User signin(@RequestBody @Valid final UserParam userParam) throws Exception {
		log.info("signin");

		return userService.signin(userParam);
	}

	/**
	 * jwt refresh
	 *
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/refresh")
	public User refresh(@RequestAttribute final String userId) throws Exception {
		log.info("refresh");

		if(Strings.isNullOrEmpty(userId))
			throw new Exception("refresh userId is null");

		return userService.refresh(userId);
	}
}
