package com.drkapps.ms_orders.infrastructure.messaging;

import com.drkapps.ms_orders.application.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaOrderListener {

    private final OrderService orderService;

    @KafkaListener(topics = "order-cancelled", groupId = "orders-group")
    public void onOrderCancelled(String message) {
        orderService.orderCancel(message).subscribe();
    }


    @KafkaListener(topics = "order-completed", groupId = "orders-group")
    public void onOrderCompleted(String message) {
        orderService.orderComplete(message).subscribe();
    }
}
