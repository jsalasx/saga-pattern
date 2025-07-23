package com.drkapps.ms_orders.application;

import com.drkapps.ms_orders.domain.model.Order;
import com.drkapps.ms_orders.domain.ports.EventPublisherPort;
import com.drkapps.ms_orders.domain.ports.OrderRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepositoryPort orderRepository;
    private final EventPublisherPort eventPublisher;

    public Mono<Order> createOrder(String productId, int quantity) {
        Order order = Order.builder()
                .productId(productId)
                .quantity(quantity)
                .status("PENDING")
                .build();

        return orderRepository.save(order)
                .doOnSuccess(saved -> {
                    String payload = saved.getProductId() + ":" + saved.getQuantity() + ":" + saved.getId();
                    log.warn("payload {}", payload);
                    eventPublisher.publish("order-created", payload);
                });
    }

    public Mono<Order> orderCancel(String orderId) {
        return orderRepository.findById(orderId)
                .flatMap(order -> {
                    order.setStatus("CANCELLED");
                    return orderRepository.save(order)
                            .doOnSuccess(saved -> {
                                String payload = saved.getProductId() + ":" + saved.getQuantity();
                                eventPublisher.publish("order-cancelled", payload);
                            });
                })
                .switchIfEmpty(Mono.error(new RuntimeException("Order not found")));
    }
}
