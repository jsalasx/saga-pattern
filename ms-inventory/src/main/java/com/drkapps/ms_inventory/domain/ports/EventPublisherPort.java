package com.drkapps.ms_inventory.domain.ports;

public interface EventPublisherPort {
    void publish(String topic, String message);
}