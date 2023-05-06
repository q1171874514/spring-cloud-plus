package com.example.config;


import io.seata.rm.datasource.DataSourceProxy;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;


//@Configuration
public class DataSourceConfig {



    @Bean  // 注意：这里DataSourceProxy导的是seata的包
    public DataSourceProxy dataSourceProxy(@Qualifier("dataSource") DataSource dataSource) {
        return new DataSourceProxy(dataSource);
    }



}
