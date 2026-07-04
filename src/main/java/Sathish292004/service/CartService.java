package Sathish292004.service;

import Sathish292004.dto.request.AddToCartRequest;
import Sathish292004.dto.request.UpdateCartItemRequest;
import Sathish292004.dto.response.CartItemResponse;
import Sathish292004.dto.response.CartResponse;
import Sathish292004.exception.CustomerNotFoundException;
import Sathish292004.exception.ProductNotFoundException;
import Sathish292004.model.Cart;
import Sathish292004.model.CartItem;
import Sathish292004.model.Customer;
import Sathish292004.model.Product;
import Sathish292004.repository.CartItemRepo;
import Sathish292004.repository.CartRepo;
import Sathish292004.repository.CustomerRepo;
import Sathish292004.repository.ProductRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class CartService {

    private final CartRepo cartRepo;
    private final CartItemRepo cartItemRepo;
    private final CustomerRepo customerRepo;
    private final ProductRepo productRepo;

    public CartService(CartRepo cartRepo,
                       CartItemRepo cartItemRepo,
                       CustomerRepo customerRepo,
                       ProductRepo productRepo) {

        this.cartRepo = cartRepo;
        this.cartItemRepo = cartItemRepo;
        this.customerRepo = customerRepo;
        this.productRepo = productRepo;
    }

    private Cart getOrCreateCart(Customer customer) {

        return cartRepo.findByCustomer(customer)
                .orElseGet(() -> {

                    Cart cart = new Cart();
                    cart.setCustomer(customer);

                    return cartRepo.save(cart);
                });
    }
    @Transactional
    public String addToCart(String email,
                            Integer productId,
                            AddToCartRequest request) {

        Customer customer = customerRepo.findByEmail(email)
                .orElseThrow(() ->
                        new CustomerNotFoundException("Customer not found"));

        Product product = productRepo.findById(productId)
                .orElseThrow(() ->
                        new ProductNotFoundException("Product not found"));

        Cart cart = getOrCreateCart(customer);

        CartItem cartItem = cartItemRepo.findByCartAndProduct(cart, product)
                .orElse(null);

        if (cartItem == null) {

            cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(request.quantity());

        } else {

            cartItem.setQuantity(
                    cartItem.getQuantity() + request.quantity()
            );
        }

        cartItem.setTotalPrice(
                product.getPrice()
                        .multiply(BigDecimal.valueOf(cartItem.getQuantity()))
        );

        cartItemRepo.save(cartItem);

        return "Product added to cart";
    }

    public CartResponse getCart(String email) {

        Customer customer = customerRepo.findByEmail(email)
                .orElseThrow(() ->
                        new CustomerNotFoundException("Customer not found"));

        Cart cart = getOrCreateCart(customer);

        List<CartItem> cartItems = cartItemRepo.findByCart(cart);

        List<CartItemResponse> responseItems = new ArrayList<>();

        BigDecimal totalAmount = BigDecimal.ZERO;
        int totalItems = 0;

        for (CartItem item : cartItems) {

            Product product = item.getProduct();

            responseItems.add(
                    new CartItemResponse(
                            product.getId(),
                            product.getName(),
                            product.getBrand(),
                            product.getCategory(),
                            product.getImageName(),
                            item.getQuantity(),
                            product.getPrice(),
                            item.getTotalPrice(),
                            product.isProductAvailable()
                    )
            );

            totalItems += item.getQuantity();
            totalAmount = totalAmount.add(item.getTotalPrice());
        }

        return new CartResponse(
                responseItems,
                totalItems,
                totalAmount
        );
    }

    @Transactional
    public String updateQuantity(String email,
                                 Integer productId,
                                 UpdateCartItemRequest request) {

        Customer customer = customerRepo.findByEmail(email)
                .orElseThrow(() ->
                        new CustomerNotFoundException("Customer not found"));

        Product product = productRepo.findById(productId)
                .orElseThrow(() ->
                        new ProductNotFoundException("Product not found"));

        Cart cart = getOrCreateCart(customer);

        CartItem cartItem = cartItemRepo.findByCartAndProduct(cart, product)
                .orElseThrow(() ->
                        new RuntimeException("Product not found in cart"));

        cartItem.setQuantity(request.quantity());

        cartItem.setTotalPrice(
                product.getPrice()
                        .multiply(BigDecimal.valueOf(request.quantity()))
        );

        cartItemRepo.save(cartItem);

        return "Cart updated successfully";
    }

    @Transactional
    public String removeFromCart(String email,
                                 Integer productId) {

        Customer customer = customerRepo.findByEmail(email)
                .orElseThrow(() ->
                        new CustomerNotFoundException("Customer not found"));

        Product product = productRepo.findById(productId)
                .orElseThrow(() ->
                        new ProductNotFoundException("Product not found"));

        Cart cart = getOrCreateCart(customer);

        CartItem cartItem = cartItemRepo.findByCartAndProduct(cart, product)
                .orElseThrow(() ->
                        new RuntimeException("Product not found in cart"));

        cartItemRepo.delete(cartItem);

        return "Product removed from cart";
    }



    @Transactional
    public String clearCart(String email) {

        Customer customer = customerRepo.findByEmail(email)
                .orElseThrow(() ->
                        new CustomerNotFoundException("Customer not found"));

        Cart cart = getOrCreateCart(customer);

        List<CartItem> items = cartItemRepo.findByCart(cart);

        cartItemRepo.deleteAll(items);

        return "Cart cleared successfully";
    }
}