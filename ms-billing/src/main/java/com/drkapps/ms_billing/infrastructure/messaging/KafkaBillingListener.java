package com.drkapps.ms_billing.infrastructure.messaging;

import com.drkapps.ms_billing.application.BillingService;
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
public class KafkaBillingListener {

    private final BillingService billingService;
    private final ObjectMapper mapper;

    @KafkaListener(topics = "inventory-reserved", groupId = "billing-group")
    public void onInventoryReserved(ConsumerRecord<String, String> record, Acknowledgment ack) throws JsonProcessingException {
        // message format: "orderId:amount"
        log.warn("Payload inventory-reserved: {}", record.value());
        try {
            OrderSharedDto orderSharedDto = mapper.readValue(record.value(), OrderSharedDto.class);
            billingService.generateInvoice(orderSharedDto, record.value()).block();
            ack.acknowledge();
        } catch (Exception e) {
            throw new RuntimeException("Error procesado billing message " + record.value(), e);
        }
    }
}
