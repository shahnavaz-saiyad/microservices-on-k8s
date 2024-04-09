package com.common.util;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaUtility {

    private final KafkaTemplate<String, Object> kafkaTemplate;


    public <T> void sendMessage(T messagePayload, String topic, String tenantUuid){
        Message<T> message = MessageBuilder
                .withPayload(messagePayload)
                .setHeader(KafkaHeaders.TOPIC, topic)
                .setHeader("tenantUuid", tenantUuid)
                .build();
        kafkaTemplate.send(message);
    }
}
