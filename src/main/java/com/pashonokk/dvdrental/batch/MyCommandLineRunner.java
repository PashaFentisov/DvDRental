package com.pashonokk.dvdrental.batch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class MyCommandLineRunner implements CommandLineRunner {
    private final JobLauncher jobLauncher;
    private final ConfigurableApplicationContext applicationContext;
    @Value("${app.initializer.version}")
    private Long initializerVersion;
    private final BatchInitRepository batchInitRepository;

    @Override
    public void run(String... args) throws Exception {
        Long lastVersion = batchInitRepository.findLastVersion();
        if (Objects.equals(lastVersion,
                           initializerVersion)) {
            return;
        }
        Job job;
        if (lastVersion == null) {
            lastVersion = 0L;
        }
        for (long i = ++lastVersion; i <= initializerVersion; i++) {
            job = applicationContext.getBean(String.valueOf(i), Job.class);
            jobLauncher.run(job, new JobParameters());
        }
        batchInitRepository.save(new BatchInit(initializerVersion));
    }
}