package com.example.demo;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties("servicebus")
public class SBConfiguration {
    private final String connectionString;
    private final String queueName;

    public SBConfiguration(String connectionString, String queueName) {
        this.connectionString = connectionString;
        this.queueName = queueName;
    }

    public String getConnectionString() {
        return connectionString;
    }

    public String getQueueName() {
        return queueName;
    }
}
