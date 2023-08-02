package io.springbatch.springbatchlecture.domain.step.joblauncher;

import lombok.extern.slf4j.Slf4j;

@Slf4j
//@RestController
public class JobLauncherController {

    /*@Autowired
    private Job job;
    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private BasicBatchConfigurer basicBatchConfigurer;

    @RequestMapping("/batch")
    public String launch(@RequestBody Member member) throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {

        JobParameters jobParameters = new JobParametersBuilder()
                .addString("memberId", member.getId())
                .addDate("date", new Date()).toJobParameters();

        SimpleJobLauncher simpleJobLauncher = (SimpleJobLauncher) basicBatchConfigurer.getJobLauncher();
        simpleJobLauncher.setTaskExecutor(new SimpleAsyncTaskExecutor());
        //simpleJobLauncher.run(job, jobParameters);
        jobLauncher.run(job, jobParameters);

        return "batch completed";
    }*/

}
