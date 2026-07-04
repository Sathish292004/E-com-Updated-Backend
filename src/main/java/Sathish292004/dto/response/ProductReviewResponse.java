package Sathish292004.dto.response;

import java.util.List;

public record ProductReviewResponse(

        Integer productId,
        String productName,
        Double averageRating,
        Integer totalReviews,
        List<ReviewResponse> reviews

) {
}