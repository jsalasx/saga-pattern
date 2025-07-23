package com.drkapps.ms_orders.application;

import com.drkapps.ms_orders.domain.model.Order;
import com.drkapps.ms_orders.domain.ports.EventPublisherPort;
import com.drkapps.ms_orders.domain.ports.OrderRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
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
                    eventPublisher.publish("order-created", payload);
                });
    }
}
