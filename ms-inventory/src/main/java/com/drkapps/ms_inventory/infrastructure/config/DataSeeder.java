package com.drkapps.ms_inventory.infrastructure.config;


import com.drkapps.inventory.domain.model.Product;
import com.drkapps.inventory.domain.ports.ProductRepositoryPort;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
@RequiredArgsConstructor
public class DataSeeder {

    private final ProductRepositoryPort productRepository;

    @PostConstruct
    public void seedProducts() {
        productRepository.count()
                .filter(count -> count == 0)
                .flatMapMany(ignore -> Flux.just(
                        Product.builder().name("Laptop").stock(10).price(1200).build(),
                        Product.builder().name("Phone").stock(20).price(800).build(),
                        Product.builder().name("Headphones").stock(15).price(150).build()
                ))
                .flatMap(productRepository::save)
                .doOnNext(p -> System.out.println("Producto inicial creado: " + p.getName()))
                .subscribe();
    }
}
