package com.ktdsuniversity.edu.hello_spring.member.service;

import com.ktdsuniversity.edu.hello_spring.member.vo.LoginMemberVO;
import com.ktdsuniversity.edu.hello_spring.member.vo.MemberVO;
import com.ktdsuniversity.edu.hello_spring.member.vo.RegistMemberVO;

public interface MemberService {

	public boolean createNewMember(RegistMemberVO registMemberVO);
	
	public boolean checkAvailableEmail(String email);
	
	/**
	 * 이메일과 비밀번호로 회원 조회
	 * @param memberVO 이메일과 비밀번호
	 * @return 일치하는 회원
	 */
	public MemberVO readMember(LoginMemberVO loginMemberVO);
	
	public boolean deleteMember(String email);
	
}
