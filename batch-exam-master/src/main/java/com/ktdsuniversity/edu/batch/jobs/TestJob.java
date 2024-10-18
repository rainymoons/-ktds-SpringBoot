package com.ktdsuniversity.edu.batch.jobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ktdsuniversity.edu.batch.beans.Beans;
import com.ktdsuniversity.edu.batch.service.TestService;

// TestJob은 스프링 빈이 아님. 그래서 TestService를 가져올 수 없음.(TestService는 스프링 빈)
public class TestJob implements Job { // 반드시 impl

	private static final Logger logger = LoggerFactory.getLogger(TestJob.class);
	
	// beans 클래스가 필요함.
	private TestService testService;
	
	public TestJob() { // beans에 의해 testService 빈이 전달된다.
		testService = Beans.getInstance().getBean("testService");
	}
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		logger.info("TestJob Start");
		
		String text = testService.getText();
		
		logger.info("TestJob 처리 중... {}", text);
		
		logger.info("TestJob End");
	}

}
