package com.tpt.tpt_ecom.category;

import com.tpt.tpt_ecom.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findByCategory_CategoryId(Long categoryCategoryId, Pageable pageable);

    Page<Product> findByProductNameLikeIgnoreCase(String productName, Pageable pageable);
}
