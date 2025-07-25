package com.drkapps.saga_orchestrator.infrastructure.messaging;

import com.drkapps.saga_orchestrator.application.SagaCoordinatorService;
import com.drkapps.saga_shared.infrastructure.OrderSharedDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaSagaListener {

    private final SagaCoordinatorService sagaCoordinator;
    private final ObjectMapper mapper;

//    @KafkaListener(topics = "inventory-reserved", groupId = "saga-group")
//    public void onInventoryReserved(String message) {
//        sagaCoordinator.handleInventoryReserved(message);
//    }

    @KafkaListener(topics = "inventory-failed", groupId = "saga-group")
    public void onInventoryFailed(ConsumerRecord<String, String> record, Acknowledgment ack) {
        try {
            log.warn("Payload inventory-rollback: {}", record.value());
            sagaCoordinator.handleInventoryFailed(record.value());
        } catch (Exception e) {
            log.error("Error while handling inventory-failed", e);
            throw new RuntimeException("Error al ejecutar Inventory failed:" + record.value(), e);
        }
    }

    @KafkaListener(topics = "billing-failed", groupId = "saga-group")
    public void onBillingFailed(ConsumerRecord<String, String> record, Acknowledgment ack) throws JsonProcessingException {
        log.warn("Payload billing-failed: {}", record.value());
        OrderSharedDto orderSharedDto = mapper.readValue(record.value(), OrderSharedDto.class);
        try {
            sagaCoordinator.handleBillingFailed(record.value(), orderSharedDto.getOrderId());
            ack.acknowledge();
        } catch (Exception e) {
            log.error("Error while handling billing-failed", e);
            throw new RuntimeException("Error al ejecutar BillingFailed:" + record.value(), e);
        }
    }

    @KafkaListener(topics = "invoice-created", groupId = "saga-group")
    public void onInvoiceCreated(ConsumerRecord<String, String> record, Acknowledgment ack) {
        log.warn("Payload invoice-created: {}", record.value());
        try {
            sagaCoordinator.handleInvoiceCreated(record.value());
            ack.acknowledge();
        } catch (Exception e) {
            log.error("Error while handling invoice-created", e);
            throw new RuntimeException("Error al ejecutar InvoiceCreated:" + record.value(), e);
        }

    }
}
