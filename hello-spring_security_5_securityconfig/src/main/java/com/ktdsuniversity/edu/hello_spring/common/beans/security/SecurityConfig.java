package com.ktdsuniversity.edu.hello_spring.common.beans.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

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
	AuthenticationProvider securityAuthenticationProvider() {
		// 파라미터로 빈들을 그대로 가져와서 쓰지 못함. userDetailsService, passwordEncoder
		return new SecurityAuthenticationProvider(this.securityUserDetailsService(), this.securityPasswordEncoder());
	}
	
}
