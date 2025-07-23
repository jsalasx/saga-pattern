package com.drkapps.ms_orders.domain.ports;

public interface EventPublisherPort {
    void publish(String topic, String message);
}
