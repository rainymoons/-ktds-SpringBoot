package com.ktdsuniversity.edu.hello_spring.common.beans.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.ktdsuniversity.edu.hello_spring.common.beans.Sha;
import com.ktdsuniversity.edu.hello_spring.member.dao.MemberDao;
import com.ktdsuniversity.edu.hello_spring.member.vo.MemberVO;

/**
 * SpringSecurity에 인증을 요청한 사용자의 정보를 조회하는 역할.
 * 인증을 위해서 DB에 사용자가 요청한 정보가 있는지 검사를 하려고 하는 클래스.
 * 
 * 아이디(이메일: userDetails.getUserName())로만 데이터베이스에서 사용자의 정보를 조회한다.
 * 비밀번호 확인은 다른 클래스의 역할.
 * AuthoriztionFilter -> AuthorizationManager -> AuthorizationProvider -> 호출
 * 
 * 암호화를 위해 Sha 상속
 */
public class SecurityUserDetailsService implements UserDetailsService {

	/**
	 * 사용자 정보를 조회할 Dao;
	 */
	private MemberDao memberDao;
	
	public SecurityUserDetailsService(MemberDao memberDao) {
		this.memberDao = memberDao;
	}
	
	/**
	 * 역할 : 데이터베이스에서 사용자의 정보를 조회한다.
	 * 
	 * @param username : 인증을 요청한 사용자의 아이디(이메일)
	 * @return userDetails 인터페이스를 구현한 사용자 정보 객체
	 * @throws UserNameNotFoundException username으로 조회한 결과가 null일 때, SpringSecurity에게 던질 예외
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		MemberVO memberVO = this.memberDao.selectMemberByEmail(username); // 이메일 줄테니 memberVO 줘라.
		
		if (memberVO == null) {
			// 조회했는데 회원이 없을 경우 UserDetailsService에서 예외가 던져지면,
			// AuthenticationProvider에서 예외를 처리한다.
			throw new UsernameNotFoundException("아이디 또는 비밀번호가 없습니다.");
		}
		// UserDetails 인터페이스 타입의 클래스로 계정 정보를 전달한다.
		// SecurityUser is a UserDetails
		return new SecurityUser(memberVO);
	}
}
