package com.tpt.tpt_ecom.service;

import com.tpt.tpt_ecom.dto.CartDTO;

import java.util.List;

public interface CartService {
    CartDTO addProductToCart(Long productId, Integer quantity);
    List<CartDTO> getAllCart();
    CartDTO getCartByUser();
    CartDTO updateCartProductQuantity(Long productId, Integer quantity);
    String deleteCartProductQuantity(Long productId);
}
