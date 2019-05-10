package com.example.shortbin.sqlite;

import com.example.shortbin.AppConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DBConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(DBConfiguration.class);
    private String dbPath;

    @Autowired
    public DBConfiguration(AppConfiguration config) {
        if (config == null) {
            logger.error("Failed to load configuration");
            throw new RuntimeException("Failed to load configuration");
        }
        dbPath = config.getDbPath();
    }

    @Bean
    public DataSource dataSource() {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName("org.sqlite.JDBC");
        dataSourceBuilder.url(String.format("jdbc:sqlite:%s", dbPath));
        return dataSourceBuilder.build();
    }
}
