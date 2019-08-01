package me.ecology.common.component;

import java.nio.charset.StandardCharsets;

import org.springframework.stereotype.Component;

import com.kosprov.jargon2.api.Jargon2;
import com.kosprov.jargon2.api.Jargon2.Type;

@Component
public class UserPwdHashComponent {

	/**
	 * user pwd 에서 사용하는 Argon2Hash encode
	 *
	 * @param userPwd
	 * @return
	 * @throws Exception
	 */
	public String encodeArgon2Hash(final String userPwd) throws Exception {
		return Jargon2.jargon2Hasher()
				.type(Type.ARGON2i)
				.memoryCost(512)
				.timeCost(2)
				.parallelism(2)
				.hashLength(16)
				.password(userPwd.getBytes(StandardCharsets.UTF_8))
				.encodedHash();
	}

	public boolean verifyArgon2Hash(final String srcUserPwd, final String userPwd) throws Exception {
		return Jargon2.jargon2Verifier()
				.hash(srcUserPwd)
				.password(userPwd.getBytes(StandardCharsets.UTF_8))
				.verifyEncoded();
	}
}
