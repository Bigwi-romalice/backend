package com.shopery.vendor.inventory.controllers;

import com.shopery.vendor.inventory.dto.OrderDetailsDto;
import com.shopery.vendor.inventory.services.OrderDetailsService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order-details")
public class OrderDetailsController {

    @Autowired
    private OrderDetailsService orderDetailsService;

    // Create a new order detail
    @Operation(summary = "Place Order")
    @PostMapping
    public ResponseEntity<OrderDetailsDto> createOrderDetail(@RequestBody OrderDetailsDto orderDetailsDto) {
        OrderDetailsDto createdOrderDetail = orderDetailsService.createOrderDetail(orderDetailsDto);
        return new ResponseEntity<>(createdOrderDetail, HttpStatus.CREATED);
    }




}