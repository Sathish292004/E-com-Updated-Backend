package Sathish292004.dto.response;

public record AdminReviewResponse(

        Integer reviewId,
        String customerName,
        String productName,
        Integer rating,
        String comment

) {
}