package com.ktdsuniversity.edu.hello_spring.common.beans;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.ktdsuniversity.edu.hello_spring.access.dao.AccessLogDao;

// application.yml에서 설정하지 못하는 디테일한 설정을 위한 Annotation
// application.yml보다 높은 우선순위를 가지게 되면서 WhitelabelError 발생
// Spring Bean 을 수동으로 생성하는 기능
@Configuration
// Spring WebMvc에 있는 다양한 요소를 활성화 해주는 Annotation
// 		- Spring Validator
// 		- Spring Inteceptor
// 		- ...
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer{
	
	@Autowired
	private AccessLogDao accessLogDao; // accessLogDao를 인젝션 받는다.
	
	// addInterceptor의 url 리스트를 application.yml로 옮기면서 추가.직접적인 경로설정 사항은 뺸다.
	// pathpattern 적용 : application.yml @value들이 ,로 구분되어 있으면 List로 가져온다.
	@Value("${app.interceptors.check-session.path-patterns}")
	private List<String> checkSessionPathPatterns;
	@Value("${app.interceptors.check-session.exclude-path-patterns}")
	private List<String> checkSessionExcludePathPatterns;

	@Value("${app.interceptors.check-dup-login.path-patterns}")
	private List<String> checkDupLoginPathPatterns;
	@Value("${app.interceptors.check-dup-login.exclude-path-patterns}")
	private List<String> checkDupLoginExcludePathPatterns;
	
	@Value("${app.interceptors.add-access-log.path-patterns}")
	private List<String> addAcessLogPathPatterns;
	@Value("${app.interceptors.add-access-log.exclude-path-patterns}")
	private List<String> addAcessLogExcludePathPatterns;
	
	/**
	 * Auto DI: @Component
	 * Manual DI: @Bean
	 *  -> 객체 생성을 스프링이 아닌 개발자가 직접 하는 것
	 * @return
	 */
	@Bean // Sha라는 이름의 Bean이 생김
	Sha createShaInstance() {
		Sha sha = new Sha();
		return sha;
	}

	// 우선순위가 바뀌게 되면서 yml에 있는 jsp 경로를 우선순위로 변경해줘야 함
	/**
	 * JSP View Resolver 설정
	 */
	@Override
	public void configureViewResolvers(ViewResolverRegistry registry) {
		registry.jsp("/WEB-INF/views/", ".jsp");
	}
	
	/**
	 * Static Resource 설정 (CSS, JS)
	 */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/css/**").addResourceLocations("classpath:/static/css/"); // css 경로
		registry.addResourceHandler("/js/**").addResourceLocations("classpath:/static/js/"); // js 경로
	}
	
	/**
	 * 인터셉터 등록
	 * 인터셉터를 스프링 MVC의 요청 처리 흐름에 추가할 수 있다. 
	 * preHandler의 경우 controller에서 동작함.
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// List를 만들어서 개입하지 않았으면 하는 url들을 적어준다.
		/* 아래 내용을 application.yml로 이동.
		 * List<String> excludeCheckSessionInterceptorURL = new ArrayList<>();
		 * excludeCheckSessionInterceptorURL.add("/js/**");
		 * excludeCheckSessionInterceptorURL.add("/css/**");
		 * excludeCheckSessionInterceptorURL.add("/image/**");
		 * excludeCheckSessionInterceptorURL.add("/member/login");
		 * excludeCheckSessionInterceptorURL.add("/member/regist/**");
		 */
		
		
		// first interceptor
		registry.addInterceptor(new CheckSessionInterceptor())// mvc 라이프사이클(Spring flow)에 등록. (스프링 빈으로 등록)
				//  checkSessionPathPatterns 경로에 대해 CheckSessionInterceptor가 동작한다.
				.addPathPatterns(this.checkSessionPathPatterns) // 개입할 패턴 지정 : 모든 패턴("/**")
				// 인터셉터가 동작하지 않도록 제외할 경로의 패턴 리스트
				.excludePathPatterns(this.checkSessionExcludePathPatterns); 
		
		// second interceptor -> 이미 로그인한 클라이언트가 로그인페이지에 다시 접근하지 못하도록 
		registry.addInterceptor(new CheckDuplicateLoginInterceptor())
				.addPathPatterns(this.checkDupLoginPathPatterns) // 얘는 String을 받는 addPathPatterns.
				.excludePathPatterns(this.checkDupLoginExcludePathPatterns);
		
		// third interceptor (this.accessLogDao -> bean을 주는 것 : 생성자 주입) -> 마지막에 호출.
		registry.addInterceptor(new AddAccessLogHistoryInterceptor(this.accessLogDao))
				.addPathPatterns(this.addAcessLogPathPatterns)
				.excludePathPatterns(this.addAcessLogExcludePathPatterns); // 패키지가 없으니 로그를 남기지 않음.
		
	}
	
}
