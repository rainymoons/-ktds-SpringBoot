package com.ktdsuniversity.edu.hello_spring.common.beans;

import org.springframework.web.servlet.HandlerInterceptor;

import com.ktdsuniversity.edu.hello_spring.member.vo.MemberVO;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * 요청과 응답을 가로채는 인터셉터.
 * 
 * checkSession과 checkDuplicate 둘 다 합칠 수 있을 것 같음.
 * 
 * 이 클래스는 단순히 로그인이 되어있다면 컨트롤러를 정상적으로 진행시키고, 로그인이 되어있지 않은 상태에서 특정 페이지에 접근하면
 * 로그인페이지로 이동시키는 HandlerInterceptor의 구현체 클래스
 * 
 */
public class CheckSessionInterceptor implements HandlerInterceptor{

	// 동작이 되려면 스프링 빈에 등록이 되어야 함.
	@Override
	public boolean preHandle(HttpServletRequest request, 
							 HttpServletResponse response, 
							 Object handler) //원래 동작을 시켜야 되는 컨트롤러의 정보.
									 throws Exception {
		/*
		 * 컨트롤러가 실행되기 전 로그인 세션을 검사해서
		 * 로그인이 되어있지 않다면, 로그인 페이지를 보여주도록 한다.
		 * true 반환 -> 컨트롤러로 요청이 전달되고 컨트롤러가 정상적으로 실행됨
		 * false 반환 -> 요청 처리가 중단되고, 컨트롤러가 실행되지 않음.
		 */
		// 1. 클라이언트의 세션 가져오기
		HttpSession session = request.getSession();
		
		// 2. 세션이 존재한다면 컨트롤러 실행시키기
		
		// 세션에서 _LOGIN_USER_라는 이름으로 저장된 사용자 정보를 가져온다.
		MemberVO memberVO = (MemberVO) session.getAttribute("_LOGIN_USER_");
		// member VO가 NULL이 아니면 로그인을 한 상태. null이라면 로그인을 하지 않은 상태 
		if (memberVO != null) {
			return true; // 컨트롤러 정상적으로 실행
		}
		
		// 3. 세션이 존재하지 않는다면(세션에 memberVO가 없으면) 로그인 페이지 보여주기 -> 컨트롤러를 실행하면 안됨
		
		// RequestDispatcher로 인해 memberlogin.jsp로 요청을 포워드(서버 내부에서 페이지를 이동하는 방법)한다.
		// 실질적으로 동작시키기 위해서는 .jsp 파일에서 action 설정이 이루어져야 한다.
		RequestDispatcher rd = request.getRequestDispatcher("WEB-INF/views/member/memberlogin.jsp");
		rd.forward(request, response); // 로그인 페이지로 요청 전달.
		
		return false; // 컨트롤러를 실행시키지 않는다.
		// 로그인 페이지로 이동. 원래 요청된 컨트롤러는 호출되지 않고 로그인 페이지가 표시된다.
	}
}
