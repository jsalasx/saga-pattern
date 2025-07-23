package com.drkapps.ms_inventory.infrastructure.messaging;

import com.drkapps.ms_inventory.application.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaInventoryListener {

    private final InventoryService inventoryService;

    @KafkaListener(topics = "order-created", groupId = "inventory-group")
    public void onOrderCreated(String message) {
        String[] parts = message.split(":"); // formato "productId:quantity"
        String productId = parts[0];
        int quantity = Integer.parseInt(parts[1]);
        String orderId = parts[2]; // opcional, si necesitas el ID del pedido
        log.warn("Payload: {}", message);
        inventoryService.handleOrder(productId, quantity, orderId).subscribe();
    }
}
