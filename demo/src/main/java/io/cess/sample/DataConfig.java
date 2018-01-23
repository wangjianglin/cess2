package io.cess.sample;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

public class DataConfig {

//
//    @Bean
//    @Primary
//    @ConfigurationProperties("spring.primary.datasource")
//    public DataSourceProperties primaryDataSourceProperties() {
//        return new DataSourceProperties();
//    }
//
//    @Bean(name = "primaryDS")
//    @Qualifier("primaryDS")
//    @Primary
//    @ConfigurationProperties(prefix="spring.primary.datasource")
//    public DataSource primaryDataSource(){
//        return primaryDataSourceProperties().initializeDataSourceBuilder().build();
//    }

//
//    @Bean
//    @Primary
//    @ConfigurationProperties("spring.secondary.datasource")
//    public DataSourceProperties secondaryDataSourceProperties() {
//        return new DataSourceProperties();
//    }
//
//    @Bean(name = "secondaryDS")
//    @Qualifier("secondaryDS")
//    @ConfigurationProperties(prefix="spring.secondary.datasource")
//    public DataSource secondaryDataSource(){
////        return DataSourceBuilder.create().build();
//        return secondaryDataSourceProperties().initializeDataSourceBuilder().build();
//    }
}
