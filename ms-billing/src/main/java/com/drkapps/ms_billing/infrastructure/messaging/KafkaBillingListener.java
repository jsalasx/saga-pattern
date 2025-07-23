package com.drkapps.ms_billing.infrastructure.messaging;

import com.drkapps.ms_billing.application.BillingService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaBillingListener {

    private final BillingService billingService;

    @KafkaListener(topics = "inventory-reserved", groupId = "billing-group")
    public void onInventoryReserved(String message) {
        // message format: "orderId:amount"
        String[] parts = message.split(":");
        String orderId = parts[0];
        double amount = Double.parseDouble(parts[1]);

        billingService.generateInvoice(orderId, amount).subscribe();
    }
}
