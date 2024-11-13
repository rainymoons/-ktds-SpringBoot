package com.ktdsuniversity.edu.hello_spring.common.beans.security;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.ktdsuniversity.edu.hello_spring.member.dao.MemberDao;
import com.ktdsuniversity.edu.hello_spring.member.vo.MemberVO;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 시큐리티 인증에 성공했을 때 처리할 클래스.
 */
public class LoginSuccessHandler implements AuthenticationSuccessHandler {
	
	private MemberDao memberDao;
	
	public LoginSuccessHandler(MemberDao memberDao) {
		this.memberDao = memberDao;
	}
	
	private static final Logger log = LoggerFactory.getLogger(LoginSuccessHandler.class);
	
	/**
	 * @param authentication : 인증이 완료되어 보안컨텍스트에 저장된 인증 객체
	 * 							- UsernamePasswordAuthenticationToken
	 */
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request,
										HttpServletResponse response,
										Authentication authentication) throws IOException, ServletException {
		
		String authenticatedEmail = authentication.getName();
		
		log.debug("회원이 인증 성공함! {} : ", authenticatedEmail);
		
		// TODO 로그인 날짜를 현재시간으로 변경함.
		
		// 인증 객체에서 MemberVO를 추출함.
		MemberVO memberVO = (MemberVO) authentication.getPrincipal(); // 토큰에 있는 principal 객체를 꺼내옴. MemberVO를 꺼낸다.(SecurityConfig의 filterChain)
		log.debug("{} / {}", memberVO.getEmail(), memberVO.getName());
		
		String nextUrl = request.getParameter("nextUrl");
		
		response.sendRedirect(nextUrl);
	}
}
