package com.ktdsuniversity.edu.hello_spring.member.vo;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class LoginMemberVO {

	@NotBlank(message = "이메일을 작성해주세요.")
	@Email(message = "이메일 형식이 아닙니다.")
	private String email;
	
	@NotBlank(message = "비밀번호를 입력해주세요.")
	private String password;
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	
	


	
	
}
