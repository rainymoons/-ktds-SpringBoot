package com.ktdsuniversity.edu.hello_spring.common.exceptions;

import com.ktdsuniversity.edu.hello_spring.member.vo.RegistMemberVO;

public class AlreadyUseException extends RuntimeException {
	
	private static final long serialVersionUID = -3226829462007028580L;
	
	private RegistMemberVO registMemberVO;

	public AlreadyUseException(RegistMemberVO registMemberVO, String message) {
		super();
		this.registMemberVO = registMemberVO;
	}

	public RegistMemberVO getRegistMemberVO() {
		return registMemberVO;
	}
}
