package com.ktdsuniversity.edu.hello_spring.common.beans;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// application.yml에서 설정하지 못하는 디테일한 설정을 위한 Annotation
// application.yml보다 높은 우선순위를 가지게 되면서 WhitelabelError 발생
@Configuration
// Spring WebMvc에 있는 다양한 요소를 활성화 해주는 Annotation
// 		- Spring Validator
// 		- Spring Inteceptor
// 		- ...
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer{

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
	
}
