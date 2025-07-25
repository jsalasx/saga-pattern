package com.drkapps.ms_billing.infrastructure.controller;

import com.drkapps.ms_billing.domain.model.Invoice;
import com.drkapps.ms_billing.infrastructure.repository.MongoInvoiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("saga/api/v1/invoices")
@RequiredArgsConstructor
public class InvoiceController {

    private final MongoInvoiceRepository mongoInvoiceRepository;

    @GetMapping("/get-invoices")
    public Flux<Invoice> getAll() {
        return mongoInvoiceRepository.findAll();
    }

    @GetMapping()
    public Mono<ResponseEntity<String>> health() {
        return Mono.just(ResponseEntity.ok("Invoice service is running"));
    }
}
