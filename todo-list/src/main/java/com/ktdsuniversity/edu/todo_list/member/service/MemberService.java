package com.ktdsuniversity.edu.todo_list.member.service;

import com.ktdsuniversity.edu.todo_list.member.vo.LoginMemberVO;
import com.ktdsuniversity.edu.todo_list.member.vo.MemberVO;
import com.ktdsuniversity.edu.todo_list.member.vo.RegistMemberVO;

public interface MemberService {

	public boolean createNewMember(RegistMemberVO registMemberVO);
	
	public boolean checkAvailableEmail(String email);
	
	public MemberVO readMember(LoginMemberVO loginMemberVO);

	public boolean deleteMember(String email);
	
}
