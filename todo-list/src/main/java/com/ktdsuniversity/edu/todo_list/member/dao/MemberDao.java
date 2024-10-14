package com.ktdsuniversity.edu.todo_list.member.dao;

import com.ktdsuniversity.edu.todo_list.member.vo.LoginMemberVO;
import com.ktdsuniversity.edu.todo_list.member.vo.MemberVO;
import com.ktdsuniversity.edu.todo_list.member.vo.RegistMemberVO;

public interface MemberDao {

	public String NAMESPACE = "com.ktdsuniversity.edu.todo_list.member.dao.MemberDao";
	
	public int insertNewMember(RegistMemberVO registMemberVO);
	public int selectEmailCount(String email);
	
	public String selectSalt(String email);
	
	public MemberVO selectOneMember(LoginMemberVO loginMemberVO);
	
	public int updateLoginFailState(LoginMemberVO loginMemberVO);
	
	public int selectLoginImpossibleCount(String email);
	
	public int updateLoginSuccessState(LoginMemberVO loginMemberVO);
	
	public int deleteOneMember(String email);
	
}
