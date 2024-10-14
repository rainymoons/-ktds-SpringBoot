package com.ktdsuniversity.edu.hello_spring.member.service;

import com.ktdsuniversity.edu.hello_spring.member.vo.RegistMemberVO;

public interface MemberService {

	public boolean createNewMember(RegistMemberVO registMemberVO);
	
	public boolean checkAvailableEmail(String email);
	
}
