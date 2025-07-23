package com.drkapps.ms_billing.application;

import com.drkapps.ms_billing.domain.model.Invoice;
import com.drkapps.ms_billing.domain.ports.EventPublisherPort;
import com.drkapps.ms_billing.domain.ports.InvoiceRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class BillingService {

    private final InvoiceRepositoryPort invoiceRepository;
    private final EventPublisherPort eventPublisher;

    public Mono<Invoice> generateInvoice(String orderId, double amount) {
        // Simulamos una probabilidad de fallo
        boolean fail = new Random().nextDouble() < 0.2;

        Invoice invoice = Invoice.builder()
                .orderId(orderId)
                .amount(amount)
                .createdAt(Instant.now())
                .status(fail ? "FAILED" : "GENERATED")
                .build();

        return invoiceRepository.save(invoice)
                .doOnSuccess(saved -> {
                    if (fail) {
                        eventPublisher.publish("billing-failed", orderId);
                    } else {
                        eventPublisher.publish("invoice-created", saved.getId() + ":" + orderId);
                    }
                });
    }
}
