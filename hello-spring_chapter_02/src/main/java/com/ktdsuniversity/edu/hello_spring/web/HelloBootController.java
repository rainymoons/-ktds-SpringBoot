package com.ktdsuniversity.edu.hello_spring.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.ktdsuniversity.edu.hello_spring.service.HelloBootService;

@Controller // 브라우저와 통신할 수 있는 클래스.
public class HelloBootController {

	/**
	 * @Autowired -> DI(Dependency Injection)를 처리하는 Annotation. -> Spring Bean Container에 있는 인스턴스를 주입한다.
	 * 단, 멤버변수의 타입과 Spring Bean Container에 있는 인스턴스의 타입이 같은것만 주입한다.
	 * 왜 타입이 같은가? HelloBootService와 HelloBootServiceImpl은 is a 관계이기 때문. 구현을 해도 is a 관계가 설정된다.
	 */
	@Autowired // DI(Dependency Injection)를 처리하는 Annotation. -> Spring Bean Container에 있는 인스턴스를 주입한다.
	private HelloBootService helloBootService;
	
	
//	public HelloBootController() {
//		System.out.println("@Controller가 적용된 클래스는 스프링이 직접 인스턴스로 만들어서 Bean Container에 보관한다.");
//	}
	
	@GetMapping("/print")  // http://localhost8080/print
	public void printConsoleURL() {
		System.out.println("브라우저에 의해서 호출됬습니다.");
	}
	
	@GetMapping("/text") // http://localhost:8080/text
	public ResponseEntity<String> printText() {
		// 브라우저에 보낼 타입을 제네릭에 넣어준다.
		// http status 코드, String을 보내줘야함.
//		return new ResponseEntity<>("브라우저로 텍스트를 전달합니다.", HttpStatus.OK);
		
		// notfound 에러 페이지 유발
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
	
	// 일반적으로 ResponseEntity는 file을 다운로드 하거나 업로드할 때 사용.
	@GetMapping("/html")
	public ResponseEntity<String> printHtml() {
		return new ResponseEntity<>("""
				<!DOCTYPE html>
				<html>
					<head>
						<title> Spring Test</title>
					</head>
					<body>
						<h1>Hello Spring Boot</h1>
					</body>
				</html>
				""" , HttpStatus.OK);
	}
	
	
	/**
	 * http://localhost:8080/jsp로 브라우저가 요청을 하면
	 * 스프링 컨트롤러는 /WEB-INF/views/helloJsp.jsp 을 읽어와서
	 * html로 변환한 후 브라우저에게 돌려준다.
	 * @return
	 */
	
	// RequsetDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/hello.jsp"); 이거랑 아래랑 같음.
	// JSP 전송
	@GetMapping("/jsp") //http://localhost:8080/jsp
	public String viewJSP() {
		// .jsp파일 이름
		return "helloJsp";
	}
	
	// 현재는 사용되지 않는 방식
	@GetMapping("/jsp2") //http://localhost:8080/jsp
	public ModelAndView viewJSPWithModelAndView() {
		
		/*
		 * Model : JSP에 보내질 데이터
		 * View : 브라우저에게 보여줄 화면
		 * 
		 * 데이터와 화면을 함께 반환시킨다. 
		 */
		ModelAndView view = new ModelAndView("helloJsp"); // yml파일에 있던 prefix와 suffix가 붙음.
		view.addObject("applicationName", "HelloBoot!");
		return view;
	}
	
	// 최신 방법
	@GetMapping("/jsp3") //http://localhost:8080/jsp
	public String viewJSPWithModel(Model model) {
		
		// 서비스 호출 -> helloBootServiceImpl 
		String greetingMessage = this.helloBootService.getGreetingMessage();
		model.addAttribute("applicationName", greetingMessage);
		
		
		// jsp에 데이터를 보내고 싶을때는 model이라는 파라미터 사용.
		// applicationName이라는 이름으로 SpringBoot 3.3.4 전달
		// model.addAttribute("applicationName", "Spring Boot 3.3.4");
		return "helloJsp";
	}
	/*
	 * 서블릿이 위 메소드를 호출시킨다. URL이 같으면. 
	 * controller에 파라미터가 존재할 경우 servlet이 해당하는 파라미터를 넣어준다. -> Java Reflection(메소드를 직접호출하지 않더라도 메서드를 호출시키는 기능. 파라미터가 무엇인지 타입이 무엇인지 체크 가능)
	 * 
	 */
	 
}
