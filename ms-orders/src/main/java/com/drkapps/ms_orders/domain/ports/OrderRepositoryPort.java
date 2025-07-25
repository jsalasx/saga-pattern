package com.drkapps.ms_orders.domain.ports;

import com.drkapps.ms_orders.domain.model.Order;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface OrderRepositoryPort {
    Mono<Order> save(Order order);
    Mono<Order> findById(String orderId);
    Flux<Order> findAll();
}
