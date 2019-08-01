package me.ecology.test.jasypt;

import javax.annotation.Resource;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JasyptTest {
	@Resource
	private StandardPBEStringEncryptor jasyptStringEncryptor;

	@Test
	public void 암복호화_test() throws Exception {
		System.out.println("testtest");

		String encText = jasyptStringEncryptor.encrypt("test");
		String plainText = jasyptStringEncryptor.decrypt(encText);

		System.out.println("encText : " + encText);
		System.out.println("plainText : " + plainText);

		System.out.println(jasyptStringEncryptor.encrypt("smluv82"));
		System.out.println(jasyptStringEncryptor.encrypt("1234qwer!@"));
	}
}
