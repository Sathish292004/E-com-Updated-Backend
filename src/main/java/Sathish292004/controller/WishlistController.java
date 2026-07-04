package Sathish292004.controller;

import Sathish292004.dto.response.WishlistResponse;
import Sathish292004.service.WishlistService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Wishlist", description = "Wishlist APIs")
@RequestMapping("/api/customer/wishlist")
@CrossOrigin
public class WishlistController {

    private final WishlistService wishlistService;

    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    @PostMapping("/{productId}")
    public ResponseEntity<String> addToWishlist(
            Authentication authentication,
            @PathVariable Integer productId) {

        String email = authentication.getName();

        return ResponseEntity.ok(
                wishlistService.addToWishlist(email, productId)
        );
    }

    @GetMapping
    public ResponseEntity<List<WishlistResponse>> getWishlist(
            Authentication authentication) {

        String email = authentication.getName();

        return ResponseEntity.ok(
                wishlistService.getWishlist(email)
        );
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<String> removeFromWishlist(
            Authentication authentication,
            @PathVariable Integer productId) {

        String email = authentication.getName();

        return ResponseEntity.ok(
                wishlistService.removeFromWishlist(email, productId)
        );
    }
}