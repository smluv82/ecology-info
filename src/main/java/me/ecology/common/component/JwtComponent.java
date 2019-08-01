package me.ecology.common.component;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtComponent {
	private final Environment env;

	private enum JwtNameEnum {
		TYP, ALG
	}

	public String makeJwt(final String userId) throws Exception {
		return Jwts.builder()
		.setHeaderParam(JwtNameEnum.TYP.name().toLowerCase(), "JWT")
		.setHeaderParam(JwtNameEnum.ALG.name().toLowerCase(), SignatureAlgorithm.HS512.getValue())
		.setSubject(userId)
//		.claim("claim", "smluv82")
		.setExpiration(Date.from(LocalDateTime.now().plusMinutes(env.getProperty("jwt.expire", int.class, 30)).atZone(ZoneId.systemDefault()).toInstant()))
		.signWith(SignatureAlgorithm.HS512, getApiKey())
		.compact();
	}

	public Claims parseJwt(final String jwtToken) throws Exception {
		return Jwts.parser()
				.setSigningKey(getSecretKeyBytes())
				.parseClaimsJws(jwtToken)
				.getBody();
	}

	private Key getApiKey() throws Exception {
		return new SecretKeySpec(getSecretKeyBytes(), SignatureAlgorithm.HS512.getJcaName());
	}

	private byte[] getSecretKeyBytes() throws Exception {
		return DatatypeConverter.parseBase64Binary(env.getProperty("jwt.secret-key"));
	}
}
