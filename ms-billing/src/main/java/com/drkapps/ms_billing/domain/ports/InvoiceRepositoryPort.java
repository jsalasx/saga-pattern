package com.drkapps.ms_billing.domain.ports;

import com.drkapps.ms_billing.domain.model.Invoice;
import reactor.core.publisher.Mono;

public interface InvoiceRepositoryPort {
    Mono<Invoice> save(Invoice invoice);
}
