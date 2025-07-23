package com.drkapps.ms_inventory.infrastructure.messaging;

import com.drkapps.inventory.application.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaInventoryListener {

    private final InventoryService inventoryService;

    @KafkaListener(topics = "order-created", groupId = "inventory-group")
    public void onOrderCreated(String message) {
        String[] parts = message.split(":"); // formato "productId:quantity"
        String productId = parts[0];
        int quantity = Integer.parseInt(parts[1]);

        inventoryService.handleOrder(productId, quantity).subscribe();
    }
}
