package com.ktdsuniversity.edu.hello_spring.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ktdsuniversity.edu.hello_spring.dao.HelloBootDao;
import com.ktdsuniversity.edu.hello_spring.service.HelloBootService;

// 파일이 워낙 많기 때문에 Impl을 service에 넣지 않고 impl 패키지에 넣는다. service에는 interface만 존재.
// 여긴 @Controller 를 넣지 않는다. URL을 관리함.
// Service는 Transaction을 관리하는 컨테이너. -> 인스턴스 생성

// 스프링 빈 컨테이너 안에 있는 컨트롤러가 서비스를 호출하게 하려면 어떻게 해야하나.
@Service
public class HelloBootServiceImpl implements HelloBootService {

	@Autowired
	HelloBootDao helloBootDao;

	// 생성자로 체크.
	public HelloBootServiceImpl() {
		System.out.println("HelloBootServiceImpl 인스턴스 생성함.");
	}

	@Override
	public String getGreetingMessage() {
		
		String message = helloBootDao.selectMessage();
		
		return "안녕하세요, 서비스 클래스입니다." + message;
	}
}
