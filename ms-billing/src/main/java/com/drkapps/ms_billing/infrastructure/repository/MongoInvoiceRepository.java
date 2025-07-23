package com.drkapps.ms_billing.infrastructure.repository;

import com.drkapps.ms_billing.domain.model.Invoice;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface MongoInvoiceRepository extends ReactiveMongoRepository<Invoice, String> {
}
