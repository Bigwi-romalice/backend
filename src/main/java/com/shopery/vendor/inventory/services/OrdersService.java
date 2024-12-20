package com.shopery.vendor.inventory.services;


import com.shopery.vendor.inventory.dto.OrdersDto;
import com.shopery.vendor.inventory.models.Orders;

import java.util.List;
import java.util.UUID;

public interface OrdersService {
    OrdersDto createOrder(OrdersDto ordersDto);
    public List<OrdersDto> getCustomerOrdersByShop(UUID customerId, UUID shopId);
    public List<OrdersDto> getOrderByShop(UUID shopId);
    public List<OrdersDto> getAllOrders();
}
