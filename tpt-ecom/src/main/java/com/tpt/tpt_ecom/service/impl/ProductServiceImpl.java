package com.tpt.tpt_ecom.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tpt.tpt_ecom.category.CategoryRepository;
import com.tpt.tpt_ecom.category.ProductRepository;
import com.tpt.tpt_ecom.dto.CategoryDTO;
import com.tpt.tpt_ecom.dto.PaginationMetadata;
import com.tpt.tpt_ecom.dto.ProductDTO;
import com.tpt.tpt_ecom.dto.ProductResponse;
import com.tpt.tpt_ecom.exceptions.APIException;
import com.tpt.tpt_ecom.exceptions.ResourceNotFoundException;
import com.tpt.tpt_ecom.model.Category;
import com.tpt.tpt_ecom.model.Product;
import com.tpt.tpt_ecom.service.FileService;
import com.tpt.tpt_ecom.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.management.relation.RelationServiceNotRegisteredException;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    private final CategoryRepository categoryRepository;

    private final FileService fileService;

    private final ModelMapper modelMapper;

    @Value("${project.images}")
    // path to directory
    private String path;

    @Value("${project.baseUrl}")
    private String baseUrl;

    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository, FileService fileService, ModelMapper modelMapper) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.fileService = fileService;
        this.modelMapper = modelMapper;
    }

    public ProductDTO addProduct(ProductDTO productDTO, MultipartFile image) throws IOException {
        Optional<Category> category = this.categoryRepository.findById(productDTO.getCategoryId());

        if(category.isEmpty()) {
            throw new ResourceNotFoundException("Category not found", "categoryId", productDTO.getCategoryId());
        }

        // implement upload service later
        if(Files.exists(Paths.get(this.path + File.separator + image.getOriginalFilename()))) {
            throw new FileAlreadyExistsException("File already exists! Please enter another file name!");
        }

        String fileName = fileService.uploadFile(path, image);
        // upload service returns a link => store this link to db

        Product product = modelMapper.map(productDTO, Product.class);
        product.setImage(fileName);
        product.setCategory(category.get());

        Product productSaved = this.productRepository.save(product);

        return this.modelMapper.map(productSaved, ProductDTO.class);
    }

    @Override
    public ProductDTO updateProduct(Long productID, ProductDTO productDTO) {
        Optional<Product> product = this.productRepository.findById(productID);

        if(product.isEmpty()) {
            throw new ResourceNotFoundException("Product not found", "productId", productID);
        }

        Product updatedProduct = modelMapper.map(productDTO, Product.class);
        updatedProduct.setProductId(productID);

        this.productRepository.save(updatedProduct);

        return productDTO;
    }

    @Override
    public String deleteProduct(Long productID) throws IOException {
        Optional<Product> product = this.productRepository.findById(productID);

        if(product.isEmpty()) {
            throw new ResourceNotFoundException("Product not found", "productId", productID);
        }

        Files.deleteIfExists(Paths.get(this.path + File.separator + product.get().getImage()));

        this.productRepository.deleteById(productID);

        return "Deleted successfully";
    }

    @Override
    public ProductResponse getAllProducts(Integer pageNumber, Integer pageSize) {
        Pageable page = PageRequest.of(pageNumber, pageSize);
        Page<Product> productPage = this.productRepository.findAll(page);
        return getProductResponse(productPage);
    }

    @Override
    public ProductDTO getProductDetails(Long productID) {
        Optional<Product> product = this.productRepository.findById(productID);

        if(product.isEmpty()) {
            throw new ResourceNotFoundException("Product not found", "productId", productID);
        }

        ProductDTO productDTO = this.modelMapper.map(product.get(), ProductDTO.class);
        productDTO.setImage(this.baseUrl + product.get().getImage());

        return productDTO;
    }

    @Override
    public ProductDTO updateProductImage(Long productID, MultipartFile productImage) throws IOException {
        // Get product from db
        Optional<Product> product = this.productRepository.findById(productID);

        if(product.isEmpty()) {
            throw new ResourceNotFoundException("Product not found", "productId", productID);
        }
        // Upload image using file service to server folder /images
        // implement upload service later
        if(Files.exists(Paths.get(this.path + File.separator + productImage.getOriginalFilename()))) {
            throw new FileAlreadyExistsException("File already exists! Please enter another file name!");
        }

        String fileName = fileService.uploadFile(path, productImage);
        // Get image name and set to product then store to db
        Product product1 = product.get();
        product1.setImage(fileName);

        this.productRepository.save(product1);

        return modelMapper.map(product1, ProductDTO.class);
    }

    @Override
    public ProductResponse getProductsByCategory(Long categoryId, Integer pageNumber, Integer pageSize) {
        Optional<Category> category = this.categoryRepository.findById(categoryId);

        if(category.isEmpty()) {
            throw new ResourceNotFoundException("Category not found", "categoryId", categoryId);
        }

        Page<Product> productPage = this.productRepository.findByCategory_CategoryId(categoryId, PageRequest.of(pageNumber, pageSize));
        return getProductResponse(productPage);
    }

    @Override
    public ProductResponse getProductsByKeyword(String keyword, Integer pageNumber, Integer pageSize) {
        Pageable page = PageRequest.of(pageNumber, pageSize);
        Page<Product> productPage = this.productRepository.findByProductNameLikeIgnoreCase(keyword, page);
        return getProductResponse(productPage);
    }

    private ProductResponse getProductResponse(Page<Product> productPage) {
        List<Product> products = productPage.getContent();

        List<ProductDTO> productDTOS =  products.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setProducts(productDTOS);

        productDTOS.forEach(productDTO -> {
            productDTO.setImage(this.baseUrl + "/" + productDTO.getImage());
        });

        PaginationMetadata paginationMetadata = PaginationMetadata
                .builder().pageNumber(productPage.getNumber())
                .pageSize(productPage.getSize())
                .totalPages(productPage.getTotalPages())
                .lastPage(productPage.isLast())
                .build();

        productResponse.setPaginationMetadata(paginationMetadata);

        return productResponse;
    }
}
