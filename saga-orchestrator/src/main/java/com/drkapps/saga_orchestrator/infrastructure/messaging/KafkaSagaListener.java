package com.drkapps.saga_orchestrator.infrastructure.messaging;

import com.drkapps.saga_orchestrator.application.SagaCoordinatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaSagaListener {

    private final SagaCoordinatorService sagaCoordinator;

    @KafkaListener(topics = "inventory-reserved", groupId = "saga-group")
    public void onInventoryReserved(String message) {
        sagaCoordinator.handleInventoryReserved(message);
    }

    @KafkaListener(topics = "inventory-failed", groupId = "saga-group")
    public void onInventoryFailed(String orderId) {
        sagaCoordinator.handleInventoryFailed(orderId);
    }

    @KafkaListener(topics = "billing-failed", groupId = "saga-group")
    public void onBillingFailed(String orderId) {
        sagaCoordinator.handleBillingFailed(orderId);
    }

    @KafkaListener(topics = "invoice-created", groupId = "saga-group")
    public void onInvoiceCreated(String message) {
        sagaCoordinator.handleInvoiceCreated(message);
    }
}
