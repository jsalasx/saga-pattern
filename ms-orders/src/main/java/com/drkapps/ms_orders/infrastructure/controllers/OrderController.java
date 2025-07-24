package com.drkapps.ms_orders.infrastructure.controllers;


import com.drkapps.ms_orders.application.OrderService;
import com.drkapps.ms_orders.domain.model.Order;
import com.drkapps.ms_orders.infrastructure.dto.OrderRequestDto;
import com.drkapps.saga_shared.infrastructure.OrderSharedDto;
import com.fasterxml.jackson.core.JsonProcessingException;
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
    public Mono<ResponseEntity<Order>> createOrder(@RequestBody OrderSharedDto orderSharedDto) throws JsonProcessingException {
        log.info("Create order {}", orderSharedDto.getCustomerName());
        return orderService.createOrder(orderSharedDto)
                .map(ResponseEntity::ok);
    }

    @GetMapping()
    public Mono<ResponseEntity<String>> health() {
        return Mono.just(ResponseEntity.ok("Order Service is running"));
    }
}
