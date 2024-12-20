package com.shopery.vendor.inventory.dto;

import com.shopery.vendor.inventory.models.Orders;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ShopDto {

    private UUID shopId;
    private String shopName;
    private String shopAddress;
    private String shopLogo;
    private String shopSpecialisation;
    private UUID vendorId;
    private Double latitude;
    private Double longitude;
    private Orders orders;
}
