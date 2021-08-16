package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SBService {
    @Autowired
    private SBProducer producer;

    @Autowired
    private SBConsumer consumer;

    public void sendMessage(String message) {
        producer.send(message);
    }

    public void receiveMessage() {
        consumer.receive();
    }
}
