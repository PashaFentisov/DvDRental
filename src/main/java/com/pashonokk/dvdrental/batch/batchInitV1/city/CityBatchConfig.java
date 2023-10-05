package com.pashonokk.dvdrental.batch.batchInitV1.city;

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
public class CityBatchConfig {
    @Bean("cityItemProcessor")
    public ItemProcessor<CityData, CityData> itemProcessor() {
        return (cityData)->cityData;  //todo можливо приймати не катрі ід а імя тоді тут процесити і брати по імені катнрі її ід
    }

    @Bean("cityItemReader")
    public FlatFileItemReader<CityData> csvCityReader() {
        return new FlatFileItemReaderBuilder<CityData>().name("csv-city-reader")
                .resource(new ClassPathResource("batch/cities.csv"))
                .targetType(CityData.class)
                .delimited()
                .delimiter("|")
                .names("name", "countryId").build();
    }


    @Bean
    public Step processCityStep(@Qualifier("cityItemReader") ItemReader<CityData> csvReader,
                                   @Qualifier("cityItemProcessor") ItemProcessor<CityData, CityData> itemProcessor,
                                   @Qualifier("cityItemWriter") ItemWriter<CityData> dbWriter,
                                   JobRepository jobRepository,
                                   PlatformTransactionManager transactionManager) {

        return new StepBuilder("city-step", jobRepository)
                .<CityData, CityData>chunk(2, transactionManager)
                .reader(csvReader)
                .processor(itemProcessor)
                .writer(dbWriter)
                .build();
    }
}
