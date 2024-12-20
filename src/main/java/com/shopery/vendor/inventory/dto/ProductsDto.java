package com.shopery.vendor.inventory.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
public class ProductsDto {

    private UUID productId;
    private UUID shopId; // Only include the ID of the shop to simplify the DTO
    private String productName;
    private BigDecimal price;
    private int stock;
    private String productPicture;
    private String productDescription;
    private String productCategory;
    private Date createdAt;
    private Date updatedAt;
    private boolean isDeleted;

    // Default constructor
    public ProductsDto() {}

    // Constructor with necessary parameters
    public ProductsDto(String productName, String productPicture, BigDecimal price, int stock, UUID shopId) {
        this.productName = productName;
        this.productPicture = productPicture;
        this.price = price;
        this.stock = stock;
        this.shopId = shopId;
    }
}
