package com.tpt.tpt_ecom.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartDTO {
    private Long cartId;
    private Double totalPrice = 0.0;
    private List<CartItemDTO> cartItemDTOS = new ArrayList<>();
}
