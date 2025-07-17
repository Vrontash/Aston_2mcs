package org.example.orderservice.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties("database")
@Component
@Getter
@Setter
public class DatabaseProperties {

    private Connection connection = new DatabaseProperties.Connection();
    private Hikari hikari = new DatabaseProperties.Hikari();

    @Getter
    @Setter
    public static class Connection{
        private String url;
        private String username;
        private String password;
        private String driverClassName;
    }

    @Getter
    @Setter
    public static class Hikari{
        private int maximumPoolSize;
        private int minimumIdle;
        private int connectionTimeout;
    }

}
