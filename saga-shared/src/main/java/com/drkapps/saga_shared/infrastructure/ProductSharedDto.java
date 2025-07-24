package com.drkapps.saga_shared.infrastructure;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductSharedDto {

    private String productId;
    private int quantity;
    private double price;
}
