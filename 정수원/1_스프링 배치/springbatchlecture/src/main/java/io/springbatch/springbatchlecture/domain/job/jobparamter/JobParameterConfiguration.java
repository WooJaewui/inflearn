package io.springbatch.springbatchlecture.domain.job.jobparamter;

/*@Configuration
@RequiredArgsConstructor
@Slf4j
public class JobParameterConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job job() {
        return jobBuilderFactory.get("job")
                .start(step1())
                .next(step2())
                .build();
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .tasklet((contribution, chunkContext) -> {

                    JobParameters jobParameters = contribution.getStepExecution().getJobExecution().getJobParameters();
                    String name = jobParameters.getString("name");
                    Long seq = jobParameters.getLong("seq");
                    Date date = jobParameters.getDate("date");
                    Double age = jobParameters.getDouble("age");

                    log.info("{}, {}, {}, {}", name, seq, date, age);

                    log.info("============== step1 was execute =============");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Step step2() {
        return stepBuilderFactory.get("step2")
                .tasklet((contribution, chunkContext) -> {

                    Map<String, Object> jobParameters = chunkContext.getStepContext().getJobParameters();

                    Object name = jobParameters.get("name");
                    Object seq = jobParameters.get("seq");
                    Object date = jobParameters.get("date");
                    Object age = jobParameters.get("age");

                    log.info("{}, {}, {}, {}", name, seq, date, age);
                    log.info("============== step2 was execute =============");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

}*/
