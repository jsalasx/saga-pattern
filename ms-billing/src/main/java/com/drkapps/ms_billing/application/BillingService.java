package com.drkapps.ms_billing.application;

import com.drkapps.ms_billing.domain.model.Invoice;
import com.drkapps.ms_billing.domain.ports.EventPublisherPort;
import com.drkapps.ms_billing.domain.ports.InvoiceRepositoryPort;
import com.drkapps.saga_shared.infrastructure.OrderSharedDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class BillingService {

    private final InvoiceRepositoryPort invoiceRepository;
    private final EventPublisherPort eventPublisher;

    public Mono<Invoice> generateInvoice(OrderSharedDto orderSharedDto, String message) {
        // Simulamos una probabilidad de fallo
        //boolean fail = new Random().nextDouble() < 0.2;
        try {
            Invoice invoice = Invoice.builder()
                    .orderId(orderSharedDto.getOrderId())
                    .amount(orderSharedDto.getTotal())
                    .createdAt(Instant.now())
                    //.status(fail ? "FAILED" : "GENERATED")
                    .status("GENERATED") // For simplicity, always generated
                    .build();

            return invoiceRepository.save(invoice)
                    .doOnSuccess(saved -> {
                        //if (fail) {
                        //    eventPublisher.publish("billing-failed", orderId);
                        //} else {
                        eventPublisher.publish("invoice-created", orderSharedDto.getOrderId());
                        //}
                    });
        } catch (Exception e) {
            // En caso de error, publicamos un evento de fallo
            eventPublisher.publish("billing-failed", message);
            return null;
            //return Mono.error(new RuntimeException("Error generating invoice for order " + orderId, e));
        }
    }
}
