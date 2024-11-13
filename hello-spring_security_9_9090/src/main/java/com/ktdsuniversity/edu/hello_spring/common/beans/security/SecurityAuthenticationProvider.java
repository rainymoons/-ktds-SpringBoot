package com.ktdsuniversity.edu.hello_spring.common.beans.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * SecurityAuthenticationProvider 클래스는 SpringSecurity에서 사용자 인증을 처리하는 핵심 컴포넌트로, ID와 비밀번호를 통해 사용자의 인증을 수행
 * AuthenticationProvider 인터페이스를 구현하여 인증 요청이 있을 때 사용자 정보를 조회하고 비밀번호를 검증
 * 
 * 1.UserDetailsService를 사용하여 사용자 정보를 조회하면, 
 *   사용자가 인증을 요청하면 SecurityAuthenticationProvider는 UserDetailsService를 사용하여 사용자 정보를 조회
 *   이때 사용자의 이메일(ID)을 통해 데이터베이스에서 해당 사용자의 정보를 얻고, 그 결과를 UserDetails 인터페이스 구현체인 SecurityUser로 반환받는다.
 *   
 * 2. PasswordEncoder를 사용한 비밀번호 검증
 *   사용자가 입력한 비밀번호가 데이터베이스의 비밀번호와 일치하는지 검증하기 위해 passwordEncoder를 사용
 *   PasswordEncoder는 입력된 비밀번호를 암호화하고, 데이터베이스에 저장된 암호화된 비밀번호와 비교
 * 3. Authentication 객체 반환
 *   인증이 성공하면 새로운 Authentication 객체 (UsernamePasswordAuthenticationToken)을 생성하여 반환
 *   객체는 SecurityContext에 저장되어 인증이 성공적으로 이루어졌음을 유지합니다. 이렇게 인증된 사용자는 이후에 인증된 사용자로서 시스템의 보호된 자원에 접근
 * 4. supports() 메서드로 인증 방식 정의
 *   이 AuthenticationProvider가 어떤 인증 방식을 지원하는지 정의함. 여기서는 이메일과 비밀번호 방식의 인증을 처리하는 UsernamePasswordAuthenticationToken을 지원한다.
 */

/**
 * SecurityAuthenticationProvider 클래스는 Spring Security에서 사용자의 인증을 처리하는 핵심 컴포넌트
 * UserDetailsService를 사용하여 사용자 정보 조회를 수행하고, PasswordEncoder를 통해 비밀번호 검증을 수행
 * 인증이 성공하면 UsernamePasswordAuthenticationToken을 반환하여 인증 과정을 완료
 * supports() 메서드는 이 인증 공급자가 어떤 인증 방식을 처리할 수 있는지 정의
 * 
 * 인증을 수행한다는 것은, ID로 회원을 찾고 PW로 검증을 수행한다는 것.
 *  - ID로 회원을 찾는다 : UserDetailsService -> SecurityUserDetailsService;
 *  - PW로 검증 수행 : PasswordEncoder -> SecurityPasswordEncoder;
 *  
 *  수행 결과가 정상이라면 SecurityContext에 인증 정보를 저장한다.
 *  
 *  -- 호출 구조
 *     AuthoriztionFilter -> AuthorizationManager -> 호출
 */
public class SecurityAuthenticationProvider implements AuthenticationProvider{

	private static final Logger log = LoggerFactory.getLogger(SecurityAuthenticationProvider.class);
	
	/**
	 * 인증 회원 정보 조회
	 */
	private UserDetailsService userDetailsService;
	
	/**
	 * 인증 비밀번호 검증 및 암호화
	 */
	private PasswordEncoder passwordEncoder;
	
	/**
	 * 생성자 주입 방식으로 UserDetailsService와 PasswordEncoder를 받는다.
	 */
	public SecurityAuthenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
		this.userDetailsService = userDetailsService;
	}
	
	/**
	 * support에 정의된 인증 방식으로 인증을 수행한다.(사용자가 제출한 인증 정보를 기반으로 인증을 수행)
	 * SpringSecurity의 인증 필터(AuthoriaztionFilter 에서 호출)
	 * 인증에 성공하면 Authentication 객체를 반환하고, 실패하면 예외
	 * Authentication 객체는 인증에 대한 결과를 담고 있으며, 여기에는 사용자 정보(이메일, 비밀번호 등)가 포함된다.
	 * 
	 * SecurityContext : HttpSession을 대체할 인증 정보들이 모여있는 곳.
	 * 
	 * @param authentication : 사용자가 인증을 요청한 정보가 들어있다. (이메일, 비밀번호)
	 * @return Authentication : support에 정의된 인증 토큰을 전달 -> SecurityContext에 저장 -> 인증 완료 
	 */
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		/** AuthenticationToken의 역할.
		 * - 사용자가 전달한 인증 이메일과 비밀번호를 추출 -> Parameter : authentication 
		 * - 인증 이메일로 회원 정보를 조회 -> UserDetailsService.loadUserByUsername();으로 검증
		 * - 인증 비밀번호와 회원의 비밀번호를 검증 -> PasswordEncoder.matches();로 검증
		 * - SecurityContext에 인증 토큰(UsernamePasswordAuthenticationToken)을 저장.
		 */
		
		// 아이디와 패스워드를 authentication에서 추출
		String requestAuthenticationEmail = authentication.getName(); // username -> email로 변경됨(config 설정에 의해)
		String requestAuthenticationPassword = authentication.getCredentials().toString();
		
		// 사용자의 권한은 loadUserByUsername으로 DB에 있는 권한을 넣어준다.
		UserDetails userDetails = this.userDetailsService.loadUserByUsername(requestAuthenticationEmail);
		
		// 데이터베이스에 저장된 암호화된 비밀번호
		String storedUserPassword = userDetails.getPassword();
		
		// 암호화된 값을 비교하기 위해 salt가 필요함. -> userDetailsService.getSalt(); 하면 안나옴. SecurityUser가 가지고 있음. -> 형변환 필요
		// (1번 방법) SecurityUser storedUserDetails = (SecurityUser) userDetails;
		String storedUserSalt = ((SecurityUser) userDetails).getSalt();
		// 가져온 salt값을 넣어주기 위해서는 마찬가지로 형변환이 필요함.
		((SecurityPasswordEncoder)this.passwordEncoder).setSalt(storedUserSalt);
		
		// 비밀번호 비교(사용자 입력 PW, DB에 있는 PW)
		boolean isMatchPassword = this.passwordEncoder.matches(requestAuthenticationPassword, storedUserPassword);
		
		if (isMatchPassword) {
			// 인증 컨텍스트(SecurityContext)에 저장을 한다.
			// 인증된 사용자의 정보(MemberVO - getMemberVO(형변환)), 권한 정보가 토큰에 들어 있음.
			return new UsernamePasswordAuthenticationToken(
					((SecurityUser) userDetails).getMemberVO(),
					storedUserPassword,
					userDetails.getAuthorities()); 
		} else {
			// 일단 인증횟수 초과, 인증횟수 몇번 남았다 등등은 여기다가 쓰는데 다른곳에 쓰는게 더 좋음.
			throw new BadCredentialsException("아이디 또는 비밀번호가 일치하지 않습니다.");
		}
	}

	
	/**
	 * 인증 수단을 정의하는 메서드
	 * UsernamePasswordAuthenticationToken 방식을 사용함.
	 *  -> 이메일과 비밀번호로 인증을 수행하는 방식
	 */
	@Override
	public boolean supports(Class<?> authentication) {
		log.debug("Param token tpye: {}", authentication);
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

}
