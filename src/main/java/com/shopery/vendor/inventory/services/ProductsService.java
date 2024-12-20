package com.shopery.vendor.inventory.services;

import com.shopery.vendor.inventory.dto.ProductsDto;
import com.shopery.vendor.inventory.models.Products;

import java.util.List;
import java.util.UUID;

public interface ProductsService {
    ProductsDto createProduct(ProductsDto productsDto);
    ProductsDto getProductById(UUID productId);
    void deleteProductsById(UUID productId);
    List<ProductsDto> findAllNonDeletedProducts();
    List<Products> getProductsByShopId(UUID shopId);
    ProductsDto updateProductById(ProductsDto productsDto, UUID productId);
}
