package com.ktdsuniversity.edu.hello_spring.common.beans;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;

import com.ktdsuniversity.edu.hello_spring.access.dao.AccessLogDao;
import com.ktdsuniversity.edu.hello_spring.access.vo.AccessLogVO;
import com.ktdsuniversity.edu.hello_spring.member.vo.MemberVO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AddAccessLogHistoryInterceptor implements HandlerInterceptor {
	
	// 데이터베이스에 접근하기 위한 객체. 로그정보를 데이터베이스에 저장.
	private AccessLogDao accessLogDao;
	
	// AccessLogDao 생성자 주입
	public AddAccessLogHistoryInterceptor(AccessLogDao accessLogDao) {
		this.accessLogDao = accessLogDao;
	}

	// 사용자의 접근 로그를 남기기 위한 기록.
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		// 현재 인증이 되어있는 사용자의 정보를 가지고 올 수 있다.
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		// 로그인된 사용자 정보를 가져오고 (memberVO가 들어있음)
		// Class cast Exception (String -> MemberVO)
		// 인증 되기 전의 Authentication에는 "anonymousUser"값이 할당 되어 있음.
		// anonymousUser 값을 MemberVO로 형변환 할 수 없는 문제.
		
		boolean isAuthenticated = authentication.getPrincipal() instanceof MemberVO;
		
		MemberVO memberVO = isAuthenticated ? (MemberVO) authentication.getPrincipal() : null ; // principal에 MemberVO가 들어있냐?
		
		// 로그인된 경우 memberVO가 null이 아니므로 클라이언트의 이메일 주소를 가져와서 email에 저장함.
		String email = memberVO == null ? null : memberVO.getEmail();
		
		// 핸들러(컨트롤러)의 정보를 문자열로 가져옴.
		String controller = handler.toString();
		
		// 패키지 이름만 따오기 위한 작업. BoardController의 경우 BOARD로 변환. -> 어떤 컨트롤러에 접근했는지 파악.
		String packageName = controller.replace("com.ktdsuniversity.edu.hello_spring.", "");
		packageName = packageName.substring(0, packageName.indexOf(".")).toUpperCase();
		
		// 로그데이터 저장.
		AccessLogVO accessLogVO = new AccessLogVO();
		accessLogVO.setAccessType(packageName); // 접근한 컨트롤러 이름
		accessLogVO.setAccessEmail(email); // 사용자 이메일. 로그인 안됬을 경우 Null
		accessLogVO.setAccessUrl(request.getRequestURI()); // UR I (/board/list)
		accessLogVO.setAccessMethod(request.getMethod().toUpperCase()); // HTTP Method -> GET, POST ..
		accessLogVO.setAccessIp(request.getRemoteAddr()); // 클라이언트의 IP 주소.
		accessLogVO.setLoginSuccessYn(memberVO == null ? "N" : "Y"); // 로그인 여부 Y or N으로 저장.
		
		// accessLogVO에 저장된 로그를 DB에 기록.
		this.accessLogDao.insertNewAccessLog(accessLogVO);
		
		// true 반환시 요청처리 진행 -> 컨트롤러 실행 -> 로그 기록후에도 진행되기 위한 것.
		return true;
	}

}
