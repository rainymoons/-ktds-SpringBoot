package com.ktdsuniversity.edu.hello_spring.member.dao;

import com.ktdsuniversity.edu.hello_spring.member.vo.LoginMemberVO;
import com.ktdsuniversity.edu.hello_spring.member.vo.MemberVO;
import com.ktdsuniversity.edu.hello_spring.member.vo.RegistMemberVO;

public interface MemberDao {

	public String NAMESPACE = "com.ktdsuniversity.edu.hello_spring.member.dao.MemberDao";
	
	public int insertNewMember(RegistMemberVO registMemberVO);
	
	public int selectEmailCount(String Email);
	
	/**
	 * 로그인 시 비밀번호 암호화를 위해 기존에 발급했던 salt 조회
	 * @param email 조회할 email
	 * @return 회원가입 시 발급받은 salt 값
	 */
	public String selectSalt(String email);
	
	/**
	 * 이메일과 비밀번호로 회원정보 조회
	 * @param LoginMemberVO 이메일과 비밀번호
	 * @return 이메일과 비밀번호가 일치하는 회원의 정보
	 */
	// public MemberVO selectOneMember(LoginMemberVO loginMemberVO);
	
	public int updateLoginFailState(LoginMemberVO loginMemberVO);
	
	public int selectLoginImpossibleCount(String email);
	
	public int updateLoginSuccessState(LoginMemberVO loginMemberVO);
	
	public int deleteOneMember(String email);

	public MemberVO selectMemberByEmail(String username);

	/**
	 * 멤버 테이블에 이미 가입된 회원이 있을 경우 회원의 정보를 수정하고, 가입되어있지 않은 회원일 경우 회원 정보를 insert한다.
	 * @param oAuthMember
	 * @return
	 */
	public int mergeMember(MemberVO oAuthMember);
	
}
