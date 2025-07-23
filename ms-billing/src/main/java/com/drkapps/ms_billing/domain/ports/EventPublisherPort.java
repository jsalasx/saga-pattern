package com.drkapps.ms_billing.domain.ports;

public interface EventPublisherPort {
    void publish(String topic, String message);
}
