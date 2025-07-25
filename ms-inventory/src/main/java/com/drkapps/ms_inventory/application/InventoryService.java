package com.drkapps.ms_inventory.application;

import com.drkapps.ms_inventory.domain.model.Product;
import com.drkapps.ms_inventory.domain.ports.EventPublisherPort;
import com.drkapps.ms_inventory.domain.ports.ProductRepositoryPort;
import com.drkapps.saga_shared.infrastructure.OrderSharedDto;
import com.drkapps.saga_shared.infrastructure.ProductSharedDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final ProductRepositoryPort productRepository;
    private final EventPublisherPort eventPublisher;

    public Mono<Void> handleOrder(OrderSharedDto orderSharedDto, String message) {
        List<Product> updatedProducts = new ArrayList<>();

        return Flux.fromIterable(orderSharedDto.getProductsList())
                .concatMap(product ->
                        productRepository.findById(product.getProductId())
                                .switchIfEmpty(Mono.error(new RuntimeException("Product not found: " + product.getProductId())))
                                .flatMap(existingProduct -> {
                                    if (existingProduct.getStock() >= product.getQuantity()) {
                                        existingProduct.setStock(existingProduct.getStock() - product.getQuantity());
                                        return productRepository.save(existingProduct)
                                                .doOnSuccess(updatedProducts::add); // Guardamos para posible rollback
                                    } else {
                                        return Mono.error(new RuntimeException("Insufficient stock for product: " + product.getProductId()));
                                    }
                                })
                )
                .then(Mono.fromRunnable(() -> {
                    // Solo se ejecuta si todos los productos se reservaron bien
                    eventPublisher.publish("inventory-reserved", message);
                }))
                .onErrorResume(ex -> {
                    // Hacer rollback de los productos que sí se modificaron antes del error
                    return Flux.fromIterable(updatedProducts)
                            .flatMap(product -> {
                                product.setStock(product.getStock() +
                                        orderSharedDto.getProductsList()
                                                .stream()
                                                .filter(p -> p.getProductId().equals(product.getId()))
                                                .findFirst()
                                                .map(ProductSharedDto::getQuantity)
                                                .orElse(0)
                                );
                                return productRepository.save(product);
                            })
                            .then(Mono.fromRunnable(() -> {
                                // Emitir evento de fallo después del rollback
                                eventPublisher.publish("inventory-failed", orderSharedDto.getOrderId());
                            }));
                })
                .then();
    }

    public Mono<Void> handleBillingError(OrderSharedDto orderSharedDto, String message) {
        return Flux.fromIterable(orderSharedDto.getProductsList())
                .flatMap(product ->
                        productRepository.findById(product.getProductId())
                                .switchIfEmpty(Mono.error(new RuntimeException("Product not found: " + product.getProductId())))
                                .flatMap(existingProduct -> {
                                    existingProduct.setStock(existingProduct.getStock() + product.getQuantity());
                                    return productRepository.save(existingProduct);
                                })
                ).then();
    }

    public Flux<Product> getProducts() {
        return productRepository.getAll();
    }

    public Mono<Product> updateStock(String productId, int newStock) {
        return productRepository.findById(productId).flatMap(product ->
                {
                    product.setStock(newStock);
                    return productRepository.save(product);
                }
        );

    }

    public Mono<Product> saveProduct(Product product) throws RuntimeException {
        return productRepository.findByName(product.getName().toLowerCase())
                .flatMap(existing -> Mono.<Product>error(new RuntimeException("Product already exists: " + product.getName())))
                .switchIfEmpty(productRepository.save(product));
    }
}