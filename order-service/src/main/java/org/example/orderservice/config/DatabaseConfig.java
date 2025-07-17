package org.example.orderservice.config;

import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import org.example.orderservice.config.properties.DatabaseProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
public class DatabaseConfig {

    private final DatabaseProperties dbProperties;

    @Bean
    public DataSource dataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(dbProperties.getConnection().getUrl());
        dataSource.setUsername(dbProperties.getConnection().getUsername());
        dataSource.setPassword(dbProperties.getConnection().getPassword());
        dataSource.setDriverClassName(dbProperties.getConnection().getDriverClassName());

        dataSource.setMaximumPoolSize(dbProperties.getHikari().getMaximumPoolSize());
        dataSource.setMinimumIdle(dbProperties.getHikari().getMinimumIdle());
        dataSource.setConnectionTimeout(dbProperties.getHikari().getConnectionTimeout());

        return dataSource;
    }
}