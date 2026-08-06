package com.play.quiz.jobs;

import lombok.extern.log4j.Log4j2;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

@Log4j2
public class TimingJob implements Job {

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("Executing job {}", jobExecutionContext.getJobDetail().getKey());
    }
}
