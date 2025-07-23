package com.drkapps.saga_orchestrator.application;

import com.drkapps.saga_orchestrator.domain.ports.EventPublisherPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SagaCoordinatorService {

    private final EventPublisherPort eventPublisher;

    /**
     * Maneja cuando el inventario es reservado.
     * Inicia el proceso de facturación.
     */
    public void handleInventoryReserved(String message) {
        // message: "orderId:amount"
        System.out.println("[SAGA] Inventario reservado, iniciando facturación: " + message);
        eventPublisher.publish("generate-invoice", message);
    }

    /**
     * Maneja cuando el inventario falla.
     * Cancela el pedido.
     */
    public void handleInventoryFailed(String orderId) {
        System.out.println("[SAGA] Inventario falló, cancelando orden: " + orderId);
        eventPublisher.publish("order-cancelled", orderId);
    }

    /**
     * Maneja cuando la facturación falla.
     * Revierte inventario y cancela orden.
     */
    public void handleBillingFailed(String orderId) {
        System.out.println("[SAGA] Facturación falló, rollback de inventario y cancelación de orden: " + orderId);
        eventPublisher.publish("inventory-rollback", orderId);
        eventPublisher.publish("order-cancelled", orderId);
    }

    /**
     * Maneja cuando la factura es creada.
     * Completa el pedido.
     */
    public void handleInvoiceCreated(String message) {
        // message: "invoiceId:orderId"
        String[] parts = message.split(":");
        String orderId = parts[1];
        System.out.println("[SAGA] Factura creada, orden completada: " + orderId);
        eventPublisher.publish("order-completed", orderId);
    }
}
