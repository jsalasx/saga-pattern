package com.drkapps.ms_inventory.infrastructure.repository;


import com.drkapps.ms_inventory.domain.model.Product;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface MongoProductRepository extends ReactiveMongoRepository<Product, String> {

    Mono<Product> findByNameIgnoreCase(String productName);
}