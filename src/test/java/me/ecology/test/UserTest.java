package me.ecology.test;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import me.ecology.app.user.service.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserTest {
	@Resource
	private UserService userService;

	@Test
	public void 계정_생성() throws Exception {

	}

	@Test
	public void 계정_인증() throws Exception {

	}

	@Test
	public void 토큰_refresh() throws Exception {

	}
}
