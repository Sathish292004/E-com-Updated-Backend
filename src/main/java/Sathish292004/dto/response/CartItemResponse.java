package Sathish292004.dto.response;

import java.math.BigDecimal;

public record CartItemResponse(

        Integer productId,
        String productName,
        String brand,
        String category,
        String imageName,
        Integer quantity,
        BigDecimal price,
        BigDecimal totalPrice,
        boolean productAvailable

) {
}