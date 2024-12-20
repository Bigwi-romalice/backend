package com.shopery.vendor.inventory.controllers;

import com.shopery.vendor.inventory.dto.OrdersDto;
import com.shopery.vendor.inventory.models.Orders;
import com.shopery.vendor.inventory.services.OrdersService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
public class OrdersController {


    private final OrdersService ordersService;

    @Autowired
    public  OrdersController(OrdersService ordersService){
        this.ordersService = ordersService;
    }

    @Operation(summary = "Create Order")
    @PostMapping
    public ResponseEntity<OrdersDto> createOrder(OrdersDto ordersDto){
        return   ResponseEntity.ok(ordersService.createOrder(ordersDto));
    }

    @Operation(summary = "Get order By customer and Shop")
    @GetMapping("/customer/{customerId}/shop/{shopId}")
    public ResponseEntity<List<OrdersDto>> getOrdersByCustomerAndShop(
            @PathVariable UUID customerId,
            @PathVariable UUID shopId) {
        List<OrdersDto> orders = ordersService.getCustomerOrdersByShop(customerId, shopId);
        return ResponseEntity.ok(orders);
    }
    @Operation(summary = "Get order by shop")
    @GetMapping("/shop/{shopId}")
    public ResponseEntity<List<OrdersDto>> getOrderByShop(@PathVariable UUID shopId){
        List<OrdersDto> orders = ordersService.getOrderByShop(shopId);
        return  ResponseEntity.ok(orders);
    }

    @Operation(summary ="Get All Orders")
    @GetMapping
    public ResponseEntity<List<OrdersDto>> getAllOrders(){
        List<OrdersDto> orders = ordersService.getAllOrders();
        return  ResponseEntity.ok(orders);
    }
}
