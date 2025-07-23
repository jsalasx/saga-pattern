package com.drkapps.ms_orders.infrastructure.controllers;


import com.drkapps.ms_orders.application.OrderService;
import com.drkapps.ms_orders.domain.model.Order;
import com.drkapps.ms_orders.infrastructure.dto.OrderRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public Mono<ResponseEntity<Order>> createOrder(@RequestBody OrderRequestDto orderRequestDto) {
        log.info("Creating order for productId: {}, quantity: {}", orderRequestDto.getProductId(), orderRequestDto.getQuantity());
        return orderService.createOrder(orderRequestDto.getProductId(), orderRequestDto.getQuantity())
                .map(ResponseEntity::ok);
    }

    @GetMapping()
    public Mono<ResponseEntity<String>> health() {
        return Mono.just(ResponseEntity.ok("Order Service is running"));
    }
}
