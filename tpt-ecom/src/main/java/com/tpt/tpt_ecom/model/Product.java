package com.tpt.tpt_ecom.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long productId;

    @Column(unique=true, nullable=false, length=255)
    private String productName;

    @Column(nullable=false, length=1000)
    private String description;

    @Column(nullable=false)
    private String image;

    @Column(nullable=false)
    private Integer quantity;

    @Column(nullable=false)
    private double price;

    @Column(nullable=false)
    private double discount;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private User user;
}
