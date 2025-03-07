package com.tpt.tpt_ecom.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity(name = "categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    @NotBlank(message = "Category name must not be blank")
    @Min(value = 8, message = "Category name must not be less than 8 characters length")
    @Max(value = 200, message = "Category name must not be greater than 200 characters length")
    private String categoryName;

    @OneToMany(mappedBy = "category")
    private Set<Product> products;
}
