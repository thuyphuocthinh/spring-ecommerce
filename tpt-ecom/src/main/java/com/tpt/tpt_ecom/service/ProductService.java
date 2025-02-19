package com.tpt.tpt_ecom.service;

import com.tpt.tpt_ecom.dto.ProductDTO;
import com.tpt.tpt_ecom.dto.ProductResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProductService {
    ProductDTO addProduct(ProductDTO productDTO, MultipartFile image) throws IOException;
    ProductDTO updateProduct(Long productID, ProductDTO productDTO);
    String deleteProduct(Long productID) throws IOException;
    ProductResponse getAllProducts(
            Integer pageNumber,
            Integer pageSize
    );
    ProductDTO getProductDetails(Long productID);
    ProductDTO updateProductImage(Long productID, MultipartFile productImage) throws IOException;
    ProductResponse getProductsByCategory(Long categoryId, Integer pageNumber, Integer pageSize);
    ProductResponse getProductsByKeyword(String keyword, Integer pageNumber, Integer pageSize);
}
