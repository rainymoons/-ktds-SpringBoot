package com.ktdsuniversity.edu.hello_spring.member.service;

import com.ktdsuniversity.edu.hello_spring.member.vo.InsertNewMemberVO;

public interface MemberService {
	
	/**
	 * 회원 가입을 처리한다.
	 * @param memberVO 사용자가 작성한 사용자 정보
	 * @return 회원가입이 정상적으로 처리되었는지 여부
	 */
	
	
	// create가 맞음. registMemberVO로
	public boolean InsertNewMember(InsertNewMemberVO insertNewMemberVO);

	public boolean checkAvailableEmail(String email);
	
}
