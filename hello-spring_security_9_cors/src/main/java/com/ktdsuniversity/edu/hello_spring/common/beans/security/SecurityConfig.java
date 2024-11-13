package com.ktdsuniversity.edu.hello_spring.common.beans.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

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
	
	@Bean
	WebSecurityCustomizer webSecurityCustomizer() {
		
		/*
		 * 아래 URL들은 SpringSecurity가 절대 개입하지 않는다. 
		 *  -> <sec:*> </sec:*> 사용 불가.(springSecurity JSTL이 사용불가하므로) 
		 */
		// ""경로의 모든 url을 무시하겠다.(security가 경로에 대해 개입하지 않음)
		return (web) -> web.ignoring()
				.requestMatchers("/WEB-INF/views/**")
				//.requestMatchers("/member/login")
				//.requestMatchers("/member/regist/**") // 중복검사 등을 수행하기 위해 ** (시큐리티가 개입하지 못하게) -> csrf때문에 막음. 
				.requestMatchers("/error/**")
				.requestMatchers("/favicon.ico")
				.requestMatchers("/member/**-delete-me") // 회원탈퇴시 리다이렉트되는 페이지에 대해 막아준다.
				.requestMatchers("/js/**")
				.requestMatchers("/css/**");
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
		
		/*
		 * URL 패턴별로 인증을 필요로 하는지? 혹은 인증이 필요 없는지? 혹은 특정 역할(ROLE)만 접근이 가능한지?
		 * 혹은 특정 권한(Authoirty)만 접근이 가능한지를 명시한다.
		 * @PreAuthority() 로도 별도의 설정이 가능하다.
		 */
		http.authorizeHttpRequests(httpRequest -> 
			// /board/list는 인증 여부와 관계없이 접근이 가능하다.
			// permitAll()은 <sec:*></sec:*> securityJSTL이 사용 가능함. 스프링 시큐리티가 항상 개입하기 때문.
			// Controller에서 Authentication 객체에 접근이 가능하다.
			httpRequest.requestMatchers("/member/login").permitAll()
						.requestMatchers("member/regist/**").permitAll()
						.requestMatchers("/board/list").permitAll() // permitAll() 모든 접근 가능(인증없이도 접근 가능)
						// /board/excel/download URL은 ROLE_ADMIN만 접근할 수 있다. (ROLE로 접근)
						// ROLE_ADMIN이 아니라 ADMIN으로 작성. 시큐리티가 알아서 ROLE_을 붙여서 검색. -> 역할로 접근
//					   .requestMatchers("/board/excel/download").hasRole("ADMIN")
					    // /board/excel/download URL은 DOWNLOAD_EXCEL 권한만 접근할 수 있다. -> 권한으로 접근
					   .requestMatchers("/board/excel/download").hasAuthority("DOWNLOAD_EXCEL")
						// /board/list를 제외한 모든 URL은 반드시 인증이 필요하다
					   .requestMatchers("/board/delete/**").hasAuthority("ARTICLE_DELETE")
					   .requestMatchers("/board/modify/**").hasAuthority("ARTICLE_MODIFY")
					   .requestMatchers("/board/write").hasAuthority("ARTICLE_CREATE")
					   .requestMatchers("/board/view").hasAuthority("ARTICLE_READ")
					   // postMapping에 대해서만 권한을 주고 싶다. 댓글을 읽어올수는 있어야 하니까.
					   .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.POST, "/board/reply/**")).hasAuthority("REPLY_CREATE")
					   .anyRequest().authenticated()
		);
		
		
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
//		http.csrf(csrf -> csrf.disable());
		
		return http.build(); // SecurityFilterChain 반환.
	}
	
}
