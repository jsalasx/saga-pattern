package com.drkapps.ms_orders.infrastructure.repository;

import com.drkapps.ms_orders.domain.model.Order;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface MongoOrderRepository extends ReactiveMongoRepository<Order, String> {
}
