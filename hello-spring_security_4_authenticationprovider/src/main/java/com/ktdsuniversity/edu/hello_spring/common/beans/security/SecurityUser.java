package com.ktdsuniversity.edu.hello_spring.common.beans.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.ktdsuniversity.edu.hello_spring.member.vo.MemberVO;

/**
 * SecurityUser 클래스는 SpringSecurity와 데이터베이스의 사용자 정보를 연결하는 역할을 한다. 
 * 즉, 데이터베이스에서 조회된 사용자 정보를 SpringSecurity가 이해할 수 있는 형태로 변환하는 것이다.
 * 
 * MemberVO는 사용자 정보를 담고 있는 VO(Value Object)이며, SecurityUser는 이 VO를 사용해 UserDetails를 구현한다. 
 * 이를 통해 SpringSecurity는 사용자 정보를 효과적으로 인증하고 관리할 수 있다.
 * 
 * UserDetailsService 인터페이스 구현체는 SpringSecurity가 사용자 정보를 조회할 때 사용하는 서비스 클래스이며, 
 * 이 클래스에서 SecurityUser 객체를 반환하여 인증 및 권한 부여에 활용한다.
 */

/**
 * SpringSecurity로 인증할 사용자의 정보를 담고 있는 객체.
 * SpringSecurity로 인증한 사용자의 정보를 담고 있는 객체.
 * SpringSecurity가 사용할 클래스.  --> 인증을 수행함.
 * 
 * -- 호출 과정 --
 * AuthoriztionFilter -> AuthorizationManager -> AuthorizationProvider -> UserDetailService -> 호출
 */
public class SecurityUser implements UserDetails {
	// UserDetails는 SpringSecurity에서 사용자 정보를 표현하는 표준 인터페이스 -> 구현체인 SecurityUser는 사용자 인증과 권한 관리를 위한 클래스가 되는 것.
	/**
	 * Serializable -> 객체를 직렬화 하기 위해 필요. UID와 함께 직렬화 시 클래스 변경으로 인한 오류를 방지할 수 있음.
	 * 직렬화 (Serialization)은 프로그램 내부에서 사용하는 객체나 데이터를 다른 프로그램에 전달하여 사용할 수 있도록 데이터의 형태를 바이트 (Byte) 형태로 변환하는 것을 의미한다.
	 * 객체를 다른 프로세스로 전송 가능한 형태로 변환한다는 것
	 */
	private static final long serialVersionUID = 653523424966037221L;
	
	/**
	 * UserDetailsService를 통해서 아이디(이메일)로 데이터베이스에서 조회된 결과를 가지고 있는 멤버변수. (데이터베이스에서 조회된 정보를 저장한다)
	 */
	private MemberVO memberVO;
	
	/**
	 * SecurityUser의 생성자는 MemberVO 객체를 매개변수로 받아서 초기화 -> 인증을 요청한 사용자의 정보를 담고 있는 MemberVO 객체를 이 클래스의 멤버 변수로 저장.
	 * -> 생성자 주입
	 */
	public SecurityUser(MemberVO memberVO) {
		this.memberVO = memberVO;
	}
	
	/**
	 * PW암호화시 사용되는 Salt값을 반환함. 인증 과정에서 비밀번호 비교시 사용된다.
	 */
	public String getSalt() {
		return this.memberVO.getSalt();
	}
	/**
	 * 외부에서 사용자 정보에 대해 직접 접근 -> getter 역할을 하는 것.
	 */
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
