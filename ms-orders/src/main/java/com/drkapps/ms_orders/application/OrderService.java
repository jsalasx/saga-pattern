package com.drkapps.ms_orders.application;

import com.drkapps.ms_orders.domain.model.Order;
import com.drkapps.ms_orders.domain.ports.EventPublisherPort;
import com.drkapps.ms_orders.domain.ports.OrderRepositoryPort;
import com.drkapps.saga_shared.infrastructure.OrderSharedDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepositoryPort orderRepository;
    private final EventPublisherPort eventPublisher;
    private final ObjectMapper mapper;

    public Mono<Order> createOrder(OrderSharedDto orderSharedDto) throws JsonProcessingException {
        Order order = Order.builder()
                .productList(orderSharedDto.getProductsList())
                .status("PENDING")
                .createdAt(LocalDateTime.now())
                .build();

        order.calculateTotal();



        return orderRepository.save(order)
                .doOnSuccess(saved -> {
                    String payload = null;
                    try {
                        orderSharedDto.calculateTotal();
                        orderSharedDto.setOrderId(saved.getId());
                        payload = mapper.writeValueAsString(orderSharedDto);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                    log.warn("payload {}", payload);
                    eventPublisher.publish("order-created", payload);
                });
    }

    public Mono<Order> orderCancel(String orderId) {
        return orderRepository.findById(orderId)
                .flatMap(order -> {
                    order.setStatus("CANCELLED");
                    return orderRepository.save(order);
                })
                .switchIfEmpty(Mono.error(new RuntimeException("Order not found")));
    }

    public Mono<Void> orderComplete(String orderId) {
        return orderRepository.findById(orderId)
                .switchIfEmpty(Mono.error(new RuntimeException("Order not found")))
                .flatMap(order -> {
                    order.setStatus("COMPLETED");
                    return orderRepository.save(order);
                })
                .then();
    }
}
