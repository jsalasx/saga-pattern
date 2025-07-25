package com.drkapps.ms_inventory.infrastructure.controlelrs;


import com.drkapps.ms_inventory.application.InventoryService;
import com.drkapps.ms_inventory.domain.model.Product;
import com.drkapps.saga_shared.infrastructure.OrderSharedDto;
import com.fasterxml.jackson.core.JsonProcessingException;
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

    @GetMapping("/get-products")
    public Flux<ResponseEntity<Product>> getProducts(@RequestBody OrderSharedDto orderSharedDto) throws JsonProcessingException {
        log.info("Create order {}", orderSharedDto.getCustomerName());
        return inventoryService.getProducts()
                .map(ResponseEntity::ok);
    }

    @GetMapping()
    public Mono<ResponseEntity<String>> health() {
        return Mono.just(ResponseEntity.ok("Inventory service is running"));
    }
}
