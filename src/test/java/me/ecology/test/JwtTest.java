package me.ecology.test;

import static org.junit.Assert.assertEquals;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import io.jsonwebtoken.Claims;
import me.ecology.common.component.JwtComponent;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JwtTest {
	@Resource
	private JwtComponent jwtComponent;

	@Test
	public void jwt_생성_및_복호화() throws Exception {
		String userId = "smluv82";

		//jwt 생성
		String token = jwtComponent.makeJwt(userId);
		System.out.println("make token : " + token);

		//jwt parse
		Claims claims = jwtComponent.parseJwt(token);

		String jwtSubject = claims.getSubject();
		System.out.println("jwtSubject : " + jwtSubject);

		assertEquals(userId, jwtSubject);
	}
}
