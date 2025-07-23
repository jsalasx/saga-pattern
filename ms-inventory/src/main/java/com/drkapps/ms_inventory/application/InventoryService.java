package com.drkapps.ms_inventory.application;

import com.drkapps.ms_inventory.domain.ports.EventPublisherPort;
import com.drkapps.ms_inventory.domain.ports.ProductRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final ProductRepositoryPort productRepository;
    private final EventPublisherPort eventPublisher;

    public Mono<Void> handleOrder(String productId, int quantity) {
        return productRepository.findById(productId)
                .flatMap(product -> {
                    if (product.reserveStock(quantity)) {
                        return productRepository.save(product)
                                .doOnSuccess(p -> eventPublisher.publish("inventory-reserved", productId))
                                .then();
                    } else {
                        eventPublisher.publish("inventory-failed", productId);
                        return Mono.empty();
                    }
                })
                .switchIfEmpty(Mono.fromRunnable(() -> eventPublisher.publish("inventory-failed", productId)))
                .then();
    }
}