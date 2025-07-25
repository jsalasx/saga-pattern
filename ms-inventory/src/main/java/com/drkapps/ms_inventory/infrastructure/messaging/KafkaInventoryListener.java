package com.drkapps.ms_inventory.infrastructure.messaging;

import com.drkapps.ms_inventory.application.InventoryService;
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
public class KafkaInventoryListener {

    private final InventoryService inventoryService;
    private final ObjectMapper mapper;

    @KafkaListener(topics = "order-created", groupId = "inventory-group")
    public void onOrderCreated(ConsumerRecord<String, String> record, Acknowledgment ack) throws JsonProcessingException {
        log.warn("Payload order-created: {}", record.value());
        OrderSharedDto orderSharedDto = mapper.readValue(record.value(), OrderSharedDto.class);
        try {
            inventoryService.handleOrder(orderSharedDto, record.value()).block(); // espera resultado
            ack.acknowledge(); // confirma solo si todo salió bien
        } catch (Exception e) {
            throw new RuntimeException("Error procesando mensaje: " + record.value(), e);
        }
    }

    @KafkaListener(topics = "inventory-rollback", groupId = "inventory-group")
    public void onBillingFail(ConsumerRecord<String, String> record, Acknowledgment ack) throws JsonProcessingException {
        log.warn("Payload inventory-rollback: {}", record.value());
        OrderSharedDto orderSharedDto = mapper.readValue(record.value(), OrderSharedDto.class);
        try {
            inventoryService.handleBillingError(orderSharedDto, record.value()).block();
            ack.acknowledge();
        } catch (Exception e) {
            throw new RuntimeException("Error procesando mensaje: " + record.value(), e);
        }
    }
}
