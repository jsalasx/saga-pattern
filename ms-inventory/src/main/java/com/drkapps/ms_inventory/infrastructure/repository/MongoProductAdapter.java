package com.drkapps.ms_inventory.infrastructure.repository;

import com.drkapps.ms_inventory.domain.model.Product;
import com.drkapps.ms_inventory.domain.ports.ProductRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class MongoProductAdapter implements ProductRepositoryPort {

    private final MongoProductRepository mongoRepo;

    @Override
    public Mono<Product> findById(String id) {
        return mongoRepo.findById(id);
    }

    @Override
    public Mono<Product> save(Product product) {
        return mongoRepo.save(product);
    }

    @Override
    public Mono<Long> count() {
        return mongoRepo.count();
    }
}