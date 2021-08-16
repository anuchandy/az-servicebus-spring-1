package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicInteger;

@RestController
public class HomeController {
    private final AtomicInteger sendCount = new AtomicInteger();

    @Autowired
    private SBService service;

    @GetMapping("/sendMessage")
    public String sendMessage() {
        this.service.sendMessage("message-test");
        return "Sent " + sendCount.incrementAndGet();
    }

    @GetMapping("/receiveMessages")
    public void receiveMessages() {
        this.service.receiveMessage();
    }
}
