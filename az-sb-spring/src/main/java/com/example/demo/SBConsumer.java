package com.example.demo;

import com.azure.core.amqp.AmqpRetryMode;
import com.azure.core.amqp.AmqpRetryOptions;
import com.azure.core.util.logging.ClientLogger;
import com.azure.messaging.servicebus.ServiceBusClientBuilder;
import com.azure.messaging.servicebus.ServiceBusReceivedMessage;
import com.azure.messaging.servicebus.ServiceBusReceiverAsyncClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.time.Duration;

@Component
public class SBConsumer {
    @Autowired
    private SBConfiguration configuration;

    private ServiceBusReceiverAsyncClient receiverClient;

    private final ClientLogger logger = new ClientLogger(SBConsumer.class);

    @PostConstruct
    public void init() {
        AmqpRetryOptions retryOptions = new AmqpRetryOptions()
            .setMaxRetries(20)
            .setDelay(Duration.ofMillis(25000))
            .setMaxDelay(Duration.ofMillis(25000))
            .setMode(AmqpRetryMode.FIXED)
            .setTryTimeout(Duration.ofMillis(10000));

        this.receiverClient = new ServiceBusClientBuilder()
                .connectionString(this.configuration.getConnectionString())
                .retryOptions(retryOptions)
                .receiver()
                .disableAutoComplete()
                .queueName(this.configuration.getQueueName())
                .buildAsyncClient();
    }

    public void receive() {
        this.receiverClient.receiveMessages()
                .flatMap(message -> {
                    this.logger.info("Got Message:" + message.getMessageId());
                    if (shouldComplete(message)) {
                        return this.receiverClient.complete(message);
                    } else {
                        return this.receiverClient.abandon(message);
                    }
                })
                .onErrorResume(e -> {
                    this.logger.logThrowableAsError(e);
                    return Mono.empty();
                })
                .subscribe();
    }

    private boolean shouldComplete(ServiceBusReceivedMessage message) {
        // Decide randomly whether to complete or not.
        return Math.random() < 0.5;
    }
}
