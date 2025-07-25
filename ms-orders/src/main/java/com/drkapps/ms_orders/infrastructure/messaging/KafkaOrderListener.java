package com.drkapps.ms_orders.infrastructure.messaging;

import com.drkapps.ms_orders.application.OrderService;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@EnableKafka
@Component
@RequiredArgsConstructor
public class KafkaOrderListener {

    private final OrderService orderService;

    @KafkaListener(topics = "order-cancelled", groupId = "orders-group")
    public void onOrderCancelled(ConsumerRecord<String, String> record, Acknowledgment ack) {
        try {
            orderService.orderCancel(record.value()).block(); // Esperamos la operación reactiva
            ack.acknowledge(); // Confirmamos solo si no hubo error
        } catch (Exception e) {
            throw new RuntimeException("Error procesando mensaje: " + record.value(), e);
        }
    }


    @KafkaListener(topics = "order-completed", groupId = "orders-group")
    public void onOrderCompleted(ConsumerRecord<String, String> record, Acknowledgment ack) {
        try {
            orderService.orderComplete(record.value()).block();
            ack.acknowledge();
        } catch (Exception e) {
            throw new RuntimeException("Error procesando mensaje: " + record.value(), e);
        }
    }
}
