package com.shopery.vendor.inventory.services.impl;

import com.shopery.vendor.inventory.dto.OrdersDto;
import com.shopery.vendor.inventory.models.AppUsers;
import com.shopery.vendor.inventory.models.OrderStatus;
import com.shopery.vendor.inventory.models.Orders;
import com.shopery.vendor.inventory.models.Shops;
import com.shopery.vendor.inventory.repositories.AppUsersRepository;
import com.shopery.vendor.inventory.repositories.OrdersRepository;
import com.shopery.vendor.inventory.repositories.ShopsRepository;
import com.shopery.vendor.inventory.services.OrdersService;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrdersServiceImp implements OrdersService {

    private final OrdersRepository ordersRepository;
    private final ModelMapper modelMapper;
    private final ShopsRepository shopsRepository;
    private  final AppUsersRepository appUsersRepository;

    @Autowired
    public  OrdersServiceImp(OrdersRepository ordersRepository,
                             ModelMapper modelMapper,
                             ShopsRepository shopsRepository,
                             AppUsersRepository appUsersRepository
    ){
        this.ordersRepository = ordersRepository;
        this.modelMapper = modelMapper;
        this.shopsRepository = shopsRepository;
        this.appUsersRepository = appUsersRepository;
    }



    @Override
    public OrdersDto createOrder(OrdersDto ordersDto) {
        // Fetch the related entities
        Shops shop = shopsRepository.findById(ordersDto.getShopId())
                .orElseThrow(() -> new EntityNotFoundException("Shop Not Found"));
        AppUsers appUsers = appUsersRepository.findById(ordersDto.getCustomerId())
                .orElseThrow(() -> new EntityNotFoundException("Customer Not Found"));

        // Map DTO to entity
        Orders createOrder = modelMapper.map(ordersDto, Orders.class);

        // Set the fetched relationships
        createOrder.setShop(shop);
        createOrder.setCustomer(appUsers); // Ensure this sets the customer field in Orders
        createOrder.setStatus(OrderStatus.PENDING);

        // Save the order
        Orders saveOrder = ordersRepository.save(createOrder);

        // Map saved entity back to DTO
        return modelMapper.map(saveOrder, OrdersDto.class);
    }

    @Override
    public List<OrdersDto> getCustomerOrdersByShop(UUID customerId, UUID shopId) {
       List<Orders> orders =  ordersRepository.findOrdersByCustomerAndShop(customerId, shopId);
       return  orders.stream()
               .map(order -> modelMapper.map(order, OrdersDto.class))
               .collect(Collectors.toList());
    }

    @Override
    public List<OrdersDto> getOrderByShop(UUID shopId) {
        List<Orders> orders = ordersRepository.findOrderByShop(shopId);
        return orders.stream().map(order -> modelMapper.map(order, OrdersDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<OrdersDto> getAllOrders() {
        List<Orders> orders = ordersRepository.findAll();
        return  orders.stream().map(order -> modelMapper.map(order, OrdersDto.class))
                .collect(Collectors.toList());
    }

}
