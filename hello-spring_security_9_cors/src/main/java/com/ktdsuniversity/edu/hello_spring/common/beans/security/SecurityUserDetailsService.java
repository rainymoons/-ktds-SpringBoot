package com.ktdsuniversity.edu.hello_spring.common.beans.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.ktdsuniversity.edu.hello_spring.common.beans.Sha;
import com.ktdsuniversity.edu.hello_spring.member.dao.MemberDao;
import com.ktdsuniversity.edu.hello_spring.member.vo.MemberVO;

/**
 * SpringSecurity에서 사용자의 인증을 처리하기 위해 사용자 정보를 데이터베이스에서 조회하는 역할
 * 인증을 위해서 DB에 사용자가 요청한 정보가 있는지 검사를 하는 클래스.
 * UserDetailsService 인터페이스를 구현하고 있으며, MemberDao를 사용해 데이터베이스와의 연결을 통해 사용자의 정보를 가져온다.
 * 
 * 아이디(이메일: userDetails.getUserName())로만 데이터베이스에서 사용자의 정보를 조회한다.
 * 비밀번호 확인은 다른 클래스의 역할.
 * AuthoriztionFilter -> AuthorizationManager -> AuthorizationProvider -> 호출
 */
public class SecurityUserDetailsService implements UserDetailsService {
// UserDetailsService는 사용자 인증 요청이 있을 때 데이터베이스에서 사용자 정보를 조회하하고 SecurityUser 객체를 반환 
//	-> SpringSecurity는 사용자 정보를 가져와서 인증 절차를 수행
	/**
	 * 사용자 정보를 조회할 Dao;
	 */
	private MemberDao memberDao;
	
	/**
	 *  생성자 주입 방식을 사용하여 MemberDao를 주입
	 *  SecurityUserDetailsService는 데이터베이스와의 연동을 위해 필요한 DAO 객체를 사용
	 */
	public SecurityUserDetailsService(MemberDao memberDao) {
		this.memberDao = memberDao;
	}
	
	/**
	 * 역할 : Spring Security가 사용자 인증 요청 시 호출하는 메서드. 
	 * 		 사용자가 입력한 아이디(이메일)를 사용하여 데이터베이스에서 사용자의 정보를 조회하는 역할
	 * 
	 * @param username : 인증을 요청한 사용자의 아이디(이메일)
	 * @return userDetails 인터페이스를 구현한 사용자 정보 객체
	 * @throws UserNameNotFoundException username으로 조회한 결과가 null일 때, SpringSecurity에게 던질 예외
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// 사용자가 입력한 이메일을 기준으로 데이터베이스에서 사용자 정보를 조회 -> MemberVO 객체로 반환됨. 데이터베이스에서 가져온 사용자의 모든 정보가 담김.
		MemberVO memberVO = this.memberDao.selectMemberByEmail(username);
		
		if (memberVO == null) {
			// 조회했는데 회원이 없을 경우 UserDetailsService에서 예외가 던져지면,
			// AuthenticationProvider에서 예외를 처리한다.
			throw new UsernameNotFoundException("아이디 또는 비밀번호가 없습니다.");
		}
		// UserDetails 인터페이스를 구현한 SecurityUser 객체를 반환
		// 이를 사용해 SpringSecurity는 사용자 인증을 위한 다양한 정보(비밀번호, 권한 등)를 이 객체를 통해 가져올 수 있다.
		// SecurityUser is a UserDetails
		return new SecurityUser(memberVO);
	}
}
