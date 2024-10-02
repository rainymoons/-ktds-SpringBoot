package com.ktdsuniversity.edu.hello_spring.common.beans;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// application.yml에서 설정하지 못하는 디테일한 설정을 위한 annotation.
@Configuration
// Spring Web MVC에 필요한 다양한 요소를 활성화 시키는 annotation.
// 	- Spring Validator
//  - Spring Intercepter
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {
	// 여기까지만 작성해도 Spring Validator가 동작한다. 근데 에러 발생
	
	
	/**
	 * JSP View Resolver 설정 - jsp불러오기 위한 것.
	 */
	@Override
	public void configureViewResolvers(ViewResolverRegistry registry) {
		// application.yml 파일의 내용을 가져온다.
		registry.jsp("/WEB-INF/views/", ".jsp");
	}
	
	/**
	 * static Resource 설정 - css를 가져오기 위함
	 */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// **의 의미. css밑에 있는 '모든'경로 
		registry.addResourceHandler("/css/**") // ex) http://localhost:8080/css/common/common.css
			    .addResourceLocations("classpath:/static/css/"); // 물리적인 파일명을 여기 적어준다.
		registry.addResourceHandler("/js/**") // http:localhost:8080/js/jquery/jquery-3.1.7.min.js
				.addResourceLocations("classpath:/static/js/");
	}
}
