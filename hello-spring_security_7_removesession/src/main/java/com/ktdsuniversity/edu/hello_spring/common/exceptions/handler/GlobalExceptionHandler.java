package com.ktdsuniversity.edu.hello_spring.common.exceptions.handler;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ktdsuniversity.edu.hello_spring.common.exceptions.AjaxException;
import com.ktdsuniversity.edu.hello_spring.common.exceptions.AlreadyUseException;
import com.ktdsuniversity.edu.hello_spring.common.exceptions.FileNotExistsException;
import com.ktdsuniversity.edu.hello_spring.common.exceptions.MakeXlsxFileException;
import com.ktdsuniversity.edu.hello_spring.common.exceptions.PageNotFoundException;

// Spring Application에서 예외를 일괄 처리한다.
@ControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
	
	@ExceptionHandler(AjaxException.class)
	@ResponseBody // 컨트롤러로 보내기 위한 것.
	public Map<String, Object> returnAjaxErrorMessage(AjaxException ae) {
		Map<String, Object> ajaxErrorMap = new HashMap<>();
		ajaxErrorMap.put("error", ae.getMessage());
		return ajaxErrorMap;
	}
	
	// PageNotfound 전용 화면을 보여주고 싶다.
	@ExceptionHandler(PageNotFoundException.class) // 이 예외가 터지면 어플리케이션 전역에서 작동한다.
	public String viewPageNotFoundPage() {

		if (logger.isDebugEnabled()) {
			logger.debug("Page를 찾을 수 없습니다.");
		}
		
		// 404.jsp로 보내고 싶음
		return "error/404";
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
	
	@ExceptionHandler(RuntimeException.class)
	public String viewRuntimeExceptionPage(Model model, RuntimeException re) {
		logger.error(re.getMessage(), re); // 에러 체크용 수정.
		model.addAttribute("message", "예기치 못한 에러가 발생했습니다.잠시후 다시 시도해 주세요.");
		
		return "error/500";
	}
	
}
