package io.springbatch.springbatchlecture.domain.step.jobrepository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/*@Component
@Slf4j
public class JobRepositoryListener implements JobExecutionListener {

    @Autowired
    private JobRepository jobRepository;

    @Override
    public void beforeJob(JobExecution jobExecution) {

    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        String jobName = jobExecution.getJobInstance().getJobName();
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("requestDate", "20210102").toJobParameters();

        JobExecution lastJobExecution = jobRepository.getLastJobExecution(jobName, jobParameters);

        if (lastJobExecution != null) {
           for (StepExecution stepExecution : lastJobExecution.getStepExecutions()) {
               BatchStatus status = stepExecution.getStatus();
               ExitStatus exitStatus = stepExecution.getExitStatus();

               log.info("====== status : {}", status);
               log.info("====== exitStatus : {}", exitStatus);
           }
        }
    }
}*/
