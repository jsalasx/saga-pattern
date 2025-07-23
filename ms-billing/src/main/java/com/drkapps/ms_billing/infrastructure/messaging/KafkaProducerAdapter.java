package com.drkapps.ms_billing.infrastructure.messaging;

import com.drkapps.ms_billing.domain.ports.EventPublisherPort;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaProducerAdapter implements EventPublisherPort {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public void publish(String topic, String message) {
        kafkaTemplate.send(topic, message);
    }
}
