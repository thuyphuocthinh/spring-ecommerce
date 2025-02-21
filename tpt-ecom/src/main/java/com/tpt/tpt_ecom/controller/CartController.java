package com.tpt.tpt_ecom.controller;

import com.tpt.tpt_ecom.dto.CartDTO;
import com.tpt.tpt_ecom.service.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/carts")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/public/products/{productId}/quantity/{quantity}")
    public ResponseEntity<CartDTO> addProductToCart(
            @PathVariable Long productId,
            @PathVariable Integer quantity
    ) {
        return new ResponseEntity<>(
                cartService.addProductToCart(productId, quantity),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/admin/get-all")
    public ResponseEntity<List<CartDTO>> getAllCarts() {
        return new ResponseEntity<>(
                cartService.getAllCart(),
                HttpStatus.OK
        );
    }

    @GetMapping("/public/users")
    public ResponseEntity<CartDTO> getCartByUser() {
        return new ResponseEntity<>(
                cartService.getCartByUser(),
                HttpStatus.OK
        );
    }

    @PutMapping("/public/products/{productId}/quantity/{operation}")
    public ResponseEntity<CartDTO> updateQuantity(
            @PathVariable Long productId,
            @PathVariable String operation
    ) {
        CartDTO cartDTO = this.cartService.updateCartProductQuantity(
                productId,
                operation.equalsIgnoreCase("decrease") ? -1 : 1
        );
        return new ResponseEntity<>(cartDTO, HttpStatus.OK);
    }

    @DeleteMapping("/public/products/{productId}")
    public ResponseEntity<String> deleteProductFromCart(
            @PathVariable Long productId
    ) {
        return new ResponseEntity<>(
                this.cartService.deleteCartProductQuantity(productId),
                HttpStatus.OK
        );
    }
}
