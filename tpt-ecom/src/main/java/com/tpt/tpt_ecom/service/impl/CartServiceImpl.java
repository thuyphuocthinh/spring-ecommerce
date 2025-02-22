package com.tpt.tpt_ecom.service.impl;

import com.tpt.tpt_ecom.dto.CartDTO;
import com.tpt.tpt_ecom.dto.CartItemDTO;
import com.tpt.tpt_ecom.exceptions.APIException;
import com.tpt.tpt_ecom.exceptions.ResourceNotFoundException;
import com.tpt.tpt_ecom.model.Cart;
import com.tpt.tpt_ecom.model.CartItem;
import com.tpt.tpt_ecom.model.Product;
import com.tpt.tpt_ecom.repository.CartItemRepository;
import com.tpt.tpt_ecom.repository.CartRepository;
import com.tpt.tpt_ecom.repository.ProductRepository;
import com.tpt.tpt_ecom.service.CartService;
import com.tpt.tpt_ecom.util.AuthUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;

    private final AuthUtil authUtil;

    private final ProductRepository productRepository;

    private final CartItemRepository cartItemRepository;

    private final ModelMapper modelMapper;

    public CartServiceImpl(CartRepository cartRepository, AuthUtil authUtil, ProductRepository productRepository, CartItemRepository cartItemRepository, ModelMapper modelMapper) {
        this.cartRepository = cartRepository;
        this.authUtil = authUtil;
        this.productRepository = productRepository;
        this.cartItemRepository = cartItemRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public CartDTO addProductToCart(Long productId, Integer quantity) {
        // create cart or find existing cart from user
        Cart cart = createCart();

        // get product detail
        Product product = this.productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found", "id", productId));

        CartItem cartItem = this.cartItemRepository.
                findCartItemByProductIdAndCartId(cart.getCartId(), productId);

        if(cartItem != null) {
            throw new APIException("Product " + product.getProductName() + " already exists");
        }

        // perform validation
        if(product.getQuantity() == 0) {
            throw new APIException("Product has no quantity");
        }

        if(quantity > product.getQuantity()) {
            throw new APIException("Quantity exceeds maximum quantity");
        }

        // create cart item
        CartItem newCartItem = new CartItem();
        newCartItem.setCart(cart);
        newCartItem.setProduct(product);
        newCartItem.setQuantity(quantity);
        newCartItem.setDiscount(product.getDiscount());
        newCartItem.setProductPrice(product.getPrice());
        this.cartItemRepository.save(newCartItem);

        // reduce product quantity
        // just reduce in product, not in warehouse
        product.setQuantity(product.getQuantity() - quantity);

        // add cart item to cart and save...
        cart.setTotalPrice(cart.getTotalPrice() + product.getPrice());
        List<CartItem> updatedCartItems = cart.getCartItems();
        updatedCartItems.add(newCartItem);
        cart.setCartItems(updatedCartItems);
        this.cartRepository.save(cart);

        // return updated cart
        List<CartItem> cartItems = cart.getCartItems();
        List<CartItemDTO> cartItemDTOS = cartItems.stream()
                .map(c -> modelMapper.map(c, CartItemDTO.class)).toList();
        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
        cartDTO.setCartItemDTOS(cartItemDTOS);

        return cartDTO;
    }

    @Override
    public List<CartDTO> getAllCart() {
        List<Cart> carts = this.cartRepository.findAll();

        if(carts.isEmpty()) {
            throw new APIException("No cart found");
        }

        List<CartDTO> cartDTOs = new ArrayList<>();

        carts.forEach(cart -> {
            CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
            List<CartItem> cartItems = cart.getCartItems();
            List<CartItemDTO> cartItemDTOS = cartItems.stream()
                    .map(c -> modelMapper.map(c, CartItemDTO.class)).toList();
            cartDTO.setCartItemDTOS(cartItemDTOS);
            cartDTOs.add(cartDTO);
        });

        return cartDTOs;
    }

    @Override
    public CartDTO getCartByUser() {
        Cart userCart = cartRepository.findCartByEmail(authUtil.loggedInEmail());

        if(userCart == null) {
            throw new APIException("No cart found");
        }

        CartDTO cartDTO = modelMapper.map(userCart, CartDTO.class);

        return getCartDTO(userCart, cartDTO);
    }

    @Override
    public CartDTO updateCartProductQuantity(Long productId, Integer quantity) {
        Cart userCart = cartRepository.findCartByEmail(authUtil.loggedInEmail());

        if(userCart == null) {
            throw new APIException("No cart found");
        }

        CartItem cartItem = this.cartItemRepository.findCartItemByProductIdAndCartId(productId, userCart.getCartId());
        if(cartItem == null) {
            throw new APIException("No cart item found");
        }
        cartItem.setQuantity(cartItem.getQuantity() + quantity);
        this.cartItemRepository.save(cartItem);

        Cart updatedCart = cartRepository.findById(userCart.getCartId())
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found", "id", userCart.getCartId()));

        CartDTO cartDTO = new CartDTO();
        return getCartDTO(updatedCart, cartDTO);
    }

    @Override
    public String deleteCartProductQuantity(Long productId) {
        Cart userCart = cartRepository.findCartByEmail(authUtil.loggedInEmail());

        if(userCart == null) {
            throw new APIException("No cart found");
        }

        CartItem cartItem = this.cartItemRepository.findCartItemByProductIdAndCartId(productId, userCart.getCartId());
        if(cartItem == null) {
            throw new APIException("No cart item found");
        }

        this.cartItemRepository.delete(cartItem);
        return "Success";
    }

    @Override
    public String deleteProductFromCart(Long productId) {
        Product product = this.productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found", "id", productId));

        Cart userCart = cartRepository.findCartByEmail(authUtil.loggedInEmail());
        if(userCart == null) {
            throw new APIException("No cart found");
        }

        CartItem cartItem = this.cartItemRepository.findCartItemByProductIdAndCartId(productId, userCart.getCartId());
        if(cartItem == null) {
            throw new APIException("No cart item found");
        }

        List<CartItem> cartItemList = userCart.getCartItems();
        for(CartItem cartItem1 : cartItemList) {
            if(cartItem1.getCartItemId().equals(productId)) {
                cartItemList.remove(cartItem1);
                break;
            }
        }

        userCart.setCartItems(cartItemList);
        this.cartRepository.save(userCart);
        this.cartItemRepository.delete(cartItem);

        return "success";
    }

    private CartDTO getCartDTO(Cart updatedCart, CartDTO cartDTO) {
        List<CartItem> cartItems = updatedCart.getCartItems();
        List<CartItemDTO> cartItemDTOS = new ArrayList<>();
        cartItems.forEach(cartItem1 -> {
            CartItemDTO cartItemDTO = modelMapper.map(cartItem1, CartItemDTO.class);
            cartItemDTOS.add(cartItemDTO);
        });

        cartDTO.setCartItemDTOS(cartItemDTOS);

        return cartDTO;
    }

    private Cart createCart() {
        Cart userCart = cartRepository.findCartByEmail(authUtil.loggedInEmail());

        if(userCart != null) {
            return userCart;
        }

        Cart cart = new Cart();
        cart.setTotalPrice(0.0);
        cart.setUser(authUtil.loggedInUser());
        return cartRepository.save(cart);
    }
}
