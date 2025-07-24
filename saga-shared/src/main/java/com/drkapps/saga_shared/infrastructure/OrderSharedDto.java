package com.drkapps.saga_shared.infrastructure;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderSharedDto {

    private String orderId;
    private String customerName;
    private List<ProductSharedDto> productsList;
    private Double total;


    public void calculateTotal() {
        if (productsList != null && !productsList.isEmpty()) {
            total = productsList.stream()
                    .mapToDouble(product -> product.getPrice() * product.getQuantity())
                    .sum();
        } else {
            total = 0.0;
        }

    }

}
