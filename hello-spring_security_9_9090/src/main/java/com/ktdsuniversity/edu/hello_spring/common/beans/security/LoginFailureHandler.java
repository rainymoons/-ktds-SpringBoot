package com.ktdsuniversity.edu.hello_spring.common.beans.security;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import com.ktdsuniversity.edu.hello_spring.member.dao.MemberDao;
import com.ktdsuniversity.edu.hello_spring.member.vo.LoginMemberVO;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * SpringSecurity 인증 절차가 실패했을 때 예외를 처리할 클래스
 */
public class LoginFailureHandler implements AuthenticationFailureHandler{

	
	private MemberDao memberDao;
	
	public LoginFailureHandler(MemberDao memberDao) {
		this.memberDao = memberDao;
	}
	
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		
		// 예외 메시지(UsernameNotFoundException, BadCredentialsException)
		String exceptionMessage = exception.getMessage();
		
		// 인증을 요청한 이메일
		String email = request.getParameter("email");
		
		//TODO 로그인 실패 횟수 증가.
		
		
		// 위의 두개를 받아와서 로그인 페이지에 보여준다. memberlogin.jsp 참고. message라는 이름으로 전송
		// Model 전송
		request.setAttribute("message", exceptionMessage);
		
		LoginMemberVO loginMemberVO = new LoginMemberVO();
		loginMemberVO.setEmail(email);
		request.setAttribute("loginMemberVO", loginMemberVO);
		
		// view 전송
		RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/member/memberlogin.jsp");
		rd.forward(request, response);
	}
}
