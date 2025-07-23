package com.drkapps.saga_orchestrator.domain.ports;

public interface EventPublisherPort {
    void publish(String topic, String message);
}
