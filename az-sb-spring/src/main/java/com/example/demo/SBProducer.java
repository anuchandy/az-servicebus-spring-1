package com.example.demo;

import com.azure.messaging.servicebus.ServiceBusClientBuilder;
import com.azure.messaging.servicebus.ServiceBusMessage;
import com.azure.messaging.servicebus.ServiceBusSenderClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class SBProducer {
    @Autowired
    private SBConfiguration configuration;

    private ServiceBusSenderClient senderClient;

    @PostConstruct
    public void init() {
        this.senderClient = new ServiceBusClientBuilder()
                .connectionString(this.configuration.getConnectionString())
                .sender()
                .queueName(this.configuration.getQueueName())
                .buildClient();
    }

    public void send(String message) {
        this.senderClient.sendMessage(new ServiceBusMessage(message));
    }
}
