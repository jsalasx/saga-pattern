package com.drkapps.ms_orders.infrastructure.dto;

import lombok.Data;

@Data
public class OrderRequestDto {

    private String productId;
    private int quantity;
}
