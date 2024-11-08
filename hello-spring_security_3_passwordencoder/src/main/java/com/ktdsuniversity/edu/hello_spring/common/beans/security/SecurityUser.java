package com.ktdsuniversity.edu.hello_spring.common.beans.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.ktdsuniversity.edu.hello_spring.member.vo.MemberVO;

/**
 * SpringSecurity로 인증할 사용자의 정보를 담고 있는 객체.
 * SpringSecurity로 인증한 사용자의 정보를 담고 있는 객체.
 * SpringSecurity가 사용할 클래스.  --> 인증을 수행함.
 * 
 * -- 호출 과정 --
 * AuthoriztionFilter -> AuthorizationManager -> AuthorizationProvider -> UserDetailService -> 호출
 */
public class SecurityUser implements UserDetails {
	
	// UserDetails가 Serializable을 상속받고 있으므로
	private static final long serialVersionUID = 653523424966037221L;
	
	/**
	 * UserDetailsService를 통해서 아이디(이메일)로 데이터베이스에서 조회된 결과를 가지고 있는 멤버변수. (데이터베이스에서 조회된 정보를 저장한다)
	 */
	private MemberVO memberVO;
	
	public SecurityUser(MemberVO memberVO) {
		this.memberVO = memberVO;
	}

	public String getSalt() {
		return this.memberVO.getSalt();
	}
	
	public MemberVO memberVO() {
		return this.memberVO;
	}
	
	
	/**
	 * 로그인을 요청한 사용자의 권한 정보를 세팅.
	 *  -> 로그인 이후 해당 사용자의 권한 정보를 데이터베이스에서 조회한 후 권한 부여.
	 */
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// 권한 정보가 없으므로 하드코딩
		// 권한 정보를 가지고 있는 인터페이스 -> GrantedAuthority
		// GrantedAuthority를 구현한 권한 클래스 -> SimpleGrantedAuthority
		// SimpleGrantedAuthority List를 반환.
		
		// 부여된 권한에 따라서 실행을 제어할 수 있다.
		// 이 사용자는 CRUD 권한을 가진 사용자이다.
		return List.of(new SimpleGrantedAuthority("CREATE"),
				new SimpleGrantedAuthority("READ"),
				new SimpleGrantedAuthority("UPDATE"),
				new SimpleGrantedAuthority("DELETE"));
	}
	
	/**
	 * 로그인을 요청한 사용자의 비밀번호를 반환한다.
	 */
	@Override
	public String getPassword() {
		return this.memberVO.getPassword ();
	}
	
	/**
	 * 로그인을 요청한 사용자의 아이디(이메일)를 반환한다.
	 */
	@Override
	public String getUsername() {
		return this.memberVO.getEmail();
	}
	
	// 이제 memberVO가 아닌 SecurityUser에서 사용자 정보를 가져온다.
}
