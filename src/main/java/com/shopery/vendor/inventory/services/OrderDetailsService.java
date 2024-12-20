package com.shopery.vendor.inventory.services;

import com.shopery.vendor.inventory.dto.OrderDetailsDto;
import com.shopery.vendor.inventory.models.OrderDetails;

public interface OrderDetailsService {
    OrderDetailsDto createOrderDetail(OrderDetailsDto orderDetailsDto);
}
