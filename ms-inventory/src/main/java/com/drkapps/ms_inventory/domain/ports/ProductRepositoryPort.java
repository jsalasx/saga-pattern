package com.drkapps.ms_inventory.domain.ports;

import com.drkapps.ms_inventory.domain.model.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

public interface ProductRepositoryPort {
    Mono<Product> findById(String id);
    Mono<Product> save(Product product);
    Mono<Long> count();
    Flux<Product> getAll();
    Mono<Product> findByName(String productName);
}