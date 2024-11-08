package com.ktdsuniversity.edu.hello_spring.common.beans;

import org.springframework.web.servlet.HandlerInterceptor;

import com.ktdsuniversity.edu.hello_spring.member.vo.MemberVO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * 로그인 되어 있는 상태에서 로그인 페이지에 접근하면 
 * /board/list로 이동시키는 인터셉터.
 */
public class CheckDuplicateLoginInterceptor implements HandlerInterceptor{

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		// 세션 객체를 가져온다. // request.getSession()은 현재 요청과 관련된 세션을 반환함.
		HttpSession session = request.getSession();
		
		// Session에서 _LOGIN_USER_라는 이름으로 저장된 사용자 정보(MemberVO)를 가져온다.
		MemberVO memberVO = (MemberVO) session.getAttribute("_LOGIN_USER_"); 
		
		if (memberVO != null) { // 사용자가 로그인 상태라면
			// 로그인된 사용자는 로그인 페이지에 접근하면 안되므로, /board/list로 리다이렉트
			response.sendRedirect("/board/list");
			// 스프링은 요청 처리를 중단하고 컨트롤러로 요청을 전달하지 않는다. -> board/list로 이동됨.
			return false;
		}
		// 로그인 되지 않은 경우 true 반환. 요청 처리가 진행되어 컨트롤러가 실행된다. -> 로그인 페이지로 접근.
		return true;
	}
}
