package me.ecology.test;

import static org.junit.Assert.assertNotNull;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import me.ecology.app.user.service.UserService;
import me.ecology.vo.user.User;
import me.ecology.vo.user.UserParam;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserTest {
	@Resource
	private UserService userService;

	@Test
	public void 계정_생성() throws Exception {
		UserParam userParam = new UserParam();
		userParam.setUserId("test2");
		userParam.setUserPwd("12Qwaszx!@");
		System.out.println("userParam : " + userParam);

		User resUser = userService.signup(userParam);
		System.out.println("resUser : " + resUser);

		assertNotNull(resUser);
	}

	@Test
	public void 계정_인증() throws Exception {
		UserParam userParam = new UserParam();
		userParam.setUserId("test1");
		userParam.setUserPwd("12Qwaszx!@");
		System.out.println("userParam : " + userParam);

		User resUser = userService.signin(userParam);
		System.out.println("resUser : " + resUser);

		assertNotNull(resUser);
		assertNotNull(resUser.getToken());
	}

	@Test
	public void 토큰_refresh() throws Exception {
		String userId = "test1";
		System.out.println("userId : " + userId);

		User resUser = userService.refresh(userId);
		System.out.println("resUser : " + resUser);

		assertNotNull(resUser);
		assertNotNull(resUser.getToken());
	}
}
