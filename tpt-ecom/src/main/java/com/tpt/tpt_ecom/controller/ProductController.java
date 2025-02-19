package com.tpt.tpt_ecom.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tpt.tpt_ecom.config.AppConstants;
import com.tpt.tpt_ecom.dto.ProductDTO;
import com.tpt.tpt_ecom.dto.ProductResponse;
import com.tpt.tpt_ecom.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/")
public class ProductController {
    private final ProductService productService;

    private final ObjectMapper objectMapper;

    public ProductController(ProductService productService, ObjectMapper objectMapper) {
        this.productService = productService;
        this.objectMapper = objectMapper;
    }

    @PostMapping("/admin/products")
    public ResponseEntity<ProductDTO> addProduct(@RequestPart String productStr, @RequestPart MultipartFile image) throws IOException {
        ProductDTO pdto = objectMapper.readValue(productStr, ProductDTO.class);
        return new ResponseEntity<>(this.productService.addProduct(pdto, image), HttpStatus.CREATED);
    }

    @GetMapping("/admin/products")
    public ResponseEntity<ProductResponse> getAllProducts(
            @RequestParam(name = "pageNumber", required = false, defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
            @RequestParam(name = "pageSize", required = false, defaultValue = AppConstants.PAGE_SIZE) Integer pageSize
    ) {
        return new ResponseEntity<>(this.productService.getAllProducts(pageNumber, pageSize), HttpStatus.OK);
    }

    @GetMapping("/public/products/{categoryId}")
    public ResponseEntity<ProductResponse> getProductsByCategory(
            @RequestParam(name = "pageNumber", required = false, defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
            @RequestParam(name = "pageSize", required = false, defaultValue = AppConstants.PAGE_SIZE) Integer pageSize,
            @PathVariable Long categoryId
    ) {
        return new ResponseEntity<>(this.productService.getProductsByCategory(categoryId, pageNumber, pageSize), HttpStatus.OK);
    }

    @GetMapping("/public/products/{productId}")
    public ResponseEntity<ProductDTO> getProductById(
            @PathVariable Long productId
    ) {
        return new ResponseEntity<>(this.productService.getProductDetails(productId), HttpStatus.OK);
    }

    @GetMapping("/public/products/keyword/{keyword}")
    public ResponseEntity<ProductResponse> getProductsByKeyword(
            @RequestParam(name = "pageNumber", required = false, defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
            @RequestParam(name = "pageSize", required = false, defaultValue = AppConstants.PAGE_SIZE) Integer pageSize,
            @PathVariable String keyword
    ) {
        return new ResponseEntity<>(this.productService.getProductsByKeyword(keyword, pageNumber, pageSize), HttpStatus.OK);
    }

    @PutMapping("/admin/products/{productId}")
    public ResponseEntity<ProductDTO> updateProduct(
            @RequestBody ProductDTO productDTO,
            @PathVariable Long productId
    ) {
        return new ResponseEntity<>(this.productService.updateProduct(productId, productDTO), HttpStatus.OK);
    }

    @DeleteMapping("/admin/products/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long productId) throws IOException {
        return new ResponseEntity<>(this.productService.deleteProduct(productId), HttpStatus.OK);
    }

    @PutMapping("/admin/products/{productId}/image")
    public ResponseEntity<ProductDTO> updateProductImage(
            @PathVariable Long productId,
            @RequestPart(name = "image") MultipartFile image
    ) throws IOException {
        return new ResponseEntity<>(this.productService.updateProductImage(productId, image), HttpStatus.OK);
    }
}
