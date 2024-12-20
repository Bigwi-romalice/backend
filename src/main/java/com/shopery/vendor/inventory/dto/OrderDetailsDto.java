package com.shopery.vendor.inventory.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class OrderDetailsDto {

    private UUID orderId;
    private UUID productId;
    private int quantity;

}
