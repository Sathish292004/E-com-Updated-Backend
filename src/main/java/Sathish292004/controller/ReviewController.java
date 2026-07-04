package Sathish292004.controller;

import Sathish292004.dto.response.ProductReviewResponse;
import Sathish292004.dto.request.ReviewRequest;
import Sathish292004.dto.response.ReviewResponse;
import Sathish292004.service.ReviewService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Reviews", description = "Product Review APIs")
@RequestMapping("/api")
@CrossOrigin
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/customer/products/{productId}/reviews")
    public ResponseEntity<ReviewResponse> addReview(
            Authentication authentication,
            @PathVariable Integer productId,
            @RequestBody ReviewRequest request) {

        String email = authentication.getName();

        return new ResponseEntity<>(
                reviewService.addReview(email, productId, request),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/products/{productId}/reviews")
    public ResponseEntity<ProductReviewResponse> getProductReviews(
            @PathVariable Integer productId) {

        return ResponseEntity.ok(
                reviewService.getProductReviews(productId)
        );
    }

    @PutMapping("/customer/reviews/{reviewId}")
    public ResponseEntity<ReviewResponse> updateReview(
            Authentication authentication,
            @PathVariable Integer reviewId,
            @RequestBody ReviewRequest request) {

        String email = authentication.getName();

        return ResponseEntity.ok(
                reviewService.updateReview(email, reviewId, request)
        );
    }

    @DeleteMapping("/customer/reviews/{reviewId}")
    public ResponseEntity<String> deleteReview(
            Authentication authentication,
            @PathVariable Integer reviewId) {

        String email = authentication.getName();

        return ResponseEntity.ok(
                reviewService.deleteReview(email, reviewId)
        );
    }
}