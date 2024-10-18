package com.ktdsuniversity.edu.batch;

import java.util.HashSet;
import java.util.Set;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ktdsuniversity.edu.batch.jobs.TestJob;
import com.ktdsuniversity.edu.batch.jobs.TestJob2;

public class BatchMain {

	private static final Logger logger = LoggerFactory.getLogger(BatchMain.class);
	
	public static void main(String[] args) {
	
		Scheduler scheduler = makeScheduler();
		
		if (scheduler != null) {
			// 배치에서는 스케줄러가 실행시키는 것을 job이라고 한다.
			// 0/5 5초마다. 0/1(일) -> 매일 자정에. 0/1(월)-> 매월 1일에
			//                                                 초 분 시 일 월 
			addSchedule(scheduler, TestJob.class, "testJob", "0/5 * * * * ?"); // TestJob 인스턴스가 5초에 한번씩 동작
			addSchedule(scheduler, TestJob2.class, "testJob2", "0/5 * * * * ?");
			
			start(scheduler);
		}
	}
	
	private static Scheduler makeScheduler() {
		SchedulerFactory schedulerFactory = new StdSchedulerFactory();
		try {
			return schedulerFactory.getScheduler();
		} catch (SchedulerException e) {
			logger.error(e.getMessage());
			return null;
		}
	}
	
	private static void start(Scheduler scheduler) {
		try {
			scheduler.start();
		} catch (SchedulerException e) {
			logger.error(e.getMessage());
		}
	}
	
	private static void addSchedule(Scheduler scheduler, Class<? extends Job> jobDetail, String jobName, String cronSchedule) {
		JobDetail job = createJobDetail(jobDetail);
		CronTrigger jobTrigger = createJobTrigger(job, jobName, cronSchedule);
		
		Set<Trigger> jobTriggerSet = new HashSet<>();
		jobTriggerSet.add(jobTrigger);
		
		try {
			scheduler.scheduleJob(job, jobTriggerSet, false);
		} catch (SchedulerException e) {
			logger.error(e.getMessage());
		}
		
	}
	
	private static JobDetail createJobDetail(Class<? extends Job> jobDetail) {
		JobDetail job = JobBuilder.newJob(jobDetail)
		          .withIdentity(jobDetail.getSimpleName(), "job_group")
		          .build();
		return job;
	}
	
	private static CronTrigger createJobTrigger(JobDetail jobDetail, String jobName, String cronSchedule) {
		return TriggerBuilder.newTrigger()
							 .withIdentity(jobName, jobName + "cron_trigger_group")
							 .withSchedule(CronScheduleBuilder.cronSchedule(cronSchedule))
							 .forJob(jobDetail)
							 .build();
	}
	
}
