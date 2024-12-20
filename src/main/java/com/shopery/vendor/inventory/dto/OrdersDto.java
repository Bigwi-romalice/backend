package com.shopery.vendor.inventory.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class OrdersDto {

    private UUID orderId;
    private UUID customerId;
    private String status; // PENDING, COMPLETED, CANCELLED
    private BigDecimal totalAmount;
    private UUID shopId; // Shop ID

}
