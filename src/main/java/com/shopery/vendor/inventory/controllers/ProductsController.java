package com.shopery.vendor.inventory.controllers;

import com.shopery.vendor.inventory.dto.ProductsDto;
import com.shopery.vendor.inventory.models.Products;
import com.shopery.vendor.inventory.services.ProductsService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.PostUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/products")
public class ProductsController {

    private final ProductsService  productsService;

    @Autowired
    public ProductsController(ProductsService productsService){
        this.productsService = productsService;
    }

    @Operation(summary="Creating Product")
    @PostMapping
    public ResponseEntity<ProductsDto> createProducts(@RequestBody ProductsDto productsDto){
        return new ResponseEntity<>(productsService.createProduct(productsDto), HttpStatus.CREATED);
    }

    @Operation(summary = "Get Product by ID")
    @GetMapping("/{productId}")
    public ResponseEntity<ProductsDto> getProductById(@PathVariable UUID productId) {
        return new ResponseEntity<>(productsService.getProductById(productId), HttpStatus.OK);
    }

    @Operation(summary = "Get All Products")
    @GetMapping
    public ResponseEntity<List<ProductsDto>> findAllNonDeletedProducts() {
        return new ResponseEntity<>(productsService.findAllNonDeletedProducts(), HttpStatus.OK);
    }

    @Operation(summary = "Soft Delete Product by ID")
    @PutMapping("/productStatus/{productId}")
    public ResponseEntity<Void> deleteProductById(@PathVariable UUID productId) {
        productsService.deleteProductsById(productId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Get Products by Shop")
    @GetMapping("/shop/{shopId}")
    public ResponseEntity<List<Products>> getProductsByShopId(@PathVariable UUID shopId) {
        List<Products> products = productsService.getProductsByShopId(shopId);
        return ResponseEntity.ok(products);
    }

    @Operation(summary = "Update Product by ID")
    @PatchMapping("/{productId}")
    public ResponseEntity<ProductsDto> updateProductById(@RequestBody ProductsDto productsDto, @PathVariable UUID productId) {
        ProductsDto updatedProduct = productsService.updateProductById(productsDto, productId);
        return ResponseEntity.ok(updatedProduct);
    }





}
