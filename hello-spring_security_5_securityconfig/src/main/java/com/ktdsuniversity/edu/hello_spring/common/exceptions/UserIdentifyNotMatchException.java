package com.ktdsuniversity.edu.hello_spring.common.exceptions;

import com.ktdsuniversity.edu.hello_spring.member.vo.LoginMemberVO;

public class UserIdentifyNotMatchException extends RuntimeException {

	private static final long serialVersionUID = 2088434367893397297L;

	private LoginMemberVO loginMemberVO;

	public UserIdentifyNotMatchException(LoginMemberVO loginMemberVO, String message) {
		super();
		this.loginMemberVO = loginMemberVO;
	}
	
	public LoginMemberVO getMemberVO() {
		return loginMemberVO;
	}
}
