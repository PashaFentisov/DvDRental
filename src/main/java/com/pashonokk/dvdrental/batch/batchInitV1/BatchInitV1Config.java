package com.pashonokk.dvdrental.batch.batchInitV1;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BatchInitV1Config {
    @Bean(name = "1")
    public Job job(JobRepository jobRepository, Step processCountryStep, Step processCityStep) {
        return new JobBuilder("batchInitV1ProcessJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .preventRestart()
                .flow(processCountryStep)
                .next(processCityStep)
                .end()
                .build();
    }
}
