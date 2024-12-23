package com.ktdsuniversity.edu.hello_spring.dao.impl;

import org.springframework.stereotype.Repository;
import com.ktdsuniversity.edu.hello_spring.dao.HelloBootDao;

/**
 * Repository 어노테이션을 붙이면 데이터베이스와 통신을 수행하는 클래스가 된다.
 * @Repository 가 관리하는 클래스
 *  -> @Service가 @Repository에 대해서 트랜잭션을 수행함.
 */
@Repository
public class HelloBootDaoImpl implements HelloBootDao {
	
	public HelloBootDaoImpl() {
		System.out.println("HelloBootDaoImpl 인스턴스 생성함.");
	}
	
	@Override
	public String selectMessage() {
		return "반갑습니다.";
	}
}
