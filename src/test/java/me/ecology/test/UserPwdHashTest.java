package me.ecology.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import me.ecology.common.component.UserPwdHashComponent;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserPwdHashTest {
	@Resource
	private UserPwdHashComponent userPwdHashComponent;

	@Test
	public void 계정_패스워드_암호화_테스트() throws Exception {
		String plainPwd = "12Qwaszx!@";

		String encodePwd = userPwdHashComponent.encodeArgon2Hash(plainPwd);
		assertNotNull(encodePwd);
		System.out.println(String.format("plainPwd[%s] <> encodePwd[%s] ", plainPwd, encodePwd));

		boolean verifyResult = userPwdHashComponent.verifyArgon2Hash(encodePwd, plainPwd);
		System.out.println("verifyResult : " + verifyResult);
		assertTrue(verifyResult);
	}
}
