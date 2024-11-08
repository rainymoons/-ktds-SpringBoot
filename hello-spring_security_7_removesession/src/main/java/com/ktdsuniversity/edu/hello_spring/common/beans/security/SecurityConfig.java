package com.ktdsuniversity.edu.hello_spring.common.beans.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.ktdsuniversity.edu.hello_spring.member.dao.MemberDao;

/**
 * 
 */
@Configuration // Bean 설정을 위한 애노테이션
@EnableWebSecurity // SpringSecurity 활성화 (인증 절차를 위한 활성화, provider, passwordEncoder, service를 사용하기 위한)
public class SecurityConfig {

	@Autowired // configuration이 있으므로 가능
	private MemberDao memberDao;
	
	// 1. SecurityUserDetailService를 Bean으로 등록
	@Bean
	UserDetailsService securityUserDetailsService() {
		return new SecurityUserDetailsService(this.memberDao);
	}
	
	// 2. SecurityPasswordEncoder를 Bean으로 등록
	@Bean
	@Scope("prototype") // 필요할때마다 새로운 인스턴스를 생성시키는 애노테이션
	PasswordEncoder securityPasswordEncoder() {
		return new SecurityPasswordEncoder();
	}
	
	
	// 3. SecurityAuthenticationProvider를 Bean으로 등록 -> 인증 절차를 변경한 것.
	@Bean
	AuthenticationProvider securityAuthenticationProvider() {
		// 파라미터로 빈들을 그대로 가져와서 쓰지 못함. userDetailsService, passwordEncoder
		return new SecurityAuthenticationProvider(this.securityUserDetailsService(), this.securityPasswordEncoder());
	}
	
	@Bean
	AuthenticationFailureHandler loginFailureHandler() {
		return new LoginFailureHandler(this.memberDao);
	}
	
	@Bean
	AuthenticationSuccessHandler loginSuccessHandler() {
		return new LoginSuccessHandler(this.memberDao);
	}
	
	/**
	 * SpringSecurity는 필터 기반의 프레임워크.
	 * SpringSecurity Filter의 실행 순서를 정의하는 역할을 하는 메서드
	 * @param http : 동작시킬 필터들의 전략을 수립한다.
	 * @return 필터들의 동작 순서를 반환시킨다.
	 * @throws Exception 
	 */
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		// 여기서 예외를 던짐.
		// form을 이용한 로그인 페이지의 인증 정책을 설정한다.
		http.formLogin(formLogin -> 
					// SpringSecurity의 로그인 URL을 변경한다.
					formLogin.loginPage("/member/login")
							 // 로그인에 필요한 아이디 파라미터의 이름을 email로 적용.(내가 전달할 아이디 파라미터는 email이다)
							 .usernameParameter("email") // 변경하면 securityAuthenticationProvider가 영향을 받음. 파라미터 부분.
							 .passwordParameter("password") // 내가 전달할 패스워드 파라미터는 password이다.
							 // 로그인을 성공했을 때 이동할 페이지의 URL
							 //.defaultSuccessUrl("/board/list")
							 // securityAuthenticationProvider를 실행시킬 URL(컨트롤러에는 없는 URL) -> 
							 .loginProcessingUrl("/member/security/login")
							 // 예외를 캐치하기 위함 -> 로그인에 실패했을 때 보여줄 클래스
							 .failureHandler(this.loginFailureHandler())
							 .successHandler(this.loginSuccessHandler())
				);
		
		// csrf 방어를 하지 않음.
		http.csrf(csrf -> csrf.disable());
		
		return http.build(); // SecurityFilterChain 반환.
	}
	
}
