package com.drkapps.ms_inventory.infrastructure.messaging;

import com.drkapps.ms_inventory.application.InventoryService;
import com.drkapps.saga_shared.infrastructure.OrderSharedDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaInventoryListener {

    private final InventoryService inventoryService;
    private final ObjectMapper mapper;

    @KafkaListener(topics = "order-created", groupId = "inventory-group")
    public void onOrderCreated(String message) throws JsonProcessingException {
        log.warn("Payload order-created: {}", message);
        OrderSharedDto orderSharedDto = mapper.readValue(message, OrderSharedDto.class);
        inventoryService.handleOrder(orderSharedDto, message).subscribe();
    }

    @KafkaListener(topics = "inventory-rollback", groupId = "inventory-group")
    public void onBillingFail(String message) throws JsonProcessingException {
        log.warn("Payload inventory-rollback: {}", message);
        OrderSharedDto orderSharedDto = mapper.readValue(message, OrderSharedDto.class);
        inventoryService.handleBillingError(orderSharedDto, message).subscribe();
    }
}
