package com.drkapps.ms_orders.domain.model;


import com.drkapps.saga_shared.infrastructure.ProductSharedDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "orders")
public class Order {
    @Id
    private String id;
    private List<ProductSharedDto> productList;
    private String status; // PENDING, CONFIRMED, CANCELLED
    private double total;
    private LocalDateTime createdAt;


    public void calculateTotal() {
        this.total = productList.stream()
                .mapToDouble(product -> product.getPrice() * product.getQuantity())
                .sum();
    }
}
