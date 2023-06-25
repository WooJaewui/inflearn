package io.springbatch.springbatchlecture.jobexecution.jobbuilderfactory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@RequiredArgsConstructor
@Slf4j
public class JobBuilderFactoryConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;


    @Bean
    public Job batchJob1() {
        return jobBuilderFactory.get("batchJob1")
                .start(step1())
                .next(step2())
                .build();
    }

    @Bean
    public Job batchJob2() {
        return jobBuilderFactory.get("batchJob2")
                .start(flow())
                .next(step5())
                .end()
                .build();
    }


    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .tasklet((contribution, chunkContext) -> {
                    log.info("==== step1 was executed ======");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Step step2() {
        return stepBuilderFactory.get("step2")
                .tasklet((contribution, chunkContext) -> {
                    log.info("==== step2 was executed ======");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Flow flow() {

        FlowBuilder<Flow> flowFlowBuilder = new FlowBuilder<>("flow");
        flowFlowBuilder.start(step3())
                .next(step4())
                .end();

        return flowFlowBuilder.build();
    }



    @Bean
    public Step step3() {
        return stepBuilderFactory.get("step3")
                .tasklet((contribution, chunkContext) -> {
                    log.info("==== step3 was executed ======");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }


    @Bean
    public Step step4() {
        return stepBuilderFactory.get("step4")
                .tasklet((contribution, chunkContext) -> {
                    log.info("==== step4 was executed ======");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }


    @Bean
    public Step step5() {
        return stepBuilderFactory.get("step5")
                .tasklet((contribution, chunkContext) -> {
                    log.info("==== step5 was executed ======");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }


}
