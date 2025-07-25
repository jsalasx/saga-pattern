package com.drkapps.ms_inventory.infrastructure.controllers;


import com.drkapps.ms_inventory.application.InventoryService;
import com.drkapps.ms_inventory.domain.model.Product;
import com.drkapps.ms_inventory.infrastructure.dto.UpdateStockDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("saga/api/v1/inventory")
@RequiredArgsConstructor
@Slf4j
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping("/products/get")
    public Flux<Product> getProducts() {
        return inventoryService.getProducts();
    }

    @PostMapping("/products/{productId}/stock")
    public Mono<Product> updateStock(@PathVariable("productId") String productId, @RequestBody UpdateStockDto updateStockDto) {
        return inventoryService.updateStock(productId, updateStockDto.getStock());
    }

    @PostMapping("/products/save")
    public Mono<Product> saveProduct(@RequestBody Product product) {
        return inventoryService.saveProduct(product);
    }

    @GetMapping()
    public Mono<ResponseEntity<String>> health() {
        return Mono.just(ResponseEntity.ok("Inventory service is running"));
    }
}
