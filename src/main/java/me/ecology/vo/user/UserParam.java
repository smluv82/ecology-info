package me.ecology.vo.user;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserParam implements Serializable {
	private static final long serialVersionUID = -8235814184065532677L;

	@NotEmpty
	private String userId;

	@NotEmpty
	@Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,16}$", message="패스워드는 대문자,소문자,특수문자,숫자를 포함한 8~16 자리여야 합니다.")
	@Length(min=8,max=16)
	private String userPwd;
}
