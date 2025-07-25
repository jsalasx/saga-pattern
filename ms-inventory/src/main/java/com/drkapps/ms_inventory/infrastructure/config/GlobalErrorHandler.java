package com.drkapps.ms_inventory.infrastructure.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Slf4j
@Configuration
@Order(-2) // para que tenga prioridad sobre los manejadores por defecto
public class GlobalErrorHandler implements ErrorWebExceptionHandler {

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        // Log completo con stack trace
        log.error("Unhandled error for request [{} {}]:",
                exchange.getRequest().getMethod(),
                exchange.getRequest().getPath(),
                ex);

        // Determinar status HTTP según el tipo de excepción
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        if (ex instanceof org.springframework.web.server.UnsupportedMediaTypeStatusException) {
            status = HttpStatus.UNSUPPORTED_MEDIA_TYPE; // 415
        } else if (ex instanceof org.springframework.web.server.ResponseStatusException) {
            org.springframework.web.server.ResponseStatusException rse =
                    (org.springframework.web.server.ResponseStatusException) ex;
            status = (HttpStatus) rse.getStatusCode();
        }

        // Crear un JSON de error
        String body = String.format(
                "{\"error\": \"%s\", \"message\": \"%s\"}",
                status.getReasonPhrase(),
                ex.getMessage() != null ? ex.getMessage() : "Unexpected error"
        );

        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);

        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
        return exchange.getResponse().writeWith(Mono.just(buffer));
    }
}