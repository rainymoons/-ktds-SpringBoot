package com.ktdsuniversity.edu.hello_spring.member.dao;

import com.ktdsuniversity.edu.hello_spring.member.vo.InsertNewMemberVO;

public interface MemberDao {

	/**
	 * 파라미터로 전달 된 이메일이 DB에 몇 건 존재하는지 확인한다.
	 * @param email 사용자가 가입 요청한 이메일
	 * @return 동일한 이메일로 등록된 회원의 수
	 */
	public int getEmailCount(String email);

	/**
	 * 회원 가입 쿼리를 실행한다.
	 * @param createMemberVO 사용자가 입력한 회원 정보
	 * @return DB에 insert한 회원의 개수
	 */
	public int insertNewMember(InsertNewMemberVO insertNewMemberVO);
}
