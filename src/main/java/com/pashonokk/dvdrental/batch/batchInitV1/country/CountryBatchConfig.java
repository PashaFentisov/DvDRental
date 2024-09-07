package com.pashonokk.dvdrental.batch.batchInitV1.country;


import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class CountryBatchConfig {

    @Bean("countryItemProcessor")
    public ItemProcessor<CountryData, CountryData> itemProcessor() {
        return (countryData)->countryData;
    }

    @Bean("countryItemReader")
    public FlatFileItemReader<CountryData> csvCountryReader() {
        return new FlatFileItemReaderBuilder<CountryData>().name("csv-country-reader")
                .resource(new ClassPathResource("batch/countries.csv"))
                .targetType(CountryData.class)
                .delimited()
                .names("name").build();
    }


    @Bean
    public Step processCountryStep(@Qualifier("countryItemReader") ItemReader<CountryData> csvReader,
                                   @Qualifier("countryItemProcessor") ItemProcessor<CountryData, CountryData> itemProcessor,
                                   @Qualifier("countryItemWriter") ItemWriter<CountryData> dbWriter,
                                   JobRepository jobRepository,
                                   PlatformTransactionManager transactionManager) {

        return new StepBuilder("country-step", jobRepository)
                .<CountryData, CountryData>chunk(2, transactionManager)
                .reader(csvReader)
                .processor(itemProcessor)
                .writer(dbWriter)
                .build();
    }
}
