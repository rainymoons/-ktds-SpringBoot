package com.ktdsuniversity.edu.batch.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ktdsuniversity.edu.batch.dao.TestDao;

@Service // 빈 이름은 "testService"가 된다. 
public class TestService {

	@Autowired
	private TestDao testDao;
	
	public String getText() {
		int count = testDao.getBoardAllCount();
		System.out.println(count);
		return "test";
	}
	
}
