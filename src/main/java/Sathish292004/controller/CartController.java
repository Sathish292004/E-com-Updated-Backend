package Sathish292004.controller;

import Sathish292004.dto.request.AddToCartRequest;
import Sathish292004.dto.request.UpdateCartItemRequest;
import Sathish292004.dto.response.ApiResponse;
import Sathish292004.dto.response.CartResponse;
import Sathish292004.service.CartService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customer/cart")
@Tag(name = "Cart", description = "Customer Cart APIs")
@CrossOrigin
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/{productId}")
    public ResponseEntity<ApiResponse<String>> addToCart(
            Authentication authentication,
            @PathVariable Integer productId,
            @Valid @RequestBody AddToCartRequest request) {

        String message = cartService.addToCart(
                authentication.getName(),
                productId,
                request
        );

        return ResponseEntity.ok(
                new ApiResponse<>(true, message, null)
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<CartResponse>> getCart(
            Authentication authentication) {

        CartResponse cart = cartService.getCart(authentication.getName());

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Cart fetched successfully",
                        cart
                )
        );
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ApiResponse<String>> updateQuantity(
            Authentication authentication,
            @PathVariable Integer productId,
            @Valid @RequestBody UpdateCartItemRequest request) {

        String message = cartService.updateQuantity(
                authentication.getName(),
                productId,
                request
        );

        return ResponseEntity.ok(
                new ApiResponse<>(true, message, null)
        );
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResponse<String>> removeFromCart(
            Authentication authentication,
            @PathVariable Integer productId) {

        String message = cartService.removeFromCart(
                authentication.getName(),
                productId
        );

        return ResponseEntity.ok(
                new ApiResponse<>(true, message, null)
        );
    }

    @DeleteMapping("/clear")
    public ResponseEntity<ApiResponse<String>> clearCart(
            Authentication authentication) {

        String message = cartService.clearCart(
                authentication.getName()
        );

        return ResponseEntity.ok(
                new ApiResponse<>(true, message, null)
        );
    }
}