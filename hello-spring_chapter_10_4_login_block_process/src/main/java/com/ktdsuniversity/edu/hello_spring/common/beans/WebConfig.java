package com.ktdsuniversity.edu.hello_spring.common.beans;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// application.yml 에서 설정하지 못하는 디테일한 설정을 위한 애노테이션.
// Spring Bean을 수동으로 생성하는 기능.
@Configuration
// Spring WebMVC에 필요한 다양한 요소를 활성화 시키는 애노테이션.
//    - Spring Validator
//    - Spring Inteceptor
//    - ...
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

	/**
	 * Auto DI: @Component
	 * Manual DI: @Bean
	 * 	-> 객체 생성을 스프링이 아닌 개발자가 직접 하는 것!
	 * @return
	 */
	@Bean
	Sha createShaInstance() {
		Sha sha = new Sha();
		return sha;
	}
	
	
	/**
	 * JSP View Resolver 설정
	 */
	@Override
	public void configureViewResolvers(ViewResolverRegistry registry) {
		registry.jsp("/WEB-INF/views/", ".jsp");
	}
	
	/**
	 * Static Resource 설정.
	 */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/css/**") // http://localhost:8080/css/common/common.css
			    .addResourceLocations("classpath:/static/css/");
		registry.addResourceHandler("/js/**") // http://localhost:8080/js/jquery/jquery-3.7.1.min.js
				.addResourceLocations("classpath:/static/js/");
	}
	
}
