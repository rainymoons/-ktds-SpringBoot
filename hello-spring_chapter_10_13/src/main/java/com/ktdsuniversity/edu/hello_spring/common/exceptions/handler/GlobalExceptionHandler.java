package com.ktdsuniversity.edu.hello_spring.common.exceptions.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.ktdsuniversity.edu.hello_spring.common.exceptions.AlreadyUseException;
import com.ktdsuniversity.edu.hello_spring.common.exceptions.FileNotExistsException;
import com.ktdsuniversity.edu.hello_spring.common.exceptions.MakeXlsxFileException;
import com.ktdsuniversity.edu.hello_spring.common.exceptions.PageNotFoundException;
import com.ktdsuniversity.edu.hello_spring.common.exceptions.UserIdentifyNotMatchException;

// Spring Application에서 예외를 일괄 처리한다.
@ControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
	
	
	// PageNotfound 전용 화면을 보여주고 싶다.
	@ExceptionHandler(PageNotFoundException.class) // 이 예외가 터지면 어플리케이션 전역에서 작동한다.
	public String viewPageNotFoundPage() {

		if (logger.isDebugEnabled()) {
			logger.debug("Page를 찾을 수 없습니다.");
		}
		
		// 404.jsp로 보내고 싶음
		return "error/404";
	}
	
	// 이게 실행되려면 catch가 되면 안된다. 
	@ExceptionHandler(UserIdentifyNotMatchException.class)
	public String viewLoginErrorPage(Model model, UserIdentifyNotMatchException uinme) { // unime를 파라미터로 받아옴(예외 객체). 아래 model에서 쓰기 위해서.
		
		if (logger.isDebugEnabled()) {
			logger.debug("아이디 또는 비밀번호가 일치하지 않아요!");
		}
		
		// 던져진 예외를 할당 받을 수 있다.
		model.addAttribute("message", uinme.getMessage());
		model.addAttribute("loginMemberVO", uinme.getMemberVO());  
		
		return "member/memberlogin";
	}
	
	@ExceptionHandler({FileNotExistsException.class, MakeXlsxFileException.class})
	public String viewFileErrorPage(Model model, RuntimeException re) {// d왜 런타임임? xlsx 부모가 runtime이기 때문. 모든 예외가 들어올 수 있다는게 문제. 그래서 들어오는 예외의 인스턴스를 타입체크를 통해 사용해야 함.
		if (re instanceof FileNotExistsException) {
			FileNotExistsException fnee = (FileNotExistsException) re;
			model.addAttribute("message", fnee.getMessage());
		}
		else if (re instanceof MakeXlsxFileException) {
			MakeXlsxFileException mxfe = (MakeXlsxFileException) re;
			model.addAttribute("message" ,mxfe.getMessage());
		}
		return "error/500";
	}
	
	public String viewMemberRegistErrorPage(Model model, AlreadyUseException aue) {
		model.addAttribute("message" ,aue.getMessage());
		model.addAttribute("registmemberVO", aue.getRegistMemberVO());
		return "member/memberregist";
	}
	
//	public String viewLoginErrorPage(Model model, UserIdentifyNotMatchException uinme) {
//		if (logger.isDebugEnabled()) {
//			logger.debug("아이디 또는 비밀번호가 일치하지 않아요!");
//		}
//		return "member/login";
//	}
}
