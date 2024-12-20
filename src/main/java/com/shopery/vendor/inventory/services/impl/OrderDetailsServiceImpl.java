package com.shopery.vendor.inventory.services.impl;

import com.shopery.vendor.inventory.dto.OrderDetailsDto;
import com.shopery.vendor.inventory.models.OrderDetails;
import com.shopery.vendor.inventory.models.Orders;
import com.shopery.vendor.inventory.models.Products;
import com.shopery.vendor.inventory.repositories.OrderDetailsRepository;
import com.shopery.vendor.inventory.repositories.OrdersRepository;
import com.shopery.vendor.inventory.repositories.ProductsRepository;
import com.shopery.vendor.inventory.services.OrderDetailsService;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
@Service
public class OrderDetailsServiceImpl implements OrderDetailsService {

     private final OrdersRepository ordersRepository;
     private final OrderDetailsRepository orderDetailsRepository;
     private final ProductsRepository productsRepository;
     private final ModelMapper modelMapper;

     @Autowired
     public OrderDetailsServiceImpl(OrdersRepository ordersRepository,
                                    OrderDetailsRepository orderDetailsRepository,
                                    ProductsRepository productsRepository,
                                    ModelMapper modelMapper
     ){
         this.ordersRepository = ordersRepository;
         this.orderDetailsRepository = orderDetailsRepository;
         this.productsRepository = productsRepository;
         this.modelMapper = modelMapper;
     }


    @Override
    public OrderDetailsDto createOrderDetail(OrderDetailsDto orderDetailsDto) {
         Orders orders = ordersRepository.findById(orderDetailsDto.getOrderId())
                 .orElseThrow(()->new EntityNotFoundException("Order Not Found"));
        Products products = productsRepository.findById(orderDetailsDto.getProductId())
                .orElseThrow(()->new EntityNotFoundException("Product not Found"));
        if (products.getStock() < orderDetailsDto.getQuantity()) {
            throw new EntityNotFoundException("Insufficient stock for product with ID: " + orderDetailsDto.getProductId());
        }
        // Create and populate OrderDetails entity
        OrderDetails orderDetails = modelMapper.map(orderDetailsDto, OrderDetails.class);
        orderDetails.setPrice(products.getPrice());
        orderDetails.setQuantity(orderDetailsDto.getQuantity());
        orderDetails.setOrder(orders);

        // Calculate subtotal
        BigDecimal subTotal = products.getPrice().multiply(BigDecimal.valueOf(orderDetailsDto.getQuantity()));

        // Update product stock
        products.setStock(products.getStock() - orderDetails.getQuantity()); // Use orderDetails.getQuantity() for clarity

        // Update order total amount
        orders.setTotalAmount(orders.getTotalAmount().add(subTotal)); // Add subtotal to existing total
        productsRepository.save(products);
        ordersRepository.save(orders);



        OrderDetails savedOrderDetails = orderDetailsRepository.save(orderDetails);
        //savedOrderDetails.setOrder(orders);
        return modelMapper.map(savedOrderDetails, OrderDetailsDto.class);
    }
}
