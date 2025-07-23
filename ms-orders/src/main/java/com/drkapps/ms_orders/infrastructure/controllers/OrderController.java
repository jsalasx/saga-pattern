package com.drkapps.ms_orders.infrastructure.controllers;


import com.drkapps.ms_orders.application.OrderService;
import com.drkapps.ms_orders.domain.model.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public Mono<ResponseEntity<Order>> createOrder(@RequestParam String productId, @RequestParam int quantity) {
        return orderService.createOrder(productId, quantity)
                .map(ResponseEntity::ok);
    }
}
