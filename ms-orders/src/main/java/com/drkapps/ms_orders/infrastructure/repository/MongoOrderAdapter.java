package com.drkapps.ms_orders.infrastructure.repository;

import com.drkapps.ms_orders.domain.model.Order;
import com.drkapps.ms_orders.domain.ports.OrderRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class MongoOrderAdapter implements OrderRepositoryPort {

    private final MongoOrderRepository mongoRepo;

    @Override
    public Mono<Order> save(Order order) {
        return mongoRepo.save(order);
    }

    @Override
    public Mono<Order> findById(String orderId) {
        return mongoRepo.findById(orderId);
    }

    @Override
    public Flux<Order> findAll() {
        return mongoRepo.findAll();
    }
}
