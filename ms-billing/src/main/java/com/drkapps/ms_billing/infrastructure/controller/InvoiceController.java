package com.drkapps.ms_billing.infrastructure.controller;

import com.drkapps.ms_billing.domain.model.Invoice;
import com.drkapps.ms_billing.infrastructure.repository.MongoInvoiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/invoices")
@RequiredArgsConstructor
public class InvoiceController {

    private final MongoInvoiceRepository mongoInvoiceRepository;

    @GetMapping
    public Flux<Invoice> getAll() {
        return mongoInvoiceRepository.findAll();
    }
}
