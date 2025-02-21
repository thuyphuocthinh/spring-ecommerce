package com.tpt.tpt_ecom.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemDTO {
    private Long cartItemId;
    private Long cartId;
    private ProductDTO product;
    private Integer quantity;
    private Double discount;
    private Double productPrice;
}
