package com.shopery.vendor.inventory.services.impl;

import com.shopery.vendor.inventory.dto.ProductsDto;
import com.shopery.vendor.inventory.models.Products;
import com.shopery.vendor.inventory.models.Shops;
import com.shopery.vendor.inventory.repositories.ProductsRepository;
import com.shopery.vendor.inventory.repositories.ShopsRepository;
import com.shopery.vendor.inventory.services.ProductsService;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProductsServiceImpl implements ProductsService {

    private final ProductsRepository productsRepository;
    private final ModelMapper modelMapper;
    private  final ShopsRepository shopsRepository;

    @Autowired
    public ProductsServiceImpl(ProductsRepository productsRepository,ShopsRepository shopsRepository, ModelMapper modelMapper ){
        this.shopsRepository = shopsRepository;
        this.productsRepository = productsRepository;
        this.modelMapper = modelMapper;
    }



    @Override
    public ProductsDto createProduct(ProductsDto productsDto) {
        Shops shop = shopsRepository.findById(productsDto.getShopId())
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Shop with ID %s not found.", productsDto.getShopId())
                ));

        Products product = modelMapper.map(productsDto, Products.class);
        product.setShop(shop);
        Products savedProduct = productsRepository.save(product);

        // Convert saved Product entity back to DTO
        return modelMapper.map(savedProduct, ProductsDto.class);
    }

    @Override
    public ProductsDto getProductById(UUID productId) {
        Products product = productsRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Product with ID %s not found.", productId)
                ));

        return modelMapper.map(product, ProductsDto.class);
    }


    @Override
    public void deleteProductsById(UUID productId) {
        Products product = productsRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with ID: " + productId));

        // Set the is_deleted flag to true
        product.setDeleted(true);

        // Save the updated product back to the database
        productsRepository.save(product);
    }

    @Override
    public List<ProductsDto> findAllNonDeletedProducts() {
        List<Products> products = productsRepository.findAll();

        return products.stream()
                .map(product -> modelMapper.map(product, ProductsDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<Products> getProductsByShopId(UUID shopId) {
        return productsRepository.getProductsByShopId(shopId);
    }


    @Override
    public ProductsDto updateProductById(ProductsDto productsDto, UUID productId) {
        // Fetch the existing product from the database
        Products product = productsRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Product with ID %s not found.", productId)
                ));
            product.setProductName(productsDto.getProductName());
            product.setPrice(productsDto.getPrice());
            product.setStock(productsDto.getStock());
            product.setProductPicture(productsDto.getProductPicture());
            Products updatedProduct =  productsRepository.save(product);

            return modelMapper.map(updatedProduct, ProductsDto.class);

    }




}
