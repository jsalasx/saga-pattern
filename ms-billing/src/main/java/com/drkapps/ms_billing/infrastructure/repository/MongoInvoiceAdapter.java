package com.drkapps.ms_billing.infrastructure.repository;

import com.drkapps.ms_billing.domain.model.Invoice;
import com.drkapps.ms_billing.domain.ports.InvoiceRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class MongoInvoiceAdapter implements InvoiceRepositoryPort {

    private final MongoInvoiceRepository mongoRepo;

    @Override
    public Mono<Invoice> save(Invoice invoice) {
        return mongoRepo.save(invoice);
    }
}
