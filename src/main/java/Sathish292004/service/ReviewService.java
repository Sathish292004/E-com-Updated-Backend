package Sathish292004.service;

import Sathish292004.dto.request.ReviewRequest;
import Sathish292004.dto.response.ReviewResponse;
import Sathish292004.exception.CustomerNotFoundException;
import Sathish292004.exception.ProductNotFoundException;
import Sathish292004.model.Customer;
import Sathish292004.model.Product;
import Sathish292004.model.Review;
import Sathish292004.repository.CustomerRepo;
import Sathish292004.repository.ProductRepo;
import Sathish292004.repository.ReviewRepo;
import org.springframework.stereotype.Service;
import Sathish292004.dto.response.ProductReviewResponse;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import Sathish292004.dto.response.AdminReviewResponse;
import Sathish292004.exception.ReviewNotFoundException;
import Sathish292004.exception.DuplicateReviewException;

@Service
public class ReviewService {

    private final ReviewRepo reviewRepo;
    private final ProductRepo productRepo;
    private final CustomerRepo customerRepo;

    public ReviewService(ReviewRepo reviewRepo,
                         ProductRepo productRepo,
                         CustomerRepo customerRepo) {
        this.reviewRepo = reviewRepo;
        this.productRepo = productRepo;
        this.customerRepo = customerRepo;
    }

    public ReviewResponse addReview(String email,
                                    Integer productId,
                                    ReviewRequest request) {

        Customer customer = customerRepo.findByEmail(email)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));

        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));

        if (reviewRepo.findByCustomerAndProduct(customer, product).isPresent()) {
            throw new DuplicateReviewException("You have already reviewed this product");        }

        if (request.rating() < 1 || request.rating() > 5) {
            throw new RuntimeException("Rating must be between 1 and 5");
        }

        Review review = Review.builder()
                .rating(request.rating())
                .comment(request.comment())
                .reviewDate(LocalDate.now())
                .customer(customer)
                .product(product)
                .build();

        Review savedReview = reviewRepo.save(review);

        return new ReviewResponse(
                savedReview.getId(),
                customer.getName(),
                product.getName(),
                savedReview.getRating(),
                savedReview.getComment(),
                savedReview.getReviewDate()
        );
    }

    public ProductReviewResponse getProductReviews(Integer productId) {

        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));

        List<Review> reviews = reviewRepo.findByProduct(product);

        List<ReviewResponse> reviewResponses = new ArrayList<>();

        double totalRating = 0;

        for (Review review : reviews) {

            totalRating += review.getRating();

            reviewResponses.add(
                    new ReviewResponse(
                            review.getId(),
                            review.getCustomer().getName(),
                            review.getProduct().getName(),
                            review.getRating(),
                            review.getComment(),
                            review.getReviewDate()
                    )
            );
        }

        double averageRating = 0;

        if (!reviews.isEmpty()) {
            averageRating = totalRating / reviews.size();
        }

        return new ProductReviewResponse(
                product.getId(),
                product.getName(),
                averageRating,
                reviews.size(),
                reviewResponses
        );
    }

    public ReviewResponse updateReview(String email,
                                       Integer reviewId,
                                       ReviewRequest request) {

        Customer customer = customerRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Review review = reviewRepo.findByIdAndCustomer(reviewId, customer)
                .orElseThrow(() -> new ReviewNotFoundException("Review not found"));

        if (request.rating() < 1 || request.rating() > 5) {
            throw new RuntimeException("Rating must be between 1 and 5");
        }

        review.setRating(request.rating());
        review.setComment(request.comment());

        Review updatedReview = reviewRepo.save(review);

        return new ReviewResponse(
                updatedReview.getId(),
                updatedReview.getCustomer().getName(),
                updatedReview.getProduct().getName(),
                updatedReview.getRating(),
                updatedReview.getComment(),
                updatedReview.getReviewDate()
        );
    }

    public String deleteReview(String email, Integer reviewId) {

        Customer customer = customerRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Review review = reviewRepo.findByIdAndCustomer(reviewId, customer)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        reviewRepo.delete(review);

        return "Review deleted successfully";
    }

    public List<AdminReviewResponse> getAllReviews() {

        List<Review> reviews = reviewRepo.findAll();

        List<AdminReviewResponse> response = new ArrayList<>();

        for (Review review : reviews) {

            response.add(
                    new AdminReviewResponse(
                            review.getId(),
                            review.getCustomer().getName(),
                            review.getProduct().getName(),
                            review.getRating(),
                            review.getComment()
                    )
            );
        }

        return response;
    }
}